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
set /p kw=Nhap vao key Windows:
@echo ****************************************************************
@echo 			Dang nap key Windows
@echo ****************************************************************
cd %windir%/system32
cscript slmgr.vbs /ipk %kw%
set /p kms=Nhap vao dia chi server Windows:
@echo ****************************************************************
@echo 			Dang ket noi server Windows
@echo ****************************************************************
cscript slmgr.vbs /skms %kms%
@echo ****************************************************************
@echo 			Dang kich hoat Windows 180 ngay....
@echo ****************************************************************
cscript slmgr.vbs /ato