# web2024 (v2024.4.17)

Kotlin, Spring Boot, Spring Data JPA, Thymeleaf를 사용한 Post Web application

## 실행 환경

1. Java 17 이상
2. MariaDB 10.x 이상
3. Kotlin 1.9.x
4. Spring Boot 3.2.x
5. Spring Data JPA 3.2.x
6. Thymeleaf 3.x

## 설치 방법

1. MariaDB를 설치하고 스키마를 생성 후에 아래 sql을 실행해서 테이블들을 설치한다. 
   - src/main/resources/schema.sql 
2. ./copy_properties.sh 실행해서 properties 파일을 복사 후에 설정을 수정한다.
   - src/main/resources/application.properties 에서 spring.datasource.url 설정

## 서버 실행 방법

다음 세 가지 방법으로 실행할 수 있다.

1. bootRun 실행
```
./gradlew bootRun
```
 
2. 실행 jar 생성
```
./gradlew bootJar
java -jar build/libs/web2024.jar
```

3. war 파일을 생성해서 WAS에 올림
```
./gradlew bootWar
copy build/libs/web2024.war $TOMCAT_HOME/webapps/
```

## Running server

https://irafe.com/web2024/
