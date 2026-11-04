param([switch]$CompileOnly)
$ErrorActionPreference = "Stop"
$projectRoot = $PSScriptRoot
$compilerCandidates = [System.Collections.Generic.List[string]]::new()

if ($env:JAVA_HOME) {
    $compilerCandidates.Add((Join-Path $env:JAVA_HOME "bin\javac.exe"))
}
if ($env:JDK_HOME) {
    $compilerCandidates.Add((Join-Path $env:JDK_HOME "bin\javac.exe"))
}
$compilerFromPath = Get-Command javac.exe -ErrorAction SilentlyContinue
if ($compilerFromPath) {
    $compilerCandidates.Add($compilerFromPath.Source)
}

$installRoots = @()
if ($env:ProgramFiles) { $installRoots += Join-Path $env:ProgramFiles "JetBrains" }
if ($env:LOCALAPPDATA) { $installRoots += Join-Path $env:LOCALAPPDATA "Programs" }
foreach ($installRoot in $installRoots) {
    if (Test-Path -LiteralPath $installRoot) {
        Get-ChildItem -Path (Join-Path $installRoot "IntelliJ IDEA*") -Directory -ErrorAction SilentlyContinue |
            ForEach-Object { $compilerCandidates.Add((Join-Path $_.FullName "jbr\bin\javac.exe")) }
    }
}

$compiler = $compilerCandidates | Where-Object { Test-Path -LiteralPath $_ } | Select-Object -First 1
if (-not $compiler) {
    Write-Error "No encontré javac. Configura JAVA_HOME con un JDK y vuelve a ejecutar este archivo."
    exit 1
}
$runtime = Join-Path (Split-Path -Parent $compiler) "java.exe"
if (-not (Test-Path -LiteralPath $runtime)) {
    Write-Error "No encontré java.exe junto a javac.exe."
    exit 1
}

$sourceFiles = Get-ChildItem -Path (Join-Path $projectRoot "src") -Recurse -File -Filter "*.java" |
    ForEach-Object { $_.FullName }
if (-not $sourceFiles) {
    Write-Error "No encontré archivos Java dentro de src."
    exit 1
}

$classOutput = Join-Path $projectRoot "out\ejecucion"
New-Item -ItemType Directory -Force -Path $classOutput | Out-Null
$classpath = "$classOutput;$(Join-Path $projectRoot 'lib\*');$(Join-Path $projectRoot 'resources')"
$compilerArguments = @("-encoding", "UTF-8", "-cp", $classpath, "-d", $classOutput) + $sourceFiles
Write-Host "Compilando con $compiler ..."
& $compiler @compilerArguments
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Push-Location $projectRoot
try {
    if ($CompileOnly) { Write-Host "Compilación terminada."; exit 0 }
Write-Host "Abriendo Memory Game..."
    & $runtime --enable-native-access=ALL-UNNAMED -cp $classpath Main
    exit $LASTEXITCODE
} finally {
    Pop-Location
}