chcp 65001 >nul
@echo off
title KICH HOAT BAN QUYEN WINDDOWS
mode con: cols=123 lines=35
chcp 65001 >nul
color f1
>nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"
if '%errorlevel%' NEQ '0' (
    echo Chay CMD Voi Quyen Quan tri - Run as Administrator...
    goto goUAC 
) else (
 goto goADMIN )


:goUAC
    echo Set UAC = CreateObject^("Shell.Application"^) > "%temp%\getadmin.vbs"
    set params = %*:"=""
    echo UAC.ShellExecute "cmd.exe", "/c %~s0 %params%", "", "runas", 1 >> "%temp%\getadmin.vbs"
    "%temp%\getadmin.vbs"
    del "%temp%\getadmin.vbs"
    exit /B
:goADMIN
    pushd "%CD%"
    CD /D "%~dp0"
:main
:begin
@echo **********************************************
@echo *		    Tat Windows Update		*
@echo **********************************************
sc config BITS start= disabled
net stop  BITS
sc config wuauserv start= disabled
net stop  wuauserv
@echo **********************************************
@echo *		    Da Tat Windows Update		*
@echo **********************************************
pause>nul