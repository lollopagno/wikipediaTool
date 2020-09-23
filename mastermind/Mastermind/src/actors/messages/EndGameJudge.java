package actors.messages;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class EndGameJudge implements Message {

    private final ActorSystem mySystem;
    private final ActorRef myReference;

    public EndGameJudge(ActorSystem system, ActorRef reference) {
        this.mySystem = system;
        this.myReference = reference;
    }

    public ActorSystem getSystem() {
        return this.mySystem;
    }

    public ActorRef getReference() {
        return this.myReference;
    }
}
