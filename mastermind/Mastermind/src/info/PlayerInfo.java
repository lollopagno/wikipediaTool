package info;

import actors.PlayerActor;
import actors.messages.Message;
import akka.actor.Actor;
import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;

public class PlayerInfo {
    private String name;
    private ActorRef reference;

    public PlayerInfo(String name, ActorContext context) {
        this.name = name;
        this.reference = context.actorOf(Props.create(PlayerActor.class), name);
    }

    public String getName() {
        return this.name;
    }

    public ActorRef getReference() {
        return reference;
    }
}
