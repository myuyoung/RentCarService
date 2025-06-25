#!/bin/bash
# stop_server.sh

APP_DIR="/home/ec2-user/app"

# Check if JAR file exists before trying to get its name
if ls $APP_DIR/*.jar 1> /dev/null 2>&1; then
    JAR_NAME=$(ls -tr $APP_DIR/*.jar | tail -n 1)
    CURRENT_PID=$(pgrep -f $JAR_NAME)

    if [ -z "$CURRENT_PID" ]; then
      echo "> 현재 실행 중인 애플리케이션이 없습니다."
    else
      echo "> 실행 중인 애플리케이션을 종료합니다. (PID: $CURRENT_PID)"
      kill -15 $CURRENT_PID
      sleep 5
    fi
else
    echo "> 배포된 JAR 파일이 없으므로 종료할 프로세스가 없습니다."
fi