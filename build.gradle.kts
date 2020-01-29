plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.41"

    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Provides the JMS-compatible Solace implementation
    implementation("com.solacesystems:sol-jms:10.5.0")

    // RabbitMQ client for sending results to our other services
    implementation("com.rabbitmq:amqp-client:5.8.0")

    // A library for parsing command line flags
    implementation("com.beust:jcommander:1.78")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    // Define the main class for the application.
    mainClassName = "dev.syncorswim.swimconsumer.AppKt"
}
