plugins {
    java
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "io.jaehoonmandev"
version = "0.0.1-SNAPSHOT"
description = "chat-gw-scaleout-poc"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-actuator")     // 인스턴스 식별, 헬스 체크용
    implementation("org.springframework.boot:spring-boot-starter-validation")   // 데이터 검증; 잘못된 요청은 세션까지 오지 않도록
    implementation("org.springframework.boot:spring-boot-starter-webmvc")       // 웹훅 수신, REST 컨트롤러, 내장 Tomcat, JSON 변환(Jackson)

    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
