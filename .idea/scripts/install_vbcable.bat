@echo off
echo 正在解压并静默安装 VB-CABLE...
powershell -Command "Expand-Archive -Path vb-cable-driver.zip -DestinationPath vb_driver"
cd vb_driver\VBCABLE_Setup
start /wait VBCABLE_Setup.exe /S