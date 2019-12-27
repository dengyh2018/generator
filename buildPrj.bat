@echo off
echo *************start to build project*************
call mvn -Dmaven.test.skip=true -T 4 clean install -U
echo *************project build success*************
pause