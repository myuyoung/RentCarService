#!/bin/bash
# start_server.sh

APP_DIR="/home/ec2-user/app"
JAR_PATH=$(find $APP_DIR -name "*.jar" | head -n 1)
LOG_FILE="$APP_DIR/deploy.log"

export Spring_Mail_UserName="jjjonga33@naver.com"
export Spring_Mail_Password="WX1QPXDJ87N7"
export Jwt_Secret="qwertyuiopasdfghjklzxcvbnmqwerty"
export Admin_Email="parkcw5784@gmail.com"

# JAR 파일 존재 여부를 확인하고, 없으면 스크립트를 종료합니다.
if [ -z "$JAR_PATH" ]; then
    echo "> JAR 파일을 찾을 수 없습니다." >> $LOG_FILE
    exit 1
fi

echo "> 새 애플리케이션을 배포합니다: $JAR_PATH"
# nohup: 터미널 세션이 끊겨도 프로세스가 계속 실행되도록 함
# 2>&1: 표준 에러를 표준 출력으로 리다이렉션
# > $LOG_FILE: 표준 출력을 로그 파일에 씀
# &: 백그라운드에서 실행
nohup java -jar $JAR_PATH > $LOG_FILE 2>&1 &