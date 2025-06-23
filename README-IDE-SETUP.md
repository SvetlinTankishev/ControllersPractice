# IDE Setup for Test Consistency

## Problem
Tests were failing in Cursor IDE with "expecting 200 but was 400" errors while passing in IntelliJ and command line. This was caused by Java version inconsistencies between different IDEs.

## Root Cause
- **Cursor IDE** was using Java 21.0.7 (bundled extension version)
- **SDKMAN/Terminal** was using Java 21.0.3 (configured version)
- **IntelliJ** was using its own Java configuration

This created JVM incompatibility issues where Gradle daemons started with one Java version couldn't work with another.

## Solution Applied

### 1. Updated `.vscode/settings.json`
Added explicit Java configuration:
```json
{
    "java.configuration.updateBuildConfiguration": "automatic",
    "java.compile.nullAnalysis.mode": "automatic", 
    "java.debug.settings.onBuildFailureProceed": true,
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-21",
            "path": "/Users/svetlintankishev/.sdkman/candidates/java/21.0.3-tem"
        }
    ],
    "java.home": "/Users/svetlintankishev/.sdkman/candidates/java/21.0.3-tem"
}
```

### 2. Created `gradle.properties`
Ensures Gradle always uses the same Java version:
```properties
org.gradle.java.home=/Users/svetlintankishev/.sdkman/candidates/java/21.0.3-tem
org.gradle.jvmargs=-XX:MaxMetaspaceSize=384m -XX:+HeapDumpOnOutOfMemoryError -Xms256m -Xmx512m -Dfile.encoding=UTF-8
```

### 3. Created `setup-dev-env.sh`
Provides a script to reset the development environment when needed.

## Steps to Fix in Cursor IDE

1. **Run the setup script:**
   ```bash
   ./setup-dev-env.sh
   ```

2. **Restart Cursor IDE completely** (not just reload window)

3. **In Cursor IDE:**
   - Open Command Palette (`Cmd+Shift+P`)
   - Run `Java: Reload Projects`
   - Run `Java: Restart Language Server`

4. **Verify the fix:**
   - Run any test in Cursor IDE
   - All tests should now pass consistently

## For IntelliJ Users
IntelliJ should continue working as before. If issues occur, ensure IntelliJ is configured to use the same Java version (`/Users/svetlintankishev/.sdkman/candidates/java/21.0.3-tem`).

## Prevention
- Always use the setup script when switching between development environments
- Keep Java versions consistent across all IDEs
- If adding new team members, ensure they use the same Java version 