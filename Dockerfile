# 1. 베이스 이미지: 프로젝트와 동일한 Java 17 사용
FROM openjdk:17-jdk-slim

# 2. 애플리케이션 JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar


# 3. 애플리케이션 실행 (Pinpoint Agent 설정은 잠시 제외)
ENTRYPOINT ["java", "-jar", "/app.jar"]

## 4. 애플리케이션 실행
## java -javaagent 옵션으로 Pinpoint Agent를 활성화하고 Collector 서버를 바라보게 합니다.
#ENTRYPOINT ["java", \
#            "-javaagent:/pinpoint-agent/pinpoint-bootstrap-3.0.3.jar", \
#            "-Dpinpoint.agentId=rentcar-app-01", \
#            "-Dpinpoint.applicationName=RentCarService", \
#            "-Dpinpoint.profiler.profiles.active=release", \
#            "-Dprofiler.transport.grpc.collector.ip=host.docker.internal", \
#            "-Dprofiler.transport.grpc.collector.port=9991", \
#            "-jar", "/app.jar"]