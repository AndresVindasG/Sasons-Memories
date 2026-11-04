@echo off
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0ejecutar.ps1" %*
exit /b %ERRORLEVEL%
