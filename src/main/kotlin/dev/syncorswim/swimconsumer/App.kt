package dev.syncorswim.swimconsumer

import java.util.concurrent.LinkedBlockingQueue


fun main() {
    val messageQueue = LinkedBlockingQueue<String>()
    val configuration = Configuration.fromEnvironmentVariables()

    val listener = SwimListener(configuration, messageQueue)
    val publisher = RabbitMqPublisher(configuration, messageQueue)

    publisher.start()
    listener.start()

    while (true) {
        Thread.sleep(1000)
    }
}
