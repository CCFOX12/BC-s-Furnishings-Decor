@echo off
setlocal EnableExtensions
REM Directory name contains "&". Quoted APP_HOME so cmd does not split the path.
set "APP_HOME=%~dp0"
if "%APP_HOME:~-1%"=="\" set "APP_HOME=%APP_HOME:~0,-1%"

if defined JAVA_HOME (
	set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
	set "JAVA_EXE=java.exe"
)

REM Loom 1.19 needs JDK 25 to start Gradle. Prefer the path in gradle.properties.
set "GRADLE_JVM=C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
if exist "%GRADLE_JVM%" set "JAVA_EXE=%GRADLE_JVM%"

"%JAVA_EXE%" -Xmx64m -Xms64m "-Dorg.gradle.appname=gradlew" -jar "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" %*
exit /b %ERRORLEVEL%
