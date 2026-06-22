@echo off
echo ============================================
echo Running KS Admin Automation - %date% %time%
echo ============================================

cd /d C:\Users\JalalAhamadKhan\Desktop\Admin\ks-admin-automation

:: Clean old results
rmdir /s /q target 2>nul
rmdir /s /q reports 2>nul
rmdir /s /q screenshots 2>nul

:: Run tests
call mvn clean test -DsuiteXmlFile=testng.xml -Dmaven.test.failure.ignore=true

echo ============================================
echo Test execution complete - %date% %time%
echo ============================================
