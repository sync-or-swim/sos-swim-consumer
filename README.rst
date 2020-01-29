SWIM Consumer
=============

This is a service for consuming FIXM data from SWIM and making it available on
a RabbitMQ queue.

Why?
----

SWIM uses Solace, a proprietary message broker, to expose FIXM data to
applications. SWIM seems to have configured Solace to use SMF, a messaging
format specific to Solace. There aren't any Python libraries available to read
this format, but Solace provides an official Java library for doing so. This
service uses that library to make messages available on RabbitMQ, which does
have Python support along with many other languages.

Running
-------

Assuming you have a JDK that supports Java 1.8 or newer installed, you can use
the included Gradle wrapper to run SWIM Consumer.

.. code-block:: bash

    ./gradlew run {arguments}


In addition to the required arguments, you'll need to set the `SWIM_USERNAME`
and `SWIM_PASSWORD` environment variables to authorize with SWIM.

