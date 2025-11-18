package me.changwook.config.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch; // StopWatch 추가

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect // 이 클래스가 Aspect임을 선언
@Component // 스프링 빈으로 등록
public class LoggingAspect {

    // 각 클래스에 맞는 Logger 인스턴스 생성
    private static Logger getLogger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    }

    // Pointcut: 적용 범위를 지정
    // com.example.demo 패키지 하위의 모든 Controller 또는 Service 어노테이션이 붙은 클래스의 public 메서드를 대상으로 함
    @Pointcut("within(me.changwook..*) && (within(@org.springframework.stereotype.Controller *) || within(@org.springframework.stereotype.Service *) || within(@org.springframework.web.bind.annotation.RestController *))")
    public void controllerAndServiceMethods() {
    }

    /**
     * @Around: 메서드 실행 전후, 예외 발생 시 모두 제어
     * 메서드 실행 시간 측정 및 파라미터 로깅에 유용
     */
    @Around("controllerAndServiceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = getLogger(joinPoint);
        StopWatch stopWatch = new StopWatch(); // 실행 시간 측정

        try {
            // 메서드 파라미터 로깅
            String args = Arrays.stream(joinPoint.getArgs())
                    .map(String::valueOf) // 간단히 문자열로 변환 (주의: 민감 정보 마스킹 필요)
                    .collect(Collectors.joining(", "));

            log.info("▶▶▶ START: {}({})", joinPoint.getSignature().toShortString(), args);

            stopWatch.start();
            Object result = joinPoint.proceed(); // 실제 메서드 실행
            stopWatch.stop();

            log.info("◀◀◀ END: {} (Execution time: {} ms)", joinPoint.getSignature().toShortString(), (Object) stopWatch.getTotalTimeMillis());

            return result; // 메서드 실행 결과 반환

        } catch (Throwable e) {
            // @Around에서는 예외를 다시 던져야 스프링의 예외 처리가 정상 동작합니다.
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }

            log.error("XXX ERROR: {} (Execution time: {} ms) - Exception: {}",
                    joinPoint.getSignature().toShortString(),
                    (Object) stopWatch.getTotalTimeMillis(),
                    e.getMessage());
            throw e;
        }
    }
}