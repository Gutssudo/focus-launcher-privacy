# Contributing to Focus Launcher - Privacy Edition

Thank you for your interest in contributing to Focus Launcher! This guide will help you get started with development.

## Code of Conduct

### Privacy-First Development

When contributing to this project, you must adhere to our core privacy principles:

- ❌ **Never** add analytics, tracking, or telemetry code
- ❌ **Never** introduce dependencies that collect user data
- ❌ **Never** add network calls that send user information to remote servers
- ❌ **Never** request permissions that aren't strictly necessary for core functionality
- ✅ **Always** ensure data stays local to the device
- ✅ **Always** document any new dependencies and their privacy implications
- ✅ **Always** prefer offline-first solutions

## Getting Started

### Development Environment Setup

1. **Install Java Development Kit**
   ```bash
   # Ubuntu/Debian
   sudo apt install openjdk-17-jdk
   
   # macOS (using Homebrew)
   brew install openjdk@17
   
   # Verify installation
   java -version  # Should show version 17
   ```

2. **Install Android SDK**
   - Download Android Studio from https://developer.android.com/studio
   - Or install command-line tools only
   - Ensure SDK API level 34 is installed

3. **Configure SDK Path**
   ```bash
   # Create local.properties in MiniumApps/ directory
   echo "sdk.dir=/path/to/your/Android/Sdk" > MiniumApps/local.properties
   ```

4. **Clone and Build**
   ```bash
   git clone https://github.com/yourusername/focus-launcher-privacy.git
   cd focus-launcher-privacy/MiniumApps
   ./gradlew assembleAlphaDebug
   ```

### Project Architecture

#### Build System

- **Gradle Version**: 8.5
- **Android Gradle Plugin**: 8.2.2
- **Kotlin Version**: 1.9.22
- **Build Directory**: All Gradle commands run from `MiniumApps/`

#### Key Technologies

- **AndroidAnnotations**: Dependency injection framework used throughout
  - `@EActivity`, `@EFragment`, `@EApplication` for enhanced components
  - `@ViewById` for view binding
  - `@Click` for click handlers

- **GreenDAO 3.3.0**: Local database ORM
  - Schema version: 4
  - Database: `noti-db`
  - Manual DAO files (not auto-generated): `DaoMaster.java`, `DaoSession.java`, `TableNotificationSmsDao.java`

- **EventBus**: Inter-component communication
  - Use for decoupled event-driven architecture
  - Register/unregister in onCreate/onDestroy

### Code Style

#### Java Conventions

- Follow standard Java naming conventions
- Use meaningful variable and method names
- Keep methods focused and small (< 50 lines when possible)
- Add JavaDoc comments for public APIs

#### Kotlin Conventions

- Follow official Kotlin style guide
- Use data classes for simple models
- Prefer `val` over `var` when possible
- Use null-safety features

#### Comments

```java
// ✅ GOOD - Explains why, references privacy
// Privacy: Firebase removed - user data stays local

// ❌ BAD - States the obvious
// Setting the value to true
```

### Making Changes

#### Before You Code

1. **Check existing issues** - See if someone is already working on this
2. **Open an issue** - Describe the change you want to make
3. **Get feedback** - Discuss approach before implementing

#### Development Workflow

1. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make focused commits**
   - One logical change per commit
   - Write clear commit messages
   ```bash
   git commit -m "Remove Firebase Analytics from SettingsActivity
   
   - Stubbed out logEvent() calls
   - No user data is sent to remote servers
   - Maintains existing API for compatibility"
   ```

3. **Test thoroughly**
   - Build both alpha and beta flavors
   - Test on multiple Android versions if possible
   - Verify no network calls are made (use network monitoring tools)

4. **Run lint checks**
   ```bash
   cd MiniumApps
   ./gradlew lint
   ```

### Privacy Review Checklist

Before submitting a pull request, verify:

- [ ] No new network dependencies added
- [ ] No analytics or tracking code introduced
- [ ] No cloud sync or remote storage features
- [ ] No third-party SDKs that collect data
- [ ] Permissions requested are minimal and justified
- [ ] Data storage is local only (no remote databases)
- [ ] Code comments explain privacy-related changes
- [ ] Build succeeds for both alpha and beta flavors

