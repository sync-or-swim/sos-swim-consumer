package dev.syncorswim.swimconsumer

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.BlockingQueue

/**
 * Reads results from the source queue and publishes them on RabbitMQ.
 */
class RabbitMqPublisher(args: Args, private var source: BlockingQueue<String>) : Thread() {
    private val conn: Connection
    private val channel: Channel
    private val queueName = args.rabbitMqQueueName

    init {
        // Connect to RabbitMQ
        val connFactory = ConnectionFactory()
        connFactory.setHost(args.rabbitMqHost)
        conn = connFactory.newConnection()
        channel = conn.createChannel()

        // Create the queue if it doesn't already exist
        channel.queueDeclare(queueName, false, false, false, null)
    }

    override fun run() {
        while (true) {
            val message = source.take()
            val messageBytes = message.toByteArray(StandardCharsets.UTF_8)
            channel.basicPublish("", queueName, null, messageBytes)

            println("Sent a message on RabbitMQ")
        }
    }
}