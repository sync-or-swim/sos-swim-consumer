package dev.syncorswim.swimconsumer

import kotlin.system.exitProcess

/**
 * Holds configuration information for the SWIM Consumer.
 *
 * @param swimBrokerUrl the URL of the SWIM message broker
 * @param swimQueue the name of the SWIM message queue to get FIXM data from
 * @param swimConnectionFactory the name of the SWIM connection factory
 * @param swimVpn the SWIM message VPN
 * @param rabbitMqHost the hostname of the RabbitMQ server
 * @param rabbitMqQueueName the name of the RabbitMQ queue to put FIXM data in
 * @param swimUsername the username to log into SWIM with
 * @param swimPassword the password to log into SWIM with
 */
class Configuration(
        val swimBrokerUrl: String,
        val swimQueue: String,
        val swimConnectionFactory: String,
        val swimVpn: String,
        val rabbitMqHost: String,
        val rabbitMqQueueName: String,
        val swimUsername: String,
        val swimPassword: String) {

    companion object {
        /**
         * Creates a Configuration object by consulting the corresponding
         * environment variables on the system.
         */
        fun fromEnvironmentVariables() = Configuration(
                swimBrokerUrl = getEnv("SWIM_BROKER_URL"),
                swimQueue = getEnv("SWIM_QUEUE"),
                swimConnectionFactory = getEnv("SWIM_CONNECTION_FACTORY"),
                swimVpn = getEnv("SWIM_VPN"),
                rabbitMqHost = getEnv("RABBITMQ_HOST"),
                rabbitMqQueueName = getEnv("RABBITMQ_QUEUE_NAME"),
                swimUsername = getEnv("SWIM_USERNAME"),
                swimPassword = getEnv("SWIM_PASSWORD")
        )
    }

}


/**
 * Gets an environment variable with the given name, or exits the program with
 * an error if it is not set.
 */
private fun getEnv(name: String): String {
    val value = System.getenv(name)
    if (value == null) {
        System.err.println("Environment variable $name not set")
        exitProcess(1)
    }
    return value
}
