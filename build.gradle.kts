plugins {
    id("java")
}

group = "me.ledovec"
version = "1.0-SNAPSHOT"

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.github.ZorTik:AdvancedSQLClient:0.5.3")
    implementation("com.sun.mail:javax.mail:1.6.2")
    implementation("mysql:mysql-connector-java:8.0.32")
    implementation("com.github.Carleslc:Simple-YAML:1.8.4")
}

tasks.test {
    useJUnitPlatform()
}