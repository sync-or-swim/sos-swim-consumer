package dev.syncorswim.swimconsumer

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import javax.jms.*
import kotlin.system.exitProcess

const val PROPERTIES_FILE_NAME = "settings.properties"

fun main() {
    val properties = loadProperties()

    val messageQueue = LinkedBlockingQueue<String>()

    val listener = SwimListener(properties, messageQueue)
    val publisher = RabbitMqPublisher(properties, messageQueue)

    publisher.start()
    listener.start()

    while (true) {
        Thread.sleep(1000)
    }
}

fun bytesMessageToString(message: BytesMessage): String {
    val content = ByteArray(message.bodyLength.toInt())
    message.readBytes(content)
    return String(content)
}

/**
 * Loads the properties file, making sure all required properties are defined.
 */
fun loadProperties(): Properties {
    val properties = Properties()
    try {
        FileInputStream(PROPERTIES_FILE_NAME).use {
            properties.load(it)
        }
    } catch (ex: FileNotFoundException) {
        System.err.println("Properties file $PROPERTIES_FILE_NAME missing from the current directory")
        exitProcess(1)
    }

    // Check that the properties file has all required properties
    val required = listOf(
            "swim.broker_url",
            "swim.connection_factory",
            "swim.username",
            "swim.password",
            "swim.vpn",
            "rabbitmq.host"
            )
    for (property in required) {
        if (!properties.containsKey(property)) {
            System.err.println("$PROPERTIES_FILE_NAME is missing required property '$property'")
            exitProcess(1)
        }
    }

    return properties
}
