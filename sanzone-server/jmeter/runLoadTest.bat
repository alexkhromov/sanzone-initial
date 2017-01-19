@echo off
title Sanzone perfomance testing...

cd /jmeter

del /q *
for /d %%p in (*.*) do rmdir "%%p" /s /q

call jmeter -n -t C:\DEV\projects\sanzone-initial\sanzone-server\jmeter\createSummarySanzone_v1.jmx -l createSummarySanzone_v1.log
call jmeter -g createSummarySanzone_v1.log