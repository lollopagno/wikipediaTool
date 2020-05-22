package actors;

import actors.messages.StartMsg;

public class PlayerActor extends MastermindActorImpl {
    @Override
    public Receive createReceive() {
        // TODO START
        // TODO READY
        // TODO START_TURN
        // TODO GUESS
            // TODO Esegue sulla propria sequence il tentativo
        return receiveBuilder().match(StartMsg.class, msg -> {
            this.log("Player START GAME Received:");
        }).build();
    }
}
