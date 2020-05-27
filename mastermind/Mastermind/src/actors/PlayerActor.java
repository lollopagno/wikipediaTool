package actors;

import actors.messages.*;
import info.PlayerInfo;
import model.SequenceImpl;
import views.players.PlayersView;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActor extends MastermindActorImpl {

    private ArrayList<Integer> myNumber;
    private ArrayList<PlayerInfo> players;
    private PlayerInfo iAm;
    private int stringLength;


    private final Random rand = new Random();

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

                    // Lunghezza della stringa
                    this.stringLength = msg.getLength();
                    // Tutti i giocatori
                    this.players = msg.getAllPlayers();

                    // Crea il numero da indovinare
                    this.myNumber = createNumber(this.stringLength);

                    this.iAm = msg.getPlayer();
                    // Setto il numero che ha scelto il player
                    this.iAm.setSequence(this.myNumber);

                    // Comunico alla view il numero scelto
                    this.view.addPlayer(msg.getName(), this.myNumber);

                    getSender().tell(new ReadyMsg(this.players, msg.getLength()), getSelf());

                // StartTurn dal Judge
                }).match(StartTurn.class, msg -> {
                    this.log("Player START TURN Received:");

                    // Genera la stringa guess
                    ArrayList<Integer> tryNumber = createNumber(this.stringLength);

                    // Invio il guess a un player scelto a caso
                    PlayerInfo playerSendGuess = getPlayer();
                    // TODO funziona l'invio?? (al player playerSendGuess)
                    playerSendGuess.getReference().tell(new GuessMsg(tryNumber), getSelf());

                // GuessMsg dal player che ha richiesto il guess
                }).match(GuessMsg.class, msg -> {

                    // Stringa chiesta dal players
                    ArrayList<Integer> guess = msg.getSequence();

                    SequenceImpl seqImpl = new SequenceImpl(this.iAm.getSequence());
                    // Invio al player la risposta di caratteri corretti o sbagliati
                    getSender().tell(new ReturnGuessMsg(seqImpl.tryNumbers(guess)), getSelf());

                // ReturnGuessMsg dal player (risposta di quanti caratteri giusti ho indovinato)
                }).match(ReturnGuessMsg.class, msg -> {

                    int rightNumbers = msg.getSequence().getRightNumbers();
                    int rightPlaceNumbers = msg.getSequence().getRightPlaceNumbers();

                    // 1- Inviare a tutti i players la risposta
                    // 2- Inviare al Judge il msg fine turno (vado al prossimo giocatore o al nuovo turno)

                    //getSender().tell(new EndTurn(/*boolean*/),getSelf());

                }).build();
    }

    // Scelgo un player da inviare il guess
    private PlayerInfo getPlayer(){

        int number = this.rand.nextInt(this.players.size());
        return this.players.get(number);
    }

    // Creo il numero random da chiedere ad un guess
    private ArrayList<Integer> createNumber(int length){

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
