package dev.syncorswim.swimconsumer

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import java.net.ConnectException
import java.nio.charset.StandardCharsets
import java.util.concurrent.BlockingQueue
import java.util.logging.Logger


/**
 * Reads results from the source queue and publishes them on RabbitMQ.
 */
class RabbitMqPublisher(args: Args, private var source: BlockingQueue<String>) : Thread() {
    private val logger: Logger = Logger.getLogger(javaClass.name)

    private var conn: Connection? = null
    private val channel: Channel
    private val queueName = args.rabbitMqQueueName

    init {
        // Connect to RabbitMQ
        val connFactory = ConnectionFactory()
        connFactory.host = args.rabbitMqHost

        while (conn == null) {
            try {
                conn = connFactory.newConnection()
                break
            } catch (ex: ConnectException) {
                logger.warning("Connection error while connecting to RabbitMQ: " + ex.message + ". Retrying...")
                sleep(1000)
            }
        }
        channel = conn!!.createChannel()

        // Create the queue if it doesn't already exist
        channel.queueDeclare(queueName, false, false, false, null)
    }

    override fun run() {
        while (true) {
            val message = source.take()
            val messageBytes = message.toByteArray(StandardCharsets.UTF_8)
            channel.basicPublish("", queueName, null, messageBytes)
        }
    }
}
