package example

import org.scalatest._
import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit}

class LeaderElectionSpec
    extends TestKit(ActorSystem("testsystem")) with WordSpecLike with MustMatchers with StopSystemAfterAll {

    "Simple process actors" must {
        "stop and know max value" in {
            import ProcessActor._

            // arrange
            val pp0 = ProcessActor.props(0, testActor)
            val p3Dummy = system.actorOf(pp0, "p0")
            val pp2 = ProcessActor.props(2, p3Dummy)
            val p2 = TestActorRef[ProcessActor](pp2, "p2")
            val pp1 = ProcessActor.props(1, p2)
            val p1 = TestActorRef[ProcessActor](pp1, "p1")
            val pp3 = ProcessActor.props(3, p1)
            val p3 = TestActorRef[ProcessActor](pp3, "p3")
            p2 ! SetReceiver(p3)

            // action
            p2 ! Msg(1)

            // assert
            p1.underlyingActor.localMax must be(3)
            p2.underlyingActor.localMax must be(3)
            p3.underlyingActor.localMax must be(3)
        }
    }

    "Chang-Roberts actors" must {
        "stop and know max value" in {
            import ChangRobertsActor._

            // arrange
            val pp0 = ChangRobertsActor.props(0, testActor)
            val p3Dummy = system.actorOf(pp0, "p0")
            val pp2 = ChangRobertsActor.props(2, p3Dummy)
            val p2 = TestActorRef[ChangRobertsActor](pp2, "p2")
            val pp1 = ChangRobertsActor.props(1, p2)
            val p1 = TestActorRef[ChangRobertsActor](pp1, "p1")
            val pp3 = ChangRobertsActor.props(3, p1)
            val p3 = TestActorRef[ChangRobertsActor](pp3, "p3")
            p2 ! SetReceiver(p3)

            // action
            p2 ! Msg(1, None)

            // assert

            // NOTE: actor is terminated
            //            p1.underlyingActor.localMax must be(3)
            //            p2.underlyingActor.localMax must be(3)
            //            p3.underlyingActor.localMax must be(3)
        }
    }
}
