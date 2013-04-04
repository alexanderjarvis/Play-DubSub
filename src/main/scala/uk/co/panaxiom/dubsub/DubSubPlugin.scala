/*
 * Copyright 2013 Alexander Jarvis (@alexanderjarvis) and Panaxiom Ltd (http://panaxiom.co.uk)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

  def dubsub(implicit app: Application) = current.system.actorFor("/user/DubSub")

  def current(implicit app: Application): DubSubPlugin = app.plugin[DubSubPlugin] match {
    case Some(plugin) => plugin
    case _ => throw new PlayException("DubSubPlugin Error", "The DubSubPlugin has not been initialized! Please edit your conf/play.plugins file and add the following line: '500:uk.co.panaxiom.dubsub.DubSubPlugin' (500 is an arbitrary priority and may be changed to match your needs).")
  }

}