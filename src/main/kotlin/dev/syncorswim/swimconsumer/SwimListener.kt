package dev.syncorswim.swimconsumer

import com.solacesystems.jms.SupportedProperty
import java.util.*
import java.util.concurrent.BlockingQueue
import javax.jms.*
import javax.jms.Queue as JMSQueue
import javax.naming.Context
import javax.naming.InitialContext

/**
 * Connects to the SWIM message broker and consumes messages.
 *
 * @param properties The application's properties
 * @param destination The queue to output messages to
 */
class SwimListener(properties: Properties, private var destination: BlockingQueue<String>) : MessageListener {
    private var conn: Connection

    init {
        // Convert properties into a context. This puts the configuration in a
        // standard format that the Solace understands
        val env = Hashtable<String, Any>()
        env[Context.INITIAL_CONTEXT_FACTORY] = "com.solacesystems.jndi.SolJNDIInitialContextFactory"
        env[Context.PROVIDER_URL] =  properties.getProperty("swim.broker_url")
        env[Context.SECURITY_PRINCIPAL] = properties.getProperty("swim.username")
        env[Context.SECURITY_CREDENTIALS] = properties.getProperty("swim.password")
        env[SupportedProperty.SOLACE_JMS_VPN] = properties.getProperty("swim.vpn")
        val context = InitialContext(env)

        // Create a new connection
        val connFactory = context.lookup(properties.getProperty("swim.connection_factory")) as ConnectionFactory
        conn = connFactory.createConnection()

        // Start the consumer
        val queue = context.lookup(properties.getProperty("swim.queue")) as JMSQueue
        val session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)
        val consumer = session.createConsumer(queue)
        consumer.messageListener = this
    }

    fun start() {
        conn.start()
    }

    override fun onMessage(message: Message?) {
        println("Received message from SWIM")

        when (message) {
            is BytesMessage -> destination.add(bytesMessageToString(message))
            is TextMessage -> destination.add(message.text)
            else -> println("Unknown message type")
        }
    }
}
