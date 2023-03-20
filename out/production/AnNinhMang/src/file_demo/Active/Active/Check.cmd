chcp 65001 >nul
@echo off
title CRIPT KIEM TRA BAN QUYEN WINDDOWS OFFICE
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
echo.Supporter: Minh Thuan
echo.		 ______________________________________________________________
echo.		^|	Kiem tra ban quyen Windows:		Nhap phim A   ^|
echo.		^|	Kiem tra ban quyen Office:		Nhap phim B   ^|
echo.		^|	Kiem tra ban quyen Windows+Office:	Nhap phim C   ^|
echo.		^|	Thoat:					Nhap phim D   ^|
echo.		 -------------------------------------------------------------
@echo ===========================
Choice /N /C ABCDEF /M "* Nhap Lua Chon Cua Ban : 
if ERRORLEVEL 6 goto :5 F
if ERRORLEVEL 5 goto :4 E
if ERRORLEVEL 4 goto :3 D
if ERRORLEVEL 3 goto :2 C
if ERRORLEVEL 2 goto :1 B
if ERRORLEVEL 1 goto :0 A

:0
@echo.
@echo ***********************************************************
@echo 			Kiem tra ban quyen Windows
@echo ***********************************************************
cd %windir%/system32
cscript slmgr.vbs /xpr
cscript slmgr.vbs /xpr |findstr "permanently" >nul
if %errorlevel%==0 (
@echo    	 ===	 Windows da duoc kich hoat ban quyen Vinh Vien	 ===
)else (
@echo		====	Windows chua duoc kich hoat vinh vien	====
)
@echo -----------------------------------------------------------
@echo.
@echo Nhan phim bat ky de tiep tuc.......
pause>nul
cls
goto main
)
:1
@echo.
@echo ***********************************************************
@echo 			Kiem tra ban quyen Office
@echo ************************************************************ 
if exist "%ProgramFiles%\Microsoft Office\Office14\ospp.vbs" (cd /d "%ProgramFiles%\Microsoft Office\Office14")
if exist "%ProgramFiles%\Microsoft Office\Office15\ospp.vbs" (cd /d "%ProgramFiles%\Microsoft Office\Office15")
if exist "%ProgramFiles%\Microsoft Office\Office16\ospp.vbs" (cd /d "%ProgramFiles%\Microsoft Office\Office16")
cscript ospp.vbs /dstatus
cscript ospp.vbs /dstatus |findstr "minute(s) before expiring" >null
if %errorlevel%==0 (
@echo 		====	Office khong duoc kich hoat vinh vien	====
)else (
goto :check
)
@echo -----------------------------------------------------------
@echo.
@echo Nhan phim bat ky de tiep tuc.......
pause>nul
cls
goto main
)

:check
if exist "%ProgramFiles%\Microsoft Office\Office14\ospp.vbs" (cd /d "%ProgramFiles%\Microsoft Office\Office14")
if exist "%ProgramFiles%\Microsoft Office\Office15\ospp.vbs" (cd /d "%ProgramFiles%\Microsoft Office\Office15")
if exist "%ProgramFiles%\Microsoft Office\Office16\ospp.vbs" (cd /d "%ProgramFiles%\Microsoft Office\Office16")
cscript ospp.vbs /dstatus |findstr "LICENSED" >null
if %errorlevel%==0 (
@echo 		=====	Office da duoc kich hoat ban quyen VINH VIEN	=====
)else (
@echo 		=====	Khong tim thay Office tren thiet bi	=====	
)
@echo -----------------------------------------------------------
@echo.
@echo Nhan phim bat ky de tiep tuc.......
pause>nul
cls
goto main
)




:2
@echo.
@echo ***********************************************************
@echo 			Kiem tra ban quyen Windows
@echo ***********************************************************
cd %windir%/system32
cscript slmgr.vbs /xpr
cscript slmgr.vbs /xpr |findstr "permanently" >nul
if %errorlevel%==0 (
@echo    	 ===	 Windows da duoc kich hoat ban quyen Vinh Vien	 ===
)else (
@echo		====	Windows chua duoc kich hoat vinh vien	====
)
@echo -----------------------------------------------------------
@echo.
goto :1
)
:3
exit
)