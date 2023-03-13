chcp 65001 >nul
@echo off
title Ho tro kich hoat Windows
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
echo.		^|	Kich hoat Windows all version:		Nhap phim A   ^|
echo.		^|	Fix loi:				Nhap phim B   ^|
echo.		^|	Fix loi step 2:				Nhap phim C   ^|
echo.		^|	Gia han Windows10 Pro 180 ngay:		Nhap phim D   ^|
echo.		^|	Kiem tra ban quyen Windows-Office:	Nhap phim E   ^|
echo.		 -------------------------------------------------------------
@echo ===========================
Choice /N /C ABCDEF /M "* Nhap Lua Chon Cua Ban : 
if ERRORLEVEL 6 goto :5 F
if ERRORLEVEL 5 goto :Check_active E
if ERRORLEVEL 4 goto :Kms D
if ERRORLEVEL 3 goto :Fix2 C
if ERRORLEVEL 2 goto :Fix B
if ERRORLEVEL 1 goto :0 A


:0
@echo *************************************************
@echo 		DANG CAI  DAT LAI GIAY PHEP....
echo **************************************************
@echo.
cd %windir%/system32
cscript slmgr.vbs /rilc |findstr "License files re-installed successfully." >nul
if %errorlevel%==0 (
@echo    	 ===	 Giay phep tap tin duoc cai dat lai thanh cong	 ===
@echo ------------------------------------------------------------------------
)
@echo *************************************************
@echo 		DANG XOA PRODUCT KEY......
@echo *************************************************
cscript slmgr.vbs /upk |findstr "successfully" >null
if %errorlevel%==0 (
@echo    	 ===	 Da xoa key ra khoi windows	 ===
@echo ----------------------------------------------------------
)else (
@echo.
@echo 		Xoa key khong thanh cong!!!!
cscript ospp.vbs /upk
@echo Vui long vao muc fix loi va lam theo huong dan
@echo Nhan phim bat ky de tiep tuc.....................
pause >nul
goto :main

)
)
@echo **********************************************************
@echo 		DANG XOA PRODUCT KEY KHOI REGISTRY....
@echo **********************************************************
@echo.
cscript slmgr.vbs /cpky |findstr "successfully." >nul
if %errorlevel%==0 (
@echo    	 ===	 Da xoa key ra khoi registry	 ===
@echo ------------------------------------------------------------
)

@echo *************************************************
@echo 		DANG XOA SEVER KMS....
@echo *************************************************
@echo.
cscript slmgr.vbs /ckms |findstr "Key Management Service machine name cleared successfully." >nul
if %errorlevel%==0 (
@echo    	 ===	 Da xoa sever KMS	 ===
@echo -------------------------------------------------------
)
@echo.
@echo *************************************************
@echo 		Dang mo cac sever Windows.....
@echo *************************************************
@echo.
sc config Winmgmt start=demand & net start Winmgmt & sc config LicenseManager start= auto & net start LicenseManager & sc config wuauserv start= auto & net start wuauserv
@echo *************************************************
@echo 		Da mo cac sever Windows.
@echo *************************************************

set /p key=Nhap vao Key ban quyen Windows:
cscript slmgr.vbs /ipk %key%
cd %windir%/system32
cscript slmgr.vbs /dti >C:\IID.txt
start C:\IID.txt
set /p cid=Nhap vao CID:
cscript slmgr.vbs /atp %cid% 
cscript slmgr.vbs /ato
cscript slmgr.vbs /xpr |findstr "The machine is permanently activated" >null
if %errorlevel%==0 (
@echo    	 ===	 Da kich hoat Windows VINH VIEN	 ===
)
@echo Nhap phim bat ky de tiep tuc.............
pause >nul
cls
goto :main
)


:Fix
@echo Dang fix loi Windows 10
cd %windir%/system32
cscript slmgr.vbs /upk & cscript slmgr.vbs /ckms & cscript slmgr.vbs /cpky & cscript slmgr.vbs /rearm
net stop sppsvc
ren %windir%\System32\spp\store\2.0\tokens.dat tokens.bar
net start sppsvc
cscript %windir%\system32\slmgr.vbs /rilc
@echo Da fix xong.Vui long khoi dong lai "2 LAN" va chay fix loi step2
@echo Nhan phim bat ky de tiep tuc........
pause >nul
cls
goto :main
)

:Fix2
set /p key2=Nhap vao key mac dinh cua Windows (Chua co key thi vao Link:https://bit.ly/3cRwirJ):
slmgr /ipk %key2%
pause>nul
goto :main
)



:kms
@echo *****************************************
@echo 		Dang go ban quyen...
@echo *****************************************
cd %windir%/system32
cscript slmgr.vbs /upk
cscript slmgr.vbs /cpky
cscript slmgr.vbs /ckms
@echo ***********************************************************************
@echo 		Dang kich hoat ban quyen 180 ngay cho Windows 10 Pro...
@echo ***********************************************************************
cscript slmgr.vbs /ipk W269N-WFGWX-YVC9B-4J6C9-T83GX
cscript slmgr.vbs /skms kms8.msguides.com
cscript slmgr.vbs /ato
cscript slmgr.vbs /xpr |findstr "Timebased activation will expire" >nul
if %errorlevel%==0 (
@echo    	 ===	 Da active Windows 180 ngay	 ===
)
cscript ospp.vbs /xpr |findstr "The machine is permanently activated." >nul
if %errorlevel%==0 (
@echo    	 ===	 Da active Windows VINH VIEN	 ===
)
@echo Nhan phim bat ky de tiep tuc.......
pause >nul
cls
goto :main
)

:Check_active
start Check.cmd
cls
goto :main
)



