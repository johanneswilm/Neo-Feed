/*
 * This file is part of Neo Feed
 * Copyright (c) 2025   Neo Feed Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.saulhdev.feeder.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.saulhdev.feeder.R
import com.saulhdev.feeder.data.content.FeedPreferences
import com.saulhdev.feeder.data.db.models.Feed
import com.saulhdev.feeder.data.entity.SORT_CHRONOLOGICAL
import com.saulhdev.feeder.databinding.ContentSortingBinding
import com.saulhdev.feeder.databinding.ContentSourcesBinding
import com.saulhdev.feeder.databinding.ContentTagsBinding
import com.saulhdev.feeder.databinding.SortFilterSheetBinding
import com.saulhdev.feeder.viewmodels.SortFilterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

@SuppressLint("ViewConstructor")
class FilterBottomSheet(
    context: Context,
    private val callback: () -> Unit
) : FrameLayout(context), View.OnClickListener {

    private val viewModel: SortFilterViewModel by inject(SortFilterViewModel::class.java)
    private val prefs: FeedPreferences by inject(FeedPreferences::class.java)
    private val mainScope = CoroutineScope(Dispatchers.Main + Job())
    private var _binding: SortFilterSheetBinding? = null
    private val binding get() = _binding!!
    private var sortingBinding: ContentSortingBinding
    private var sourcesBinding: ContentSourcesBinding
    private var tagsBinding: ContentTagsBinding

    private var activeSources: List<Feed>
    private var activeTags: List<String>
    private val excludedSources: MutableSet<String>
    private val excludedTags: MutableSet<String>

    init {
        _binding = SortFilterSheetBinding.inflate(LayoutInflater.from(context), this, true)

        sortingBinding = ContentSortingBinding.inflate(LayoutInflater.from(context))
        binding.sortingCard.setContentView(sortingBinding.root)

        sourcesBinding = ContentSourcesBinding.inflate(LayoutInflater.from(context))
        binding.sourcesCard.setContentView(sourcesBinding.root)

        tagsBinding = ContentTagsBinding.inflate(LayoutInflater.from(context))
        binding.tagsCard.setContentView(tagsBinding.root)

        binding.btnApply.setOnClickListener(this)
        binding.btnReset.setOnClickListener(this)
        tagsBinding.btnSelectAllTags.setOnClickListener(this)
        tagsBinding.btnDeselectAllTags.setOnClickListener(this)
        sourcesBinding.btnSelectAllSources.setOnClickListener(this)
        sourcesBinding.btnDeselectAllSources.setOnClickListener(this)

        // Load excluded source/tag sets directly from prefs so we don't rely on the
        // ViewModel StateFlow having emitted the latest values before the sheet opens.
        excludedSources = prefs.sourcesFilter.getValue().toMutableSet()
        excludedTags = prefs.tagsFilter.getValue().toMutableSet()

        // Active sources/tags are read from the current StateFlow value; the coroutine
        // below will rebuild the chips once the repository finishes loading.
        val state = viewModel.sheetState.value
        activeSources = state.activeSources
        activeTags = state.activeTags

        // Read sort preferences directly for the same reason.
        val currentSort = prefs.sortingFilter.getValue()
        val currentSortAsc = prefs.sortingAsc.getValue()
        setupSortOptions(currentSort, currentSortAsc)
        setupSources()
        setupTags()

        // The active sources/tags may not be loaded yet when the sheet first opens.
        // Collect subsequent emissions and rebuild the chips when the data arrives.
        mainScope.launch {
            viewModel.sheetState.collect { state ->
                val newSources = state.activeSources
                val newTags = state.activeTags
                if (newSources != activeSources || newTags != activeTags) {
                    activeSources = newSources
                    activeTags = newTags
                    setupSources()
                    setupTags()
                }
            }
        }
    }

    private fun setupSortOptions(currentSort: String, currentSortAsc: Boolean) {
        // Check the chip that matches the current sort type.
        when (currentSort) {
            SORT_CHRONOLOGICAL -> sortingBinding.cgSortOptions.check(R.id.chip_sort_chronological)
            context.getString(R.string.sorting_title) -> sortingBinding.cgSortOptions.check(R.id.chip_sort_title)
            context.getString(R.string.sorting_source) -> sortingBinding.cgSortOptions.check(R.id.chip_sort_source)
            else -> sortingBinding.cgSortOptions.check(R.id.chip_sort_chronological)
        }

        // Check the button that matches the current direction.
        // XML order: left = desc (Newest/Descending), right = asc (Oldest/Ascending).
        if (currentSortAsc) {
            sortingBinding.toggleSortDirection.check(R.id.btn_sort_asc)
        } else {
            sortingBinding.toggleSortDirection.check(R.id.btn_sort_desc)
        }

        updateSortDirectionLabels(currentSort)

        sortingBinding.cgSortOptions.setOnCheckedStateChangeListener { _, _ ->
            val selectedSort = getSelectedSortOption()
            updateSortDirectionLabels(selectedSort)
        }
    }

    private fun getSelectedSortOption(): String {
        return when (sortingBinding.cgSortOptions.checkedChipId) {
            R.id.chip_sort_title -> context.getString(R.string.sorting_title)
            R.id.chip_sort_source -> context.getString(R.string.sorting_source)
            else -> SORT_CHRONOLOGICAL
        }
    }

    private fun updateSortDirectionLabels(sort: String) {
        if (sort == SORT_CHRONOLOGICAL) {
            sortingBinding.tvSortDirectionLabel.visibility = VISIBLE
            sortingBinding.btnSortDesc.text = context.getString(R.string.sort_newest)
            sortingBinding.btnSortDesc.icon = AppCompatResources.getDrawable(context, R.drawable.ic_sort_descending)
            sortingBinding.btnSortAsc.text = context.getString(R.string.sort_oldest)
            sortingBinding.btnSortAsc.icon = AppCompatResources.getDrawable(context, R.drawable.ic_sort_ascending)
        } else {
            sortingBinding.tvSortDirectionLabel.visibility = INVISIBLE
            sortingBinding.btnSortDesc.text = context.getString(R.string.sort_descending)
            sortingBinding.btnSortDesc.icon = AppCompatResources.getDrawable(context, R.drawable.ic_sort_descending)
            sortingBinding.btnSortAsc.text = context.getString(R.string.sort_ascending)
            sortingBinding.btnSortAsc.icon = AppCompatResources.getDrawable(context, R.drawable.ic_sort_ascending)
        }
    }

    private fun setupSources() {
        sourcesBinding.allSourcesGroup.removeAllViews()
        activeSources.sortedBy { it.title.lowercase() }.forEach { feed ->
            val checked = !excludedSources.contains(feed.id.toString())
            val chip = createChip(
                chipName = feed.title,
                checked = checked,
            ) { isChecked ->
                val feedId = feed.id.toString()
                if (isChecked) {
                    excludedSources.remove(feedId)
                } else {
                    excludedSources.add(feedId)
                }
                updateSourcesButtonsVisibility()
            }
            sourcesBinding.allSourcesGroup.addView(chip)
        }
        updateSourcesButtonsVisibility()
    }

    private fun setupTags() {
        tagsBinding.allTagsGroup.removeAllViews()
        activeTags.sortedBy { it.lowercase() }.forEach { tagName ->
            val checked = !excludedTags.contains(tagName)
            val chip = createChip(
                chipName = tagName,
                checked = checked,
            ) { isChecked ->
                if (isChecked) {
                    excludedTags.remove(tagName)
                } else {
                    excludedTags.add(tagName)
                }
                updateTagButtonsVisibility()
            }
            tagsBinding.allTagsGroup.addView(chip)
        }
        updateTagButtonsVisibility()
    }

    private fun createChip(chipName: String, checked: Boolean, callback: (Boolean) -> Unit): Chip {
        return Chip(context).apply {
            text = chipName
            isCheckable = true
            isChecked = checked
            checkedIcon = AppCompatResources.getDrawable(context, R.drawable.ic_check_24)
            checkedIconTint = getColorStateList()
            chipIcon = if (isChecked) {
                AppCompatResources.getDrawable(context, R.drawable.ic_check_24)
            } else {
                AppCompatResources.getDrawable(context, R.drawable.ic_circle_24dp)
            }
            chipStrokeColor = getColorStateList()
            chipStrokeWidth = 1f
            setOnCheckedChangeListener { _, isChecked ->
                chipIcon = if (isChecked) {
                    AppCompatResources.getDrawable(context, R.drawable.ic_check_24)
                } else {
                    AppCompatResources.getDrawable(context, R.drawable.ic_circle_24dp)
                }
                callback(isChecked)
            }
        }
    }

    private fun getColorStateList(): android.content.res.ColorStateList? {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(
            com.google.android.material.R.attr.colorPrimaryFixed,
            typedValue,
            true
        )
        return ContextCompat.getColorStateList(context, typedValue.resourceId)
    }

    private fun updateTagButtonsVisibility() {
        val anyChecked = tagsBinding.allTagsGroup.children
            .filterIsInstance<Chip>()
            .any { it.isChecked }
        tagsBinding.btnSelectAllTags.visibility = if (anyChecked) GONE else VISIBLE
        tagsBinding.btnDeselectAllTags.visibility = if (anyChecked) VISIBLE else GONE
    }

    private fun updateSourcesButtonsVisibility() {
        val anyChecked = sourcesBinding.allSourcesGroup.children
            .filterIsInstance<Chip>()
            .any { it.isChecked }
        sourcesBinding.btnSelectAllSources.visibility = if (anyChecked) GONE else VISIBLE
        sourcesBinding.btnDeselectAllSources.visibility = if (anyChecked) VISIBLE else GONE
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_apply                -> {
                val sortingOption = when (sortingBinding.toggleSortDirection.checkedButtonId) {
                    R.id.btn_sort_asc -> true
                    R.id.btn_sort_desc -> false
                    else -> false
                }

                prefs.sourcesFilter.setValue(excludedSources)
                prefs.tagsFilter.setValue(excludedTags)
                prefs.sortingAsc.setValue(sortingOption)
                prefs.sortingFilter.setValue(getSelectedSortOption())

                callback()
            }

            R.id.btn_reset                -> {
                excludedSources.clear()
                excludedTags.clear()

                prefs.sourcesFilter.setValue(emptySet())
                prefs.tagsFilter.setValue(emptySet())
                prefs.sortingAsc.setValue(false)
                prefs.sortingFilter.setValue(SORT_CHRONOLOGICAL)

                setupSortOptions(SORT_CHRONOLOGICAL, false)
                setupSources()
                setupTags()

                callback()
            }

            R.id.btn_deselect_all_tags    -> {
                tagsBinding.allTagsGroup.children
                    .filterIsInstance<Chip>()
                    .forEach {
                        it.isChecked = false
                        excludedTags.add(it.text.toString())
                    }
                updateTagButtonsVisibility()
            }

            R.id.btn_select_all_tags      -> {
                tagsBinding.allTagsGroup.children
                    .filterIsInstance<Chip>()
                    .forEach {
                        it.isChecked = true
                        excludedTags.remove(it.text.toString())
                    }
                updateTagButtonsVisibility()
            }

            R.id.btn_deselect_all_sources -> {
                sourcesBinding.allSourcesGroup.children
                    .filterIsInstance<Chip>()
                    .forEach { chip ->
                        chip.isChecked = false
                        activeSources.find { it.title == chip.text.toString() }?.id?.toString()?.let {
                            excludedSources.add(it)
                        }
                    }
                updateSourcesButtonsVisibility()
            }

            R.id.btn_select_all_sources   -> {
                sourcesBinding.allSourcesGroup.children
                    .filterIsInstance<Chip>()
                    .forEach { chip ->
                        chip.isChecked = true
                        activeSources.find { it.title == chip.text.toString() }?.id?.toString()?.let {
                            excludedSources.remove(it)
                        }
                    }
                updateSourcesButtonsVisibility()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mainScope.cancel()
        _binding = null
    }

    companion object {
        fun show(context: Context, animate: Boolean) {
            val sheet = BaseBottomSheet.inflate(context)
            sheet.show(FilterBottomSheet(context) { sheet.close(true) }, animate)
        }
    }
}
