package actors;

import actors.messages.ReadyMsg;
import actors.messages.StartMsg;
import actors.messages.StartTurn;

import java.util.Random;

public class PlayerActor extends MastermindActorImpl {

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.log("Giocatore creato");
    }

    @Override
    public Receive createReceive() {
        // TODO START
        // TODO READY
        // TODO START_TURN
        // TODO GUESS
            // TODO Esegue sulla propria sequence il tentativo
        return receiveBuilder()
                .match(StartMsg.class, msg -> {
                    this.log("Player START MSG Received:");
                    int myNumber = createNumber(msg.getLength());
                    getSelf().tell(new ReadyMsg(), getSelf());
                }).match(StartTurn.class, msg -> {
                    this.log("Player START TURN Received:");
                }).build();
    }

    private int createNumber(int length){
        Random rand = new Random();
        //mettere che deve essere length giusto --> 100 è 3
        return rand.nextInt(length);
    }
}
