# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Focus Launcher is an Android launcher application designed to minimize distractions and help users regain control of their phone usage. The app counteracts addictive design patterns by providing features like greyscaled icons, intention reminders, app timers, and unbranded icons.

**Package ID**: `io.focuslauncher`
**Tech Stack**: Java + Kotlin hybrid, Android SDK 21-30, Gradle build system

## Build System

The actual Android project is located in the `MiniumApps/` subdirectory, not the repository root.

### Build Commands

All Gradle commands must be run from the `MiniumApps/` directory:

```bash
cd MiniumApps
./gradlew <command>
```

**Common build commands:**

- **Build debug APK**: `./gradlew assembleDebug`
- **Build alpha release APK**: `./gradlew :launcher3:assembleAlphaRelease`
- **Build beta release APK**: `./gradlew :launcher3:assembleBetaRelease`
- **Run lint checks**: `./gradlew lint -p launcher3/`
- **Clean build**: `./gradlew clean`

**Output locations:**
- Debug APK: `MiniumApps/launcher3/build/outputs/apk/alpha/debug/launcher3-alpha-debug.apk`
- Alpha release APK: `MiniumApps/launcher3/build/outputs/apk/alpha/release/launcher3-alpha-release-unsigned.apk`
- Beta release APK: `MiniumApps/launcher3/build/outputs/apk/beta/release/launcher3-beta-release-unsigned.apk`

### Product Flavors

The app has two product flavors:
- **alpha**: Version 1.0.2 (for testing)
- **beta**: Version 1.0.7 (more stable)

## Project Structure

```
MiniumApps/
├── launcher3/          # Main application module
│   └── src/main/
│       ├── java/io/focuslauncher/phone/
│       │   ├── activities/      # Activity classes
│       │   ├── adapters/        # RecyclerView and ViewPager adapters
│       │   ├── app/             # Application class, configuration, preferences
│       │   ├── customviews/     # Custom UI components
│       │   ├── db/              # GreenDAO database layer
│       │   ├── event/           # EventBus event classes
│       │   ├── fragments/       # Fragment classes
│       │   ├── helper/          # Helper utilities (Firebase, etc.)
│       │   ├── launcher/        # Core launcher functionality
│       │   ├── main/            # Main screen logic
│       │   ├── managers/        # Business logic managers
│       │   ├── models/          # Data models
│       │   ├── receivers/       # Broadcast receivers
│       │   ├── screenfilter/    # Greyscale filter logic
│       │   ├── service/         # Background services
│       │   └── utils/           # Utility classes
│       └── res/                 # Resources (layouts, drawables, values)
└── chips/              # Chip UI library module
```

## Key Architecture Components

### Application Entry Point
- **Launcher3App** (`app/Launcher3App.java`): Main application class extending CoreApplication
  - Initializes GreenDAO database
  - Configures Firebase Analytics
  - Sets up Evernote SDK integration
  - Tracks app lifecycle (foreground/background state)
  - Uses AndroidAnnotations (@EApplication)

### Database
- **ORM**: GreenDAO 3.x
- **Schema version**: 4 (defined in root `build.gradle`)
- **Database name**: `noti-db`
- **Session access**: `((Launcher3App) getApplication()).getDaoSession()`
- Database helper: `GreenDaoOpenHelper` with schema migration support

### Key Libraries
- **AndroidAnnotations**: Used throughout for dependency injection and boilerplate reduction
- **EventBus (GreenRobot)**: Event-driven communication between components
- **Firebase**: Analytics, Crashlytics, Remote Config, Cloud Messaging
- **Glide**: Image loading
- **ButterKnife**: View binding
- **GreenDAO**: Object-relational mapping
- **Material Components**: UI design

### Configuration & Preferences
- **Launcher3Prefs**: Launcher-specific preferences
- **PrefSiempo**: Shared preferences wrapper
- **Config**: Build configuration constants
- **BuildConfig fields**: `GIT_SHA`, `BUILD_TIME`, `FLAVOR`, `BUILD_TYPE`

### Main Features Architecture
- **Pane System**: Uses ViewPager with multiple panes (Favorites, Tools, JunkFood)
- **Tempo Feature**: App usage time limits with notification management
- **Intention System**: User-defined intentions displayed on home screen
- **Greyscale Filter**: Screen filter overlay service
- **App Categorization**: Favorite apps vs "junk food" apps with different access patterns

## Development Notes

### Java Version
The project requires **JDK 8** (as configured in GitHub Actions workflow).

### AndroidAnnotations
Many classes use AndroidAnnotations processor:
- `@EApplication`, `@EActivity`, `@EFragment` for enhanced components
- `@SystemService` for injecting system services
- Build uses annotation processor (kapt for Kotlin files)

### Mixed Java/Kotlin Codebase
- Core application logic is primarily Java
- Newer features and activities are written in Kotlin
- ViewBinding is enabled for modern view access patterns

### Firebase Integration
- `google-services.json` is located at `MiniumApps/launcher3/google-services.json`
- Analytics userId is set to device ID on app startup
- FirebaseHelper provides centralized analytics logging

### ProGuard
- Release builds use minification and resource shrinking
- ProGuard rules: `launcher3/proguard-rules.pro`

## CI/CD

### GitHub Actions
**Release workflow** (`.github/workflows/release.yml`):
- Triggers on version tags (v*) or manual dispatch
- Builds both alpha and beta APKs
- Creates GitHub release with APK artifacts
- Requires JDK 8

### GitLab CI
**Pipeline** (`.gitlab-ci.yml`):
- Lint job: Runs lint checks on launcher3 module
- Build job: Assembles debug APK (alpha flavor)

## Common Development Workflows

### Adding a New Feature
1. Determine if it's an Activity, Fragment, or Service
2. Follow existing patterns (use AndroidAnnotations if applicable)
3. For activities: Extend CoreActivity or use appropriate base class
4. For database: Define entities and regenerate DAOs
5. For preferences: Add keys to appropriate Prefs class
6. Use EventBus for cross-component communication

### Testing
No automated test suite is currently configured, but the structure is in place:
- Test runner: `androidx.test.runner.AndroidJUnitRunner`
- Add tests to `launcher3/src/androidTest/` for instrumented tests
- JUnit version 4.12 is configured

### Modifying Database Schema
1. Update entity classes with GreenDAO annotations
2. Increment `databaseVersion` in root `build.gradle`
3. Update `GreenDaoOpenHelper` migration logic if needed
4. GreenDAO will regenerate DAO classes during build

## Version Management

Version information is split:
- **Version code**: Set in `launcher3/build.gradle` (`versionCode 7`)
- **Version name**: Per flavor in `productFlavors` block
- **Build metadata**: Generated from git SHA and build timestamp
