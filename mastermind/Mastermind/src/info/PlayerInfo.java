package info;

import actors.PlayerActor;
import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import model.Sequence;

public class PlayerInfo {
    private final String name;
    private final ActorRef reference;
    // TODO: Capire cosa sia questo player. Deve essere un indice? Identificativo? (In quel caso non basta il nome?
    private final int player;

    private Sequence sequence;

    public PlayerInfo(String name, ActorContext context, int player) {
        this.name = name;
        this.reference = context.actorOf(Props.create(PlayerActor.class), name);
        this.player = player;
    }

    public String getName() {
        return this.name;
    }

    public ActorRef getReference() {
        return reference;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public Sequence getSequence() {
        return this.sequence;
    }
}