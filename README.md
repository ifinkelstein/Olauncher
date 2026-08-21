![Olauncher](https://repository-images.githubusercontent.com/278638069/db0acb80-661b-11eb-803e-926cae5dccb4)


# Olauncher | Minimal AF Launcher
AF stands for Ad-Free! :D

## About this fork

This fork ([ifinkelstein/Olauncher](https://github.com/ifinkelstein/Olauncher)) adds home screen and digital wellbeing features on top of upstream Olauncher.

### Home screen

- Up to 12 apps on the home screen (upstream allows 8).
- Sort home screen apps A–Z (toggle in settings).
- Per-app usage time for today shown next to each home screen app (requires usage access permission).
- Per-app daily open counts shown next to each home screen app.
- "Now row" under the clock: shows either your next calendar event today (requires calendar permission) or current weather from Open-Meteo (requires location permission; cached 30 min, no API key). Tapping it opens a configurable app. Cycles Off → Calendar → Weather in settings.
- Adjustable app spacing: Default (density-based) or 0/2/4/6/8/12/16 dp.
- "Apps to bottom" option to extend the home app list toward the bottom of the screen.

### Wellbeing

Grouped in their own "Wellbeing" settings section:

- Today's unlock count shown under the date.
- Mindful pause: a countdown (3, 5, or 10 seconds, or off) before flagged apps open, with a cancel option. Flag apps via the "Mindful apps" selector.
- Daily time budgets per app: cycle 15/30/60/90 minutes per app; when today's usage exceeds the budget, launching shows a warning with an "Open anyway" escape hatch.
- System grayscale toggle (requires `adb shell pm grant app.olauncher android.permission.WRITE_SECURE_SETTINGS`).

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
    alt="Get it on F-Droid"
    height="80">](https://f-droid.org/packages/app.olauncher)
[<img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png"
    alt="Get it on Play Store"
    height="80">](https://play.google.com/store/apps/details?id=app.olauncher)

### Install using [F-Droid](https://f-droid.org/packages/app.olauncher), [Play Store](https://play.google.com/store/apps/details?id=app.olauncher) or the [latest APK](https://github.com/tanujnotes/Olauncher/releases/).

- To maintain the simplicity of the launcher, a few niche features are available but hidden.

- Please check out the **[About](https://tanujnotes.substack.com/p/olauncher-minimal-af-launcher?utm_source=github)** page in the Olauncher settings for a complete list of features and **FAQs**.

##

License: [GNU GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html)

Contact: [X/Twitter](https://x.com/tanujnotes) • [Reddit](https://reddit.com/user/tanujnotes/) • [Bluesky](https://bsky.app/profile/tanujnotes.bsky.social)

##

### My other apps:

- [Pro Launcher](https://play.google.com/store/apps/details?id=app.prolauncher) - Pro version of Olauncher with extra features like widgets, weather, folders, etc.

- [Note to Self](https://play.google.com/store/apps/details?id=com.makenotetoself) - Free and [open source](https://github.com/jeerovan/ntsapp) notes app with chat like interface and end-to-end encryption.

- [Pentastic](https://play.google.com/store/apps/details?id=app.pentastic) - Minimal todo lists. Free and [open source](https://github.com/tanujnotes/Pentastic).

##

### Help me get a new phone for testing:

[<img src="https://img.buymeacoffee.com/button-api/?emoji=&slug=tanujnotes&button_colour=FFDD00&font_colour=000000&font_family=Cookie&outline_colour=000000&coffee_colour=ffffff"
    alt="Get it on Play Store"
    height="80">](https://www.buymeacoffee.com/tanujnotes)

Thank you!
