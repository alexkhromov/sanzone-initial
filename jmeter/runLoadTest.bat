@echo off
title Sanzone perfomance testing...

cd /jmeter

call jmeter -n -t C:\DEV\projects\sanzone-initial\jmeter\createSummarySanzone_v1.jmx -l createSummarySanzone_v1.log
call jmeter -g createSummarySanzone_v1.log