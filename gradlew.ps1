# 本仓库目录名含 &，cmd 版 gradlew.bat 会把路径截断。PowerShell 用这个脚本。
$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$javaHome = $null
$props = Join-Path $root 'gradle.properties'
if (Test-Path -LiteralPath $props) {
	Get-Content -LiteralPath $props | ForEach-Object {
		if ($_ -match '^\s*org\.gradle\.java\.home=(.*)$') {
			$javaHome = $Matches[1].Trim().Replace('\\', '\')
		}
	}
}
if (-not $javaHome) { $javaHome = $env:JAVA_HOME }
if (-not $javaHome) {
	$java = Get-Command java.exe -ErrorAction Stop
	$javaExe = $java.Source
} else {
	$javaExe = Join-Path $javaHome 'bin\java.exe'
}
$wrapper = Join-Path $root 'gradle\wrapper\gradle-wrapper.jar'
Set-Location -LiteralPath $root
& $javaExe '-Xmx64m' '-Xms64m' '-Dorg.gradle.appname=gradlew' -jar $wrapper @args
exit $LASTEXITCODE
