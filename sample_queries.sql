-- 샘플 SQL 스크립트 파일
-- 이 파일을 사용해서 테스트해볼 수 있습니다

-- 데이터베이스 선택
USE wookdb;

-- 현재 시간 조회
SELECT NOW() as current_time;

-- 데이터베이스 정보 확인
SELECT 
    DATABASE() as current_database,
    USER() as current_user,
    VERSION() as mysql_version;

-- 테이블 목록 조회
SHOW TABLES;

-- 테이블이 있다면 샘플 데이터 조회 예시 (테이블명은 실제 프로젝트에 맞게 수정)
-- SELECT * FROM users LIMIT 10;
-- SELECT * FROM products LIMIT 5;

-- 함수 사용 예시들
SELECT 
    UPPER('hello world') as uppercase_text,
    LOWER('HELLO WORLD') as lowercase_text,
    LENGTH('Hello World') as text_length,
    CONCAT('Hello', ' ', 'World') as concatenated_text;

-- 날짜 함수 예시들
SELECT 
    CURDATE() as current_date,
    CURTIME() as current_time,
    NOW() as current_datetime,
    DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s') as formatted_date;

-- 수학 함수 예시들
SELECT 
    ROUND(3.14159, 2) as rounded_number,
    CEIL(3.2) as ceiling,
    FLOOR(3.8) as floor,
    ABS(-5) as absolute_value,
    RAND() as random_number;

-- 조건부 함수 예시
SELECT 
    IF(5 > 3, 'True', 'False') as if_result,
    CASE 
        WHEN 1 = 1 THEN 'One equals One'
        WHEN 2 = 2 THEN 'Two equals Two'
        ELSE 'Something else'
    END as case_result;
