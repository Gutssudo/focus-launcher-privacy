# Contributing to Focus Launcher Private

Thank you for considering contributing! Here's how you can help.

## Code of Conduct

- Be respectful and inclusive
- Focus on privacy and security
- Write clean, documented code

## 🔧 Building from Source

### Prerequisites

- **Java Development Kit (JDK) 17** or higher
- **Android Studio** (latest stable version recommended) OR Android SDK Command Line Tools
- **Git**

### Clone the Repository

```bash
git clone https://github.com/Gutssudo/focus-launcher-privacy.git
cd focus-launcher-privacy
```

### Build Instructions

#### Option 1: Using Android Studio

1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to the cloned repository folder
4. Wait for Gradle sync to complete
5. Click **Build > Build Bundle(s) / APK(s) > Build APK(s)**
6. The APK will be generated in `MiniumApps/launcher3/build/outputs/apk/`

#### Option 2: Using Command Line (Gradle)

```bash
# Navigate to the MiniumApps directory
cd MiniumApps

# Make gradlew executable (Linux/Mac)
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# Build release APK (requires signing configuration)
./gradlew assembleRelease

# Clean build
./gradlew clean assembleRelease
```

**On Windows:**
```cmd
cd MiniumApps
gradlew.bat assembleDebug
```

### Build Outputs

- **Debug APK**: `MiniumApps/launcher3/build/outputs/apk/debug/launcher3-debug.apk`
- **Release APK**: `MiniumApps/launcher3/build/outputs/apk/release/launcher3-release.apk`

### Signing Configuration (for Release builds)

To build a signed release APK:

1. Create a keystore file:
```bash
keytool -genkey -v -keystore focus-launcher.keystore -alias focus_launcher -keyalg RSA -keysize 2048 -validity 10000
```

2. Create `keystore.properties` in the `MiniumApps` folder:
```properties
storePassword=YOUR_KEYSTORE_PASSWORD
keyPassword=YOUR_KEY_PASSWORD
keyAlias=focus_launcher
storeFile=../focus-launcher.keystore
```

3. Build signed release:
```bash
./gradlew assembleRelease
```

### Running Tests

```bash
# Run unit tests
./gradlew test

# Run instrumentation tests (requires connected device or emulator)
./gradlew connectedAndroidTest

# Check for privacy violations
./verify_no_tracking.sh
```

### Verify Build

```bash
# Check APK permissions
aapt dump permissions launcher3-release.apk

# Check APK size
ls -lh launcher3-release.apk

# Install on connected device
adb install -r launcher3-debug.apk
```

## 🤝 How to Contribute

### Reporting Issues

1. Check existing issues first
2. Use the issue template
3. Provide steps to reproduce
4. Include Android version and device info
5. Attach logs if applicable:
```bash
adb logcat | grep FocusLauncher
```

### Submitting Pull Requests

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Follow our privacy principles:
   - ❌ No tracking libraries
   - ❌ No external data transmission
   - ❌ No unnecessary permissions
   - ✅ All data stays local
4. Write tests for new features
5. Update documentation
6. Commit with clear messages (`git commit -m 'Add amazing feature'`)
7. Push to your fork (`git push origin feature/amazing-feature`)
8. Open a Pull Request

### Code Style

- Use **Kotlin** for new code (preferred)
- Use **Java 17** features where appropriate
- Follow [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)
- Comment complex logic
- Use meaningful variable names
- Keep methods small and focused

### Privacy Review Checklist

Before submitting, ensure:
- [ ] No new tracking libraries added (Firebase, Sentry, etc.)
- [ ] No new unnecessary permissions
- [ ] All data stored locally only
- [ ] No network calls without explicit user initiation
- [ ] Privacy policy updated if needed
- [ ] Permissions documented in `docs/PERMISSIONS.md`
- [ ] `verify_no_tracking.sh` script passes

### Testing Requirements

- [ ] Unit tests added for new utility functions
- [ ] UI tests added for new screens
- [ ] Tested on Android 14/15 (latest)
- [ ] Tested on Android 5-8 (legacy support)
- [ ] No memory leaks detected
- [ ] No crashes in production scenarios

## 📋 TODO List Priority

The complete modernization TODO list is available in this document. Here's the priority order:

### 🔴 CRITICAL (Must do first)
1. **Phase 1.1**: Remove all trackers (Firebase, Sentry, MailChimp)
2. **Phase 1.2**: Audit and reduce permissions
3. **Phase 1.3**: Create privacy policy
4. **Phase 2.1**: Update SDK versions to Android 14/15

### 🟠 IMPORTANT (Required for Play Store)
5. **Phase 2.2**: Update AndroidManifest for Android 14/15 compatibility
6. **Phase 2.3**: Migrate deprecated code (AsyncTask, onBackPressed, etc.)
7. **Phase 2.4**: Implement modern foreground services

### 🟡 RECOMMENDED (Quality improvements)
8. **Phase 3.1**: Add unit tests
9. **Phase 3.2**: Manual validation on real devices
10. **Phase 4.1**: Update documentation

### 🟢 BONUS (Future enhancements)
- Advanced privacy features
- Additional customization options
- Performance optimizations

## 🛠️ Development Environment

### Recommended IDE Settings

**Android Studio Settings:**
- Enable Kotlin auto-formatting
- Set Java compatibility to JDK 17
- Enable Gradle offline mode for faster builds
- Configure ProGuard for release builds

### Gradle Configuration

The project uses:
- **Gradle**: 8.2.2
- **Android Gradle Plugin**: 8.2.2
- **Kotlin**: 1.9.22
- **compileSdk**: 35 (Android 15)
- **targetSdk**: 34 (Android 14)
- **minSdk**: 21 (Android 5.0)

## 📝 Commit Message Guidelines

Use clear, descriptive commit messages:

```
feat: Add dark mode support
fix: Resolve crash on Android 14
docs: Update privacy policy
refactor: Migrate AsyncTask to Coroutines
test: Add unit tests for NotificationHelper
chore: Update dependencies to latest versions
```

## 🔍 Code Review Process

All pull requests require:
1. Passing CI/CD checks (tests, lint)
2. Privacy review (no tracking)
3. Code review by at least one maintainer
4. Documentation updates if needed

## 🐛 Debugging Tips

### Common Build Issues

**Issue: "SDK location not found"**
```bash
# Create local.properties
echo "sdk.dir=/path/to/Android/Sdk" > local.properties
```

**Issue: "Java version mismatch"**
```bash
# Verify Java version
java -version  # Should be 17+

# Set JAVA_HOME
export JAVA_HOME=/path/to/jdk-17
```

**Issue: "Gradle sync failed"**
```bash
# Clear Gradle cache
./gradlew clean
rm -rf .gradle
./gradlew build --refresh-dependencies
```

### Runtime Debugging

```bash
# View real-time logs
adb logcat -s FocusLauncher:V AndroidRuntime:E

# Clear app data
adb shell pm clear io.focuslauncher

# Check app permissions
adb shell dumpsys package io.focuslauncher | grep permission
```

## 📚 Useful Resources

- [Android Developers Documentation](https://developer.android.com/)
- [Android 14 Behavior Changes](https://developer.android.com/about/versions/14/behavior-changes-14)
- [Android Privacy Best Practices](https://developer.android.com/privacy)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)

## 🙋 Questions?

- Open an issue with the `question` label
- Join our discussions on GitHub
- Contact: privacy@focuslauncher.io

## 📜 License

By contributing, you agree that your contributions will be licensed under the same license as the project (MIT License).

---

**Thank you for helping make Focus Launcher the most private, distraction-free launcher! 🚀**
