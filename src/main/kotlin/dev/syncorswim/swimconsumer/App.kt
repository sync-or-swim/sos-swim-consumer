package dev.syncorswim.swimconsumer

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import java.util.concurrent.LinkedBlockingQueue
import kotlin.system.exitProcess


fun main(argv: Array<String>) {
    // Read command line flags
    val args = Args()
    val jCommander = JCommander.newBuilder()
            .addObject(args)
            .build()
    try {
        jCommander.parse(*argv)
    } catch (ex: ParameterException) {
        System.err.println(ex.message + "\n")
        jCommander.usage()
        exitProcess(1)
    }
    if (args.help) {
        jCommander.usage()
        exitProcess(0)
    }

    // Load SWIM credentials
    val swimUsername = System.getenv("SWIM_USERNAME")
    if (swimUsername == null) {
        exit("SWIM_USERNAME environment variable not set")
    }
    val swimPassword = System.getenv("SWIM_PASSWORD")
    if (swimPassword == null) {
        exit("SWIM_PASSWORD environment variable not set")
    }

    val messageQueue = LinkedBlockingQueue<String>()

    val listener = SwimListener(args, swimUsername, swimPassword, messageQueue)
    val publisher = RabbitMqPublisher(args, messageQueue)

    publisher.start()
    listener.start()

    while (true) {
        Thread.sleep(1000)
    }
}


fun exit(message: String) {
    System.err.println(message)
    exitProcess(1)
}
