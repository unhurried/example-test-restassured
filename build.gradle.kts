plugins {
    kotlin("jvm") version "1.6.10"
}

group = "io.github.unhurried.example.test.restassured"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.6.0")

    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("io.rest-assured:kotlin-extensions:4.5.1")
    testImplementation ("com.auth0:java-jwt:3.18.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
