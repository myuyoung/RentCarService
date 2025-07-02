#!/bin/bash

APP_DIR="/home/ec2-user/app"
LOG_FILE="$APP_DIR/deploy.log"

echo "--- ApplicationStart Hook ---" >> "$LOG_FILE"
echo "[$(date)] Running start_server.sh" >> "$LOG_FILE"

JAR_FILE=$(ls -tr $APP_DIR/*.jar | tail -n 1)

# JAR 파일이 존재하는지 확인하고 로그를 남깁니다.
if [ -z "$JAR_FILE" ]; then
    echo "[$(date)] ERROR: No JAR file found in $APP_DIR" >> "$LOG_FILE"
    exit 1
fi

echo "[$(date)] Found JAR file to execute: $JAR_FILE" >> "$LOG_FILE"

export Spring_Mail_UserName="jjjonga33@naver.com"
export Spring_Mail_Password="WX1QPXDJ87N7"
export Jwt_Secret="qwertyuiopasdfghjklzxcvbnmqwerty"
export Admin_Email="parkcw5784@gmail.com"

echo "[$(date)] Starting application..." >> "$LOG_FILE"
echo "[$(date)] Command: nohup java -jar \"$JAR_FILE\" >> \"$LOG_FILE\" 2>&1 &" >> "$LOG_FILE"

nohup java -jar "$JAR_FILE" >> "$LOG_FILE" 2>&1 &

PID=$!
sleep 2
if ! ps -p $PID > /dev/null; then
    echo "[$(date)] ERROR: Failed to start the Java process. Check Java version or JAR file." >> "$LOG_FILE"
    echo "[$(date)] See details in the log file above." >> "$LOG_FILE"
    exit 1
fi

echo "[$(date)] Application process started successfully with PID: $PID" >> "$LOG_FILE"
echo "[$(date)] start_server.sh finished." >> "$LOG_FILE"

exit 0