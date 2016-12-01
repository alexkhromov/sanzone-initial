@echo off
title Sanzone perfomance testing...

cd /jmeter

call jmeter -n -t C:\DEV\projects\sanzone-initial\jmeter\createSummarySanzoneImageWithOpenCV_v1.jmx -l createSummarySanzoneImageWithOpenCV_v1.log
call jmeter -g createSummarySanzoneImageWithOpenCV_v1.log