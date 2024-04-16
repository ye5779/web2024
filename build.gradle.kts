import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  war
  id("org.springframework.boot") version "3.2.4"
  id("io.spring.dependency-management") version "1.1.4"
  kotlin("jvm") version "1.9.23"
  kotlin("plugin.spring") version "1.9.23"
  kotlin("plugin.jpa") version "1.9.23"
}

java {
  sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-tomcat")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
  implementation("org.hibernate:hibernate-core:6.+")

  // apache commons
  implementation("org.apache.commons:commons-lang3")

  // owasp java encoder
  implementation("org.owasp.encoder:encoder:1.+")

  // bcrypt
  implementation("de.svenkubiak:jBCrypt:+")

  // ognl
  runtimeOnly("ognl:ognl:+")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "21"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

archivesName = providers.gradleProperty("appname").get()

tasks.register<Copy>("dist") {
  dependsOn("bootWar")
  val source = "build/libs/${providers.gradleProperty("appname").get()}.war"
  val target = providers.gradleProperty("webapps").get()
  println("copy from $source to $target")
  from(source)
  into(target)
}
