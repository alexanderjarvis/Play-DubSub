# DubSub Plugin for Play Framework

[DubSub](https://github.com/alexanderjarvis/DubSub) - Distributed PubSub with Akka Cluster

## Installation

Add the following to your Build.scala

```
val appDependencies = Seq(
  "uk.co.panaxiom" %% "play-dubsub" % "0.1-SNAPSHOT"
)

val main = play.Project(appName, appVersion, appDependencies).settings(
  resolvers += Resolver.url("Alex's GitHub Repository", url("http://alexanderjarvis.github.com/snapshots/"))(Resolver.ivyStylePatterns)
)
```

## Usage

You will need to define an actor which responds to Subscribe, Unsubscribe and Publish in order to receive messages from DubSub:

```
class SocketActor extends Actor {
  def receive = {
	case Subscribe(channel) => Logger.info("Subscribed to dubsub channel " + channel)
    case Unsubscribe(channel) => Logger.info("Unsubscribed to dubsub channel " + channel)
    case Publish(channel, message) => Logger.info("Publish from dubsub " + channel + " message " + message)
  }
}
```

From within this actor you can send messages to DubSub like the following:

```
# Subscribes this actor to messages published to the channel with "topic"
DubSubPlugin.actor ! Subscribe("topic")

# Unsubscribes this actor from "topic"
DubSubPlugin.actor ! Unsubscribe("topic")

# Publishes a message to all subscribers of "topic" (across all cluster nodes)
DubSubPlugin.actor ! Publish("topic", "message")
```

## Start

Set the remote port of at least your first seed node as a runtime property. Other nodes can be started without this and will pick a random port.
	
	play stage
	target/start -Ddubsub.akka.remote.netty.port=2551
	
You can specify the seed-nodes of the cluster in your application.conf:

```
dubsub {
  akka {
    cluster {
      seed-nodes = [
        "akka://DubSubSystem@127.0.0.1:2551",
        "akka://DubSubSystem@127.0.0.1:2552"]
    }
  }
}
```
