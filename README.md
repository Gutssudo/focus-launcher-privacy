# Focus Launcher - Privacy Edition

A privacy-focused Android launcher designed to minimize distractions and help users regain control of their phone usage. This fork has been completely cleaned of all tracking, analytics, and cloud dependencies.

## Privacy-First Philosophy

This version of Focus Launcher has been thoroughly audited and modified to respect your privacy:

### Removed Privacy-Invasive Components

- ✅ **Firebase Analytics** - No usage tracking
- ✅ **Firebase Crashlytics** - No crash reports sent to Google
- ✅ **Firebase Cloud Messaging** - No push notifications from remote servers
- ✅ **Firebase Remote Config** - No remote configuration changes
- ✅ **Firebase Realtime Database** - No data synced to cloud
- ✅ **Evernote SDK** - No third-party note syncing
- ✅ **MailChimp Integration** - No email list tracking
- ✅ **Background Usage Tracking** - No app monitoring libraries
- ✅ **Location Tracking** - No location data collection or upload

**Your data stays on your device. Period.**

## Features

Focus Launcher helps you build healthier phone habits through:

- 🎨 **Greyscale Mode** - Reduce visual stimulation with monochrome UI
- ⏱️ **App Timers** - Set usage limits for distracting apps
- 🎯 **Intention Setting** - Define your purpose before using apps
- 📱 **Minimal Design** - Unbranded icons and distraction-free interface
- 🔒 **Privacy Focused** - No tracking, no analytics, no cloud sync
- 📴 **Offline First** - All features work without internet connection

## Compatibility

- **Minimum SDK**: Android 5.0 (API 21)
- **Target SDK**: Android 14 (API 34)
- **Tested on**: Android 5.0 - Android 14

## Installation

### From Source

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/focus-launcher-privacy.git
   cd focus-launcher-privacy
   ```

2. Set up Android SDK path:
   ```bash
   echo "sdk.dir=$HOME/Android/Sdk" > MiniumApps/local.properties
   ```

3. Build the APK:
   ```bash
   cd MiniumApps
   ./gradlew assembleAlphaDebug
   ```

4. Install on your device:
   ```bash
   adb install launcher3/build/outputs/apk/alpha/debug/launcher3-alpha-debug.apk
   ```

### Pre-built APKs

Download the latest privacy-focused release from the [Releases](https://github.com/yourusername/focus-launcher-privacy/releases) page.

## Development

### Prerequisites

- **JDK 17** (OpenJDK recommended)
- **Android SDK** with API level 34
- **Gradle 8.5** (included via wrapper)

### Build Commands

All commands must be run from the `MiniumApps/` directory:

```bash
cd MiniumApps

# Build debug APK
./gradlew assembleAlphaDebug

# Build release APK
./gradlew assembleAlphaRelease

# Run lint checks
./gradlew lint

# Clean build
./gradlew clean
```

### Project Structure

```
MiniumApps/
├── launcher3/          # Main application module
│   └── src/main/
│       ├── java/io/focuslauncher/phone/
│       │   ├── activities/      # Activity classes
│       │   ├── fragments/       # Fragment classes
│       │   ├── db/              # GreenDAO database layer
│       │   ├── helper/          # Helper utilities (stubbed for privacy)
│       │   ├── managers/        # Business logic managers
│       │   └── service/         # Background services
│       └── res/                 # Resources (layouts, drawables, values)
└── chips/              # Chip UI library module
```

### Technology Stack

- **Language**: Java + Kotlin (hybrid)
- **Build System**: Gradle 8.5, Android Gradle Plugin 8.2.2
- **Database**: GreenDAO 3.3.0 (local SQLite ORM)
- **UI Framework**: AndroidAnnotations, Material Components
- **Image Loading**: Glide
- **Event Bus**: GreenRobot EventBus

## Privacy Modifications

### How Privacy Was Achieved

All tracking and analytics code has been replaced with no-op stubs that maintain API compatibility without collecting data:

1. **Firebase Services** - All Firebase methods now return immediately without network calls
2. **Analytics Events** - All analytics logging is stubbed to do nothing
3. **Cloud Sync** - Remote database operations are disabled
4. **Third-party SDKs** - Evernote, MailChimp, and tracking libraries completely removed
5. **Permissions** - Reduced to only essential Android permissions

### Code Verification

Key files demonstrating privacy commitment:

- `launcher3/build.gradle` - No Firebase or tracking dependencies
- `launcher3/src/main/java/io/focuslauncher/phone/helper/FirebaseHelper.java` - Stubbed analytics
- `launcher3/src/main/java/io/focuslauncher/phone/app/CoreApplication.java` - No network initialization
- `launcher3/src/main/AndroidManifest.xml` - Minimal permissions

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for development guidelines and how to submit improvements.

## Product Flavors

The app has two build variants:

- **alpha** - Version 1.0.2 (testing/development)
- **beta** - Version 1.0.7 (more stable)

## License

[Original license information to be added]

## Acknowledgments

This privacy-focused fork was created with significant assistance from [Claude Code](https://claude.ai/code), Anthropic's AI-powered development tool. The privacy audit, dependency removal, and Android 14 compatibility updates were performed collaboratively with AI assistance.

**Original Focus Launcher** - Thank you to the original developers for creating a thoughtful, distraction-minimizing launcher.

## Disclaimer

This is an independent fork focused on privacy. Use at your own risk. The app is provided as-is without warranty.

---

**Built with privacy in mind. Your data belongs to you.**
