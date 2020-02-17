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

Building
--------

This service can be built with Docker using the following command:

.. code-block:: bash

    docker build -t syncorswim/sos-swim-consumer .

Alternatively, assuming you have a JDK that supports Java 1.8 or newer
installed, you can use Gradle to make a local build.

.. code-block:: bash

    gradle distTar

Configuration
-------------

The SWIM Consumer is configured using environment variables.

- ``SWIM_BROKER_URL``: The URL of the SWIM message broker
- ``SWIM_QUEUE``: The name of the SWIM message queue to get FIXM data from
- ``SWIM_CONNECTION_FACTORY``: The name of the SWIM connection factory
- ``SWIM_VPN``: The SWIM message VPN
- ``RABBITMQ_HOSTNAME``: The hostname (domain name or IP address) of the RabbitMQ
  server to put FIXM data on
- ``RABBITMQ_QUEUE_NAME``: The name of the queue to put FIXM data on
- ``SWIM_USERNAME``: The username to log into SWIM with
- ``SWIM_PASSWORD``: The password to log into SWIM with

