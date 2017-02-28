@echo off
title Sanzone install...

set PROJECT_HOME=C:\DEV\projects\sanzone-initial
set BUILD_TARGET=target
set BACK_END=sanzone-server
set FRONT_END=sanzone-client
set TOMCAT_SERVER=Tomcat8
set APACHE_SERVER=Apache2.4
set ENV=local

sc query %TOMCAT_SERVER% | find "STATE" | find "RUNNING" > nul
if "%ERRORLEVEL%" == "0" (
    net stop %TOMCAT_SERVER%
) else (
    echo %TOMCAT_SERVER% is not running
)

sc query %APACHE_SERVER% | find "STATE" | find "RUNNING" > nul
if "%ERRORLEVEL%" == "0" (
    net stop %APACHE_SERVER%
) else (
    echo %APACHE_SERVER% is not running
)

cd %CATALINA_HOME%\logs
del /q *
for /d %%p in (*.*) do rmdir "%%p" /s /q

cd %APACHE_HOME%\logs
del /q *
for /d %%p in (*.*) do rmdir "%%p" /s /q

cd %CATALINA_HOME%\webapps
del /q ROOT.war
rmdir /s /q ROOT

cd %APACHE_HOME%\htdocs\sanzone
del /q *
for /d %%p in (*.*) do rmdir "%%p" /s /q

cd %PROJECT_HOME%\%BACK_END%
call mvn clean install -P %ENV%

cd %PROJECT_HOME%\%FRONT_END%
call grunt --env=%ENV%

xcopy %PROJECT_HOME%\%BACK_END%\%BUILD_TARGET%\ROOT.war %CATALINA_HOME%\webapps
xcopy /s %PROJECT_HOME%\%FRONT_END%\%BUILD_TARGET% %APACHE_HOME%\htdocs\sanzone\

net start %TOMCAT_SERVER%

net start %APACHE_SERVER%