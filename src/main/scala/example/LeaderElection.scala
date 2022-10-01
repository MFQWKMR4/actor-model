package example

import akka.actor.{ Actor, ActorRef, Props }
import cats.implicits._

object ProcessActor {

    def props(id: Int, receiver: ActorRef) = {
        Props[ProcessActor](new ProcessActor(id, receiver))
    }

    case class Msg(id: Int)

    /* for test */
    case class SetReceiver(receiver: ActorRef)

}

class ProcessActor(id: Int, var receiver: ActorRef) extends Actor {

    import ProcessActor._

    var receiveCount = 0
    var localMax     = -1

    def receive = {
        case Msg(receiveId) => {
            receiveCount match {
                case 0 => {
                    receiveCount = receiveCount + 1
                    localMax = math.max(id, receiveId)
                    receiver ! Msg(id)
                    receiver ! Msg(receiveId)
                }
                case _ => {
                    if (receiveId == id) {
                        println(s"${id} stops: " + localMax)
                    } else {
                        localMax = math.max(localMax, receiveId)
                        receiver ! Msg(receiveId)
                    }
                }
            }
        }
        case SetReceiver(newReceiver) => { this.receiver = newReceiver }
    }
}

object ChangRobertsActor {

    def props(id: Int, receiver: ActorRef) = {
        Props[ChangRobertsActor](new ChangRobertsActor(id, receiver))
    }

    val FINISH = "fin"

    case class Msg(id: Int, msg: Option[String])

    /* for test */
    case class SetReceiver(receiver: ActorRef)

}

class ChangRobertsActor(id: Int, var receiver: ActorRef) extends Actor {

    import ChangRobertsActor._

    var receiveCount = 0
    var localMax     = -1

    def receive = {
        case Msg(receiveId, msg) => {
            receiveCount match {
                case 0 => {
                    receiveCount = receiveCount + 1
                    localMax = math.max(id, receiveId)
                    receiver ! Msg(localMax, None)
                }
                case _ => {
                    if (receiveId == id || msg.contains(FINISH)) {
                        receiver ! Msg(receiveId, FINISH.some)
                        println("stop: " + localMax)
                        context stop self
                    }

                    if (receiveId > localMax) {
                        localMax = receiveId
                        receiver ! Msg(localMax, None)
                    }
                }
            }
        }
        case SetReceiver(newReceiver) => { this.receiver = newReceiver }
    }
}
