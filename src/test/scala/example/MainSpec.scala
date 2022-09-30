package example

import org.scalatest._
import akka.actor.{ ActorSystem, Props }
import akka.testkit.{ TestActorRef, TestKit }
import akka.testkit.TestKit.shutdownActorSystem

import scala.util.Random

trait StopSystemAfterAll extends BeforeAndAfterAll {
    this: TestKit with Suite =>

    override protected def afterAll() {
        super.afterAll()
        shutdownActorSystem(system)
    }
}

class SilentActorTest
    extends TestKit(ActorSystem("testsystem")) with WordSpecLike with MustMatchers with StopSystemAfterAll {

    "A silent actor" must {
        "single thread" in {
            import SilentActor._

            val silentActor = TestActorRef[SilentActor]
            silentActor ! SilentMessage("whisper")
            silentActor.underlyingActor.state must (contain("whisper"))
        }

        "multi thread" in {
            import SilentActor._

            val silentActor = system.actorOf(Props[SilentActor], "s3")
            silentActor ! SilentMessage("whisper1")
            silentActor ! SilentMessage("whisper2")
            silentActor ! GetState(testActor)
            expectMsg(Vector("whisper1", "whisper2"))
        }
    }

    "A sending actor" must {

        "send a message to another actor when it has finished processing" in {
            import SendingActor._

            val props        = SendingActor.props(testActor)
            val sendingActor = system.actorOf(props, "sendingActor")

            val size         = 1000
            val maxInclusive = 100000

            def randomEvents() = (0 until size).map { _ =>
                Event(Random.nextInt(maxInclusive))
            }.toVector

            val unsorted   = randomEvents()
            val sortEvents = SortEvents(unsorted)
            sendingActor ! sortEvents
            expectMsgPF() { case SortedEvents(events) =>
                events.size must be(size)
                unsorted.sortBy(_.id) must be(events)
            }
        }
    }
}
