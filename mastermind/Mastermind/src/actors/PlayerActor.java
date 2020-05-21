package actors;

import actors.messages.StartGameMsg;
import actors.messages.StartMsg;
import akka.actor.AbstractActor;

public class PlayerActor extends AbstractActor {

    public PlayerActor(int otherPlayers){
    }

    @Override
    public Receive createReceive() {
        // TODO START
        // TODO READY
        // TODO START_TURN
        // TODO GUESS
            // TODO Esegue sulla propria sequence il tentativo
        return receiveBuilder().match(StartMsg.class, msg -> {
            System.out.println("START GAME Received:");
        }).build();
    }
}
