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

  private var dubSubSystemEnabled = false

  lazy val dubsubSystem: ActorSystem = {
    val system = ActorSystem("DubSubSystem", app.configuration.underlying.getConfig("dubsub"), app.classloader)
    dubSubSystemEnabled = true
    Logger("play").info("DubSub system has started")
    system
  }

  override def onStart() {
	// Empty, laoding the system here triggers Netty exceptions
  }

  override def onStop() {
    if (dubSubSystemEnabled) {
      dubsubSystem.shutdown()
      dubsubSystem.awaitTermination()
      Logger("play").info("DubSub has stopped")
    }
  }
}

object DubSubPlugin {
  
  // The downside of this change is that we avid the Netty exception but a call to DubSubPlugin.system should be done when accessing the first page of the application (Application.index)
  // or at a similar point, otherwise the channels subscribing to it may fail due to it not being initialized (that happens in the websockets example and the preStart call, the subscribe 
  // fails and messages are not shared)

  val system = {
    val system = Play.current.plugin[DubSubPlugin].map(_.dubsubSystem).getOrElse {
      sys.error("The DubSubPlugin has not been initialized! Please edit your conf/play.plugins file and add the following line: '500:uk.co.panaxiom.dubsub.DubSubPlugin' (500 is an arbitrary priority and may be changed to match your needs).")
    }
    system.actorOf(Props[DubSub], "DubSub")  //register system here to avoid Netty issues
    system
  }


  val dubsub = system.actorFor("/user/DubSub")

}
