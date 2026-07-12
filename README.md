<h1 align="center">
  <img src="fastlane/metadata/android/en-US/images/icon.png" alt="Neo Store's icon" width="192" height="192"/>
  <br>
  Neo Feed
  <a href="https://shields.rbtlog.dev/com.machiav3lli.fdroid">
    <img alt="Reproducible build badge" src="https://shields.rbtlog.dev/simple/com.saulhdev.neofeed?style=flat&labelColor=A8F8A5&color=98E796"/>
  </a>
</h1>

<p align="center"><strong>Custom Google Discover Feed replacement for launchers!</strong></p>

<div align="center">

[![GitHub repo stars](https://img.shields.io/github/stars/NeoApplications/Neo-Feed?style=flat&labelColor=DDBBFF&color=6650A4)](https://github.com/NeoApplications/Neo-Feed/stargazers)
[![Build Status](https://img.shields.io/github/actions/workflow/status/NeoApplications/Neo-Feed/android.yml?style=flat&labelColor=DDBBFF&color=6650A4)](https://github.com/NeoApplications/Neo-Feed/actions?query=workflow%3A%22Omega+Feeder+CI%22+event%3Apush)
[![GitHub License](https://img.shields.io/github/license/NeoApplications/Neo-Feed?style=flat&labelColor=DDBBFF&color=6650A4)](https://github.com/NeoApplications/Neo-Feed/blob/main/LICENSE)
[![GitHub All Releases](https://img.shields.io/github/downloads/NeoApplications/Neo-Feed/total.svg?style=flat&labelColor=DDBBFF&color=6650A4)](https://github.com/NeoApplications/Neo-Feed/releases/)
[![GitHub release](https://img.shields.io/github/v/release/NeoApplications/Neo-Feed?style=flat&labelColor=DDBBFF&color=6650A4)](https://github.com/NeoApplications/Neo-Feed/releases/latest)

</div>

<div align="center">

[<img src="get_iod.svg" alt="Get it on IzzyOnDroid" width="24%" align="center">](https://apt.izzysoft.de/fdroid/index/apk/com.saulhdev.neofeed)
[<img src="get_codeberg.svg" alt="Get it on Codeberg" width="24%" align="center">](https://codeberg.org/NeoApplications/Neo-Feed/releases)
[<img src="get_github.svg" alt="Get it on GitHub" width="24%" align="center">](https://github.com/NeoApplications/Neo-Feed/releases)

</div>

[![Neo Applications Banner](neo_banner.png)](https://github.com/NeoApplications)

## About this fork :sparkles:

This is a community-maintained, vibe-coded modification of [Neo Feed](https://github.com/NeoApplications/Neo-Feed). It keeps the original feed-replacement functionality and adds a number of fixes and features that have not yet landed upstream:

- **Keyboard fix** — Resolves the long-standing bug where the on-screen keyboard would not appear in a launcher's app drawer while Neo-Feed was enabled ([upstream issue #56](https://github.com/NeoApplications/Neo-Feed/issues/56)).
- **Folder crash fix for Neo-Launcher** — Applies the matching folder-crash fix to [Neo-Launcher](https://github.com/NeoApplications/Neo-Launcher) so app folders open reliably again.
- **Integrated community improvements** — Pulls in useful changes developed by other contributors around Neo-Feed.
- **Expanded suggested feeds** — Adds new suggested-feed catalogues, including **Scandinavian**, **German**, and more **Spanish-language** sources.
- **Mastodon account source** — Lets you add a Mastodon home timeline as a feed source. For each Mastodon source you can configure:
  - **Must contain a link** — only show posts that include at least one URL.
  - **Must contain a picture** — only show posts that include an image.
  - **Hide replies** — hide replies and show only original posts.
- **Global blocked words** — Adds a global word filter so you can hide articles containing terms you are not interested in.

Debug test builds of both **Neo-Feed** and **Neo-Launcher** are published on the [Releases](https://github.com/johanneswilm/Neo-Feed/releases) page.

## Screenshots :framed_picture:

### Neo Launcher integration, local reader and feeds customization

| <img title="" src="fastlane/metadata/android/en-US/images/phoneScreenshots/01.png" alt="" width="330" align="center"> | <img title="" src="fastlane/metadata/android/en-US/images/phoneScreenshots/02.png" alt="" width="330" align="center"> | <img title="" src="fastlane/metadata/android/en-US/images/phoneScreenshots/03.png" alt="" width="330" align="center"> |
|:---------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------:|

### Supports wide screens like tablets and TVs

| <img title="" src="fastlane/metadata/android/en-US/images/phoneScreenshots/04.png" alt="" width="1000" align="center"> |
|:----------------------------------------------------------------------------------------------------------------------:|

### Supported launchers

Any launcher with custom feed provider support. For example:

- Neo Launcher
- LawnChair
- Shade Launcher

## Community :speech_balloon:

You can join either our [Telegram](https://t.me/neo_launcher) or [Matrix](https://matrix.to/#/#neo-launcher:matrix.org) groups to make suggestions, ask questions, receive news, install test builds, or just chat.

<p align="center">
<a href="https://t.me/neo_launcher"><img src="https://upload.wikimedia.org/wikipedia/commons/8/82/Telegram_logo.svg" alt="Join Telegram Channel" width="11%" align="center"></a>
<a href="https://matrix.to/#/#neo-launcher:matrix.org"><img src="https://docs.cloudron.io/img/element-logo.png" alt="Join Matrix Channel" width="11%" align="center" /></a>
</p>

## Translation :left_speech_bubble: 
[<img align="right" src="https://hosted.weblate.org/widgets/neo-feed/-/287x66-white.png" alt="Translation stats" width="40%" />](https://hosted.weblate.org/engage/neo-feed/?utm_source=widget)

Contribute your translations to Neo Feed on [Hosted Weblate](https://hosted.weblate.org/engage/neo-feed/). <br> Adding new languages is always accepted and supported.

[![Translation stats](https://hosted.weblate.org/widgets/neo-feed/-/multi-auto.svg)](https://hosted.weblate.org/engage/neo-feed/?utm_source=widget)

## Special Thanks :heart:

[iTaysonLab](https://github.com/iTaysonLab) as the project is a fork of his HomeFeeder.

[DrawerOverlayService](https://github.com/FabianTerhorst/DrawerOverlayService) as base for overlay service.

[Helena Zheng](https://helenazhang.com/) & [Tobias Fried](https://tobiasfried.com/) for the great [Phosphor Icons](https://phosphoricons.com/), we gladly use.

### Contributors :handshake:

<a href="https://github.com/NeoApplications/Neo-Feed/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=NeoApplications/Neo-Feed"  alt="Icons of contributors to Neo Store"/>
</a>

## Copylefted Libre License :scroll:

Licensed under the [GPLv3+](/LICENSE).

Copyright © 2025 [Saul Henriquez](https://github.com/machiav3lli) & [Antonios Hazim](https://github.com/machiav3lli)

![Star History Chart](https://api.star-history.com/svg?repos=NeoApplications/Neo-Feed&type=Date)
