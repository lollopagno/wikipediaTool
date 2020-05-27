package info;

import actors.PlayerActor;
import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.ArrayList;

public class PlayerInfo {
    private final String name;
    private final ActorRef reference;
    private final int player;

    ArrayList<Integer> sequence = new ArrayList<>();

    public PlayerInfo(String name, ActorContext context, int player ) {
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

    public void setSequence(ArrayList<Integer> sequence) {this.sequence = sequence;}

    public ArrayList<Integer> getSequence() {return  this.sequence;}
}
