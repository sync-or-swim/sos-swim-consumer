package dev.syncorswim.swimconsumer

import com.beust.jcommander.Parameter

/**
 * Holds data from command-line flags.
 */
class Args {
    @Parameter(
            names = ["--swim-broker-url"],
            description = "The URL of the SWIM message broker",
            required = true)
    var swimBrokerUrl: String? = null

    @Parameter(
            names = ["--swim-queue"],
            description = "The name of the SWIM message queue to get FIXM data from",
            required = true)
    var swimQueue: String? = null

    @Parameter(
            names = ["--swim-connection-factory"],
            description = "The name of the SWIM connection factory",
            required = true)
    var swimConnectionFactory: String? = null

    @Parameter(
            names = ["--swim-vpn"],
            description = "The SWIM message VPN",
            required = true)
    var swimVpn: String? = null

    @Parameter(
            names = ["--rabbitmq-host"],
            description = "The hostname of the RabbitMQ server",
            required = true)
    var rabbitMqHost: String? = null

    @Parameter(
            names = ["--rabbitmq-queue-name"],
            description = "The name of the RabbitMQ queue to put FIXM data in",
            required = true)
    var rabbitMqQueueName: String? = null

    @Parameter(
            names = ["-h", "--help"],
            description = "Displays a help message",
            help = true)
    var help = false
}