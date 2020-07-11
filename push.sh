#!/bin/bash
#jenkins_automation_credentials is an environment variable
remote="$1"
branch="$2"
git push ${remote} ${branch}
if [ $? -eq 0 ]; then
  credentials="${jenkins_automation_credentials}"
  curl -X POST http://${credentials}@localhost:8081/job/ftp-client/build?token=c0013a61-f1a3-4ee0-93e1-f84e6f5099c9
fi
