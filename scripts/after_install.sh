#!/bin/bash
# after_install.sh

APP_DIR="/home/ec2-user/app"
JAR_NAME=$(ls -tr $APP_DIR/*.jar | tail -n 1)

echo "> JAR 파일에 실행 권한을 추가합니다."
chmod +x $JAR_NAME