package actors;

import actors.messages.*;
import info.PlayerInfo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayerActor extends MastermindActorImpl {


    private ArrayList<Integer> myNumber;
    private JudgeActor judgeActor;
    private PlayerInfo elem;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.log("Giocatore creato");
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartMsg.class, msg -> {
                    this.log("Player START MSG Received:");
                    this.myNumber = createNumber(msg.getLength());
                    judgeActor.getSender().tell(new ReadyMsg(msg.getPlayers(), msg.getLength()), getSelf());
                }).match(StartTurn.class, msg -> {
                    this.log("Player START TURN Received:");
                    //genera stringa random da inviare ad un altro player
                    ArrayList<Integer> tryNumber = createNumber(msg.getLength());
                    //TODO DEFINIRE A CHI LO INVIA
                    elem.getReference().tell(new GuessMsg(tryNumber),getSelf());
                }).match(ReturnGuessMsg.class, msg -> {
                    //TODO AGGIORNARE LE RISPOSTE --> NUMERI CORRETTI E NUM ALLE POS CORRETTE
                    judgeActor.getSender().tell(new EndTurn(/*boolean*/),getSelf());
                }).build();
    }

    private ArrayList<Integer> createNumber(int length){
        Random rand = new Random();
        ArrayList <Integer> number = new ArrayList<>();
        for(int i = 0; i< length; i++){
            number.add(rand.nextInt(10));
        }
        //mettere che deve essere length giusto --> 100 è 3
        return number;
    }
    private  void solutionCorrect(ArrayList PlayerNumber, ArrayList myNumber){
        if(PlayerNumber == myNumber) {
            //TODO SEGNALO LA WIEW CHE C'È STATA UN VINCITORE
        }else{
            // TODO CONTROLLO DEI VARI NUMERI ALL'INTERNO QUALI CORRETTI E QUALI NO
        }
    }
    private ArrayList<Integer> getMyNumber(){
        return this.myNumber;
    }

}
