# DubSub Plugin for Play Framework

[DubSub](https://github.com/alexanderjarvis/DubSub) - Distributed PubSub with Akka Cluster

## Installation

Add the following to your Build.scala

```scala
val appDependencies = Seq(
  "uk.co.panaxiom" %% "play-dubsub" % "0.2-SNAPSHOT"
)

val main = play.Project(appName, appVersion, appDependencies).settings(
  resolvers += Resolver.url("Alex's GitHub Repository", url("http://alexanderjarvis.github.com/snapshots/"))(Resolver.ivyStylePatterns)
)
```

Add to your conf/play.plugins file (and create if it does not exist), where 500 is the priority that you can adjust relative to other plugins.

	500:uk.co.panaxiom.dubsub.DubSubPlugin

## Usage

You will need to define an actor which responds to Subscribe, Unsubscribe and Publish in order to receive messages from DubSub:

```scala
import uk.co.panaxiom.dubsub._

class SocketActor extends Actor {
  def receive = {
    case Subscribe(channel) => Logger.info("Subscribed to dubsub channel " + channel)
    case Unsubscribe(channel) => Logger.info("Unsubscribed to dubsub channel " + channel)
    case Publish(channel, message) => Logger.info("Publish from dubsub " + channel + " message " + message)
  }
}
```

From within this actor you can send messages to DubSub like the following:

```scala
val dubsub = DubSubPlugin.dubsub

// Subscribes this actor to messages published to the channel with "topic"
dubsub ! Subscribe("topic")

// Unsubscribes this actor from "topic"
dubsub ! Unsubscribe("topic")

// Publishes a message to all subscribers of "topic" (across all cluster nodes)
dubsub ! Publish("topic", "message")
```

## Start

Set the remote port of at least your first seed node as a runtime property. Other nodes can be started without this and will pick a random port.

	play stage
	target/start -Ddubsub.akka.remote.netty.tcp.port=2551

You can specify the seed-nodes of the cluster in your application.conf:

```
dubsub {
  akka {
    cluster {
      seed-nodes = [
        "akka.tcp://DubSubSystem@127.0.0.1:2551",
        "akka.tcp://DubSubSystem@127.0.0.1:2552"]
    }
  }
}
```

## Example

Play comes with a websocket-chat example, which has been enhanced with DubSub: [websocket-chat-dubsub](https://github.com/alexanderjarvis/websocket-chat-dubsub)