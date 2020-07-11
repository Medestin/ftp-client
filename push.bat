set remote = %1
set branch = %2
set url = http://%jenkins_automation_credentials%@localhost:8081/job/ftp-client/build?token=c0013a61-f1a3-4ee0-93e1-f84e6f5099c9

git push %1 %2
if %ERRORLEVEL%==0 (curl -X POST http://%jenkins_automation_credentials%@localhost:8081/job/ftp-client/build?token=c0013a61-f1a3-4ee0-93e1-f84e6f5099c9)
