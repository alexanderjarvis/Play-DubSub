package uk.co.panaxiom.dubsub

import play.api._

import akka.actor._
import akka.cluster.Cluster

class DubSubPlugin(application: Application) extends Plugin {

  lazy val system = {
    import com.typesafe.config.ConfigFactory
    val config = ConfigFactory.load()
    ActorSystem("DubSubSystem", config.getConfig("dubsub"))
  }

  override def onStart {
    system.actorOf(Props[DubSub], "DubSub")
    Logger.info("DubSub has started")
  }

  override def onStop {
    system.shutdown
    system.awaitTermination
    Logger.info("DubSub has stopped")
  }
}

object DubSubPlugin {

  def actor(implicit app: Application) = current.system.actorFor("/user/DubSub")

  def current(implicit app: Application): DubSubPlugin = app.plugin[DubSubPlugin] match {
    case Some(plugin) => plugin
    case _ => throw new PlayException("DubSubPlugin Error", "The DubSubPlugin has not been initialized! Please edit your conf/play.plugins file and add the following line: '500:uk.co.panaxiom.dubsub.DubSubPlugin' (500 is an arbitrary priority and may be changed to match your needs).")
  }

}