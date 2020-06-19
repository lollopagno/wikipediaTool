package model;

import actors.PlayerActor;
import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;

public class PlayerReference {
    String name;
    ActorRef ref;

    public PlayerReference(String name, ActorContext context) {
        this.name = name;
        this.ref = context.actorOf(Props.create(PlayerActor.class), name);
    }

    public String getName() {
        return name;
    }

    public ActorRef getRef() {
        return ref;
    }
}
