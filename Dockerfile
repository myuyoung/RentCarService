# 1. 베이스 이미지: 프로젝트와 동일한 Java 21 사용
FROM openjdk:21-jdk-slim

# 2. 프로젝트 내의 agent 디렉토리를 컨테이너 안으로 복사
COPY agent/pinpoint-agent-3.0.3 /pinpoint-agent

# 3. 애플리케이션 JAR 파일 복사
ARG JAR_FILE=build/libs/Wook-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 4. 애플리케이션 실행
# java -javaagent 옵션으로 Pinpoint Agent를 활성화하고 Collector 서버를 바라보게 합니다.
ENTRYPOINT ["java", \
            "-javaagent:/pinpoint-agent/pinpoint-bootstrap-3.0.3.jar", \
            "-Dpinpoint.agentId=rentcar-app-01", \
            "-Dpinpoint.applicationName=RentCarService", \
            "-Dprofiler.transport.grpc.collector.ip=pinpoint-collector", \
            "-jar", "/app.jar"]