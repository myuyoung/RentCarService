#!/bin/bash

APP_DIR="/home/ec2-user/app"
LOG_FILE="$APP_DIR/deploy.log"
PORT=7950

echo "--- ValidateService Hook ---" >> "$LOG_FILE"
echo "[$(date)] Running validate_server.sh" >> "$LOG_FILE"

for i in {1..30}; do
  echo "[$(date)] Checking application status on port $PORT... (Attempt $i/30)" >> "$LOG_FILE"

  STATUS=$(netstat -tuln | grep $PORT)
  if [ -n "$STATUS" ]; then
    echo "[$(date)] SUCCESS: Application is up and running on port $PORT." >> "$LOG_FILE"
    exit 0
  fi
  sleep 1
done

echo "[$(date)] ERROR: Timeout. Application failed to start on port $PORT." >> "$LOG_FILE"
echo "[$(date)] Displaying last 20 lines from $LOG_FILE for debugging:" >> "$LOG_FILE"

if [ -f "$LOG_FILE" ]; then
    tail -n 20 "$LOG_FILE" >> "$LOG_FILE"
    tail -n 20 "$LOG_FILE"
fi

exit 1