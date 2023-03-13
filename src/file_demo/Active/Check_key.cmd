Echo Supporter:MINH-THUAN
cd C:\
if exist "1.txt" (start 1.txt &exit)
cd %windir%/system32
cls
(Echo ****************************************************************************************
Echo *                               BAN QUYEN WINDOWS                                      *
Echo **************************************************************************************** &cls
cscript slmgr.vbs /dli &cscript slmgr.vbs /xpr
Echo  
Echo ****************************************************************************************
Echo *                               BAN QUYEN OFFICE                                       *
Echo ****************************************************************************************
if exist "%ProgramFiles%\Microsoft Office\Office14\ospp.vbs" (cd /d "%ProgramFiles%\Microsoft Office\Office14")
if exist "%ProgramFiles%\Microsoft Office\Office15\ospp.vbs" (cd /d "%ProgramFiles%\Microsoft Office\Office15")
if exist "%ProgramFiles%\Microsoft Office\Office16\ospp.vbs" (cd /d "%ProgramFiles%\Microsoft Office\Office16")
cscript ospp.vbs /dstatus
)>C:\1.txt
start C:\1.txt