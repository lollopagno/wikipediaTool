package actors;

import actors.messages.*;
import info.PlayerInfo;
import model.SequenceImpl;
import model.SequenceInfoJudge;
import views.players.PlayersView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayerActor extends MastermindActorImpl {

    private ArrayList<Integer> myNumber;
    public PlayersView view;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.log("Giocatore creato");
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()

                // StartMsg dal Judge
                .match(StartMsg.class, msg -> {
                    this.log("Player START MSG Received:");

                    // Crea il numero da indovinare
                    this.myNumber = createNumber(msg.getLength());

                    // Comunico alla view il numero scelto
                    this.view.addPlayer(msg.getName(), this.myNumber);

                    getSender().tell(new ReadyMsg(msg.getPlayers(), msg.getLength()), getSelf());

                // StartTurn dal Judge
                }).match(StartTurn.class, msg -> {
                    this.log("Player START TURN Received:");

                    // Genera la stringa guess
                    ArrayList<Integer> tryNumber = createNumber(msg.getLength());

                    // Invio il guess a un player scelto a caso
                    //getReference().tell(new GuessMsg(tryNumber),getSelf());

                // GuessMsg dal player che ha richiesto il guess
                }).match(GuessMsg.class, msg -> {

                    // Stringa chiesta dal players
                    ArrayList guess = msg.getSequence();

                    SequenceImpl seq = new SequenceImpl();

                    getSender().tell(new ReturnGuessMsg(seq.tryNumbers(guess)));

                // ReturnGuessMsg dal player (risposta di quanti caratteri giusti ho indovinato)
                }).match(ReturnGuessMsg.class, msg -> {
                    //TODO AGGIORNARE LE RISPOSTE --> NUMERI CORRETTI E NUM ALLE POS CORRETTE
                    //getSender().tell(new EndTurn(/*boolean*/),getSelf());

                }).build();
    }

    // Creo il numero random da chiedere ad un guess
    private ArrayList<Integer> createNumber(int length){

        Random rand = new Random();
        ArrayList <Integer> number = new ArrayList<>();
        for(int i = 0; i< length; i++){
            number.add(rand.nextInt(10));
        }

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
