#!/bin/bash

DEPLOY_SOURCE_DIR="/tmp/deploy"
APP_TARGET_DIR="/home/ec2-user/app"
APP_NAME="rentcarservice"
JAR_FILE_PATH="$DEPLOY_SOURCE_DIR/build/libs/*.jar"
LOG_FILE="$APP_TARGET_DIR/deploy.log"

mkdir -p $APP_TARGET_DIR
touch $LOG_FILE
chown ec2-user:ec2-user $APP_TARGET_DIR
chown ec2-user:ec2-user $LOG_FILE

echo "--- Deployment script started at $(date) ---" > $LOG_FILE

echo "Stopping any running instances of the application..." >> $LOG_FILE
CURRENT_PID=$(pgrep -f "java -jar $APP_TARGET_DIR/$APP_NAME.jar")

if [ -n "$CURRENT_PID" ]; then
    echo "Found running process with PID: $CURRENT_PID. Stopping it." >> $LOG_FILE
    kill -15 "$CURRENT_PID"
    sleep 5
fi

echo "Moving deployment files from $DEPLOY_SOURCE_DIR to $APP_TARGET_DIR" >> $LOG_FILE
ACTUAL_JAR=$(find $DEPLOY_SOURCE_DIR -name "*.jar" | head -n 1)
if [ -z "$ACTUAL_JAR" ]; then
    echo "CRITICAL: JAR file not found in $DEPLOY_SOURCE_DIR" >> $LOG_FILE
    exit 1
fi
mv "$ACTUAL_JAR" "$APP_TARGET_DIR/$APP_NAME.jar"

echo "Starting application as ec2-user..." >> $LOG_FILE
export Spring_Mail_UserName="jjjonga33@naver.com"
export Spring_Mail_Password="WX1QPXDJ87N7"
export Jwt_Secret="qwertyuiopasdfghjklzxcvbnmqwerty"
export Admin_Email="parkcw5784@gmail.com"

su - ec2-user -c "nohup java -jar $APP_TARGET_DIR/$APP_NAME.jar > /dev/null 2>&1 &"

echo "Checking if application has started..." >> $LOG_FILE
sleep 15

STATUS_CHECK_PID=$(pgrep -f "java -jar $APP_TARGET_DIR/$APP_NAME.jar")
if [ -z "$STATUS_CHECK_PID" ]; then
    echo "CRITICAL: Application failed to start. Check logs." >> $LOG_FILE
    echo "--- Last 50 lines of log ---" >> $LOG_FILE
    exit 1
fi

echo "SUCCESS: Application is running with PID: $STATUS_CHECK_PID" >> $LOG_FILE
echo "--- Deployment script finished at $(date) ---" >> $LOG_FILE

exit 0