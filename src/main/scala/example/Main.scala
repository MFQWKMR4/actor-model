package example

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object SilentActor {
  case class SilentMessage(data: String)

  case class GetState(receiver: ActorRef)
}

class SilentActor extends Actor {

  import SilentActor._

  var internalState = Vector[String]()

  override def receive: Receive = {
    case SilentMessage(data) => internalState = internalState :+ data
    case GetState(receiver) => receiver ! internalState
  }

  def state = internalState
}

object SendingActor {
  def props(receiver: ActorRef) = Props(new SendingActor(receiver))

  case class Event(id: Long)

  case class SortEvents(unsorted: Vector[Event])

  case class SortedEvents(sorted: Vector[Event])
}

class SendingActor(receiver: ActorRef) extends Actor {

  import SendingActor._

  def receive = {
    case SortEvents(unsorted) =>
      receiver ! SortedEvents(unsorted.sortBy(_.id))
  }
}

object Main extends App {
    println("start")
}