### Submitting Changes

#### Pull Request Process

1. **Update documentation** - If your change affects user-facing features, update README.md

2. **Write a clear PR description**
   ```markdown
   ## Summary
   Brief description of what this PR does
   
   ## Changes
   - Bullet point list of specific changes
   - Include files modified
   
   ## Privacy Impact
   - Explain any privacy implications
   - Note if new permissions are required
   
   ## Testing
   - Describe how you tested the changes
   - Note Android versions tested
   ```

3. **Link related issues**
   - Reference issue numbers in PR description
   - Use keywords: "Fixes #123" or "Closes #456"

4. **Wait for review**
   - Be responsive to feedback
   - Make requested changes promptly

### Build Troubleshooting

#### Common Issues

**Problem**: Build fails with "SDK location not found"
```bash
# Solution: Create local.properties
echo "sdk.dir=$HOME/Android/Sdk" > MiniumApps/local.properties
```

**Problem**: Java version mismatch
```bash
# Solution: Ensure JDK 17 is active
java -version
# Use update-alternatives on Linux or JAVA_HOME on macOS
```

**Problem**: Gradle daemon issues
```bash
# Solution: Stop Gradle daemon and rebuild
cd MiniumApps
./gradlew --stop
./gradlew clean assembleAlphaDebug
```

**Problem**: Out of memory during build
```bash
# Solution: Add to gradle.properties
org.gradle.jvmargs=-Xmx2048m -XX:MaxMetaspaceSize=512m
```

### Testing

#### Manual Testing Checklist

- [ ] App launches successfully
- [ ] Launcher can be set as default home screen
- [ ] App drawer opens and displays all apps
- [ ] Greyscale mode toggles correctly
- [ ] App timers work as expected
- [ ] Intentions can be set and displayed
- [ ] Database operations succeed (favorites, notes, etc.)
- [ ] No crashes on orientation change
- [ ] No memory leaks (use Android Studio Profiler)

#### Privacy Testing

- [ ] Use network monitoring tool (Charles Proxy, mitmproxy) to verify no unexpected network calls
- [ ] Check logcat for any data leakage in logs
- [ ] Review permissions in Settings > Apps > Focus Launcher > Permissions
- [ ] Verify no files written outside app private storage

### Database Changes

If you need to modify the database schema:

1. **Update entity models** in `models/` directory
2. **Increment schema version** in `DaoMaster.java`
   ```java
   public static final int SCHEMA_VERSION = 5; // Increment from 4
   ```
3. **Add migration logic** in `GreenDaoOpenHelper.java`
   ```java
   @Override
   public void onUpgrade(Database db, int oldVersion, int newVersion) {
       if (oldVersion < 5) {
           // Add migration SQL
           db.execSQL("ALTER TABLE ...");
       }
   }
   ```
4. **Update DAO classes** manually to reflect schema changes

### Adding Dependencies

**Think twice before adding any dependency!**

When you must add a new library:

1. **Research thoroughly**
   - Check library's privacy policy
   - Review source code if possible
   - Look for telemetry or analytics code

2. **Prefer libraries that**:
   - Are open source
   - Have no network requirements
   - Don't request unnecessary permissions
   - Have active maintenance

3. **Document in PR**:
   - Why this dependency is needed
   - Privacy analysis of the library
   - Alternatives considered

4. **Add to appropriate `build.gradle`**:
   ```gradle
   dependencies {
       // Privacy: This library has no analytics or tracking
       implementation 'library:name:version'
   }
   ```

### Questions?

- Open an issue for general questions
- Tag issues with `question` label
- Check existing issues for common questions

## Recognition

Contributors who make significant improvements will be acknowledged in the README.md file.

---

**Note**: This project was significantly developed with assistance from [Claude Code](https://claude.ai/code). We welcome both human and AI-assisted contributions, as long as they adhere to our privacy-first principles.

Thank you for helping make Focus Launcher a better, more private Android launcher!
