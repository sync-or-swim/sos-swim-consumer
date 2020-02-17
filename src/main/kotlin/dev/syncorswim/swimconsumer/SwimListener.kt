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
 * @param config The configuration object to consult
 * @param destination The queue to output messages to
 */
class SwimListener(
        config: Configuration,
        private var destination: BlockingQueue<String>) : MessageListener {
    private var conn: Connection

    init {
        // Convert properties into a context. This puts the configuration in a
        // standard format that the Solace understands
        val env = Hashtable<String, Any>()
        env[Context.INITIAL_CONTEXT_FACTORY] = "com.solacesystems.jndi.SolJNDIInitialContextFactory"
        env[Context.PROVIDER_URL] =  config.swimBrokerUrl
        env[Context.SECURITY_PRINCIPAL] = config.swimUsername
        env[Context.SECURITY_CREDENTIALS] = config.swimPassword
        env[SupportedProperty.SOLACE_JMS_VPN] = config.swimVpn
        val context = InitialContext(env)

        // Create a new connection
        val connFactory = context.lookup(config.swimConnectionFactory) as ConnectionFactory
        conn = connFactory.createConnection()

        // Start the consumer
        val queue = context.lookup(config.swimQueue) as JMSQueue
        val session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)
        val consumer = session.createConsumer(queue)
        consumer.messageListener = this
    }

    fun start() {
        conn.start()
    }

    override fun onMessage(message: Message?) {
        when (message) {
            is BytesMessage -> destination.add(bytesMessageToString(message))
            is TextMessage -> destination.add(message.text)
            else -> println("Unknown message type")
        }
    }
}


fun bytesMessageToString(message: BytesMessage): String {
    val content = ByteArray(message.bodyLength.toInt())
    message.readBytes(content)
    return String(content)
}

