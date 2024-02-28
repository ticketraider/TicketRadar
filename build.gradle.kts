import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.noarg") version "1.8.22"
    kotlin("kapt") version "1.8.22" // 쿼리DSL
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

val queryDslVersion = "5.0.0"

val kotestVersion = "5.5.5" // kotest

val mockkVersion = "1.13.8" // mockk

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-validation")//벨리데이션

    implementation("org.springframework.boot:spring-boot-starter-security")//시큐리티
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")//시큐리티

    implementation("com.querydsl:querydsl-jpa:$queryDslVersion:jakarta") // 쿼리DSL
    kapt("com.querydsl:querydsl-apt:$queryDslVersion:jakarta") // 쿼리DSL

    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")//시큐리티
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")//시큐리티
    runtimeOnly("org.postgresql:postgresql")
//    runtimeOnly ("com.h2database:h2")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test") //기본 테스트 의존성

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion") // kotest 관련
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion") // kotest 관련
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3") // kotest 관련
    testImplementation("io.mockk:mockk:$mockkVersion") // mockk
    testImplementation("org.postgresql:postgresql")//테스트용 데이터소스URL

    implementation("org.springframework.boot:spring-boot-starter-data-redis") //Lettuce
    implementation("org.redisson:redisson-spring-boot-starter:3.21.1") //Redisson

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test>().configureEach() { // 변경 !!
    useJUnitPlatform()
}

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}
