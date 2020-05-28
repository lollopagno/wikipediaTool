package actors;

import actors.messages.*;
import info.PlayerInfo;
import model.Sequence;
import model.SequenceImpl;
import views.players.PlayersView;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActor extends MastermindActorImpl {

    private Sequence sequence;
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

                .match(StartMsg.class, msg -> {
                    this.view = msg.getView();
                    // Lunghezza della stringa
                    this.stringLength = msg.getLength();
                    // Tutti i giocatori
                    this.players = msg.getAllPlayers();

                    // Crea il numero da indovinare
                    this.sequence = createNumber(this.stringLength);

                    this.iAm = msg.getPlayer();
                    // StartMsg dal Judge
                    this.log("Player " + this.iAm.getName()+ " START MSG Received");
                    // Setto il numero che ha scelto il player
                    this.iAm.setSequence(this.sequence);

                    // Comunico alla view il numero scelto
                    this.view.addPlayer(msg.getName(), this.sequence);

                    getSender().tell(new ReadyMsg(this.players, this.iAm.getName()), getSelf());

                }).match(StartTurn.class, msg -> {
                    // StartTurn dal Judge
                    this.log("Player "  + this.iAm.getName()+ " START TURN Received");

                    // Genera la stringa guess
                    Sequence trySequence = createNumber(this.stringLength);

                    // Invio il guess a un player scelto a caso
                    PlayerInfo playerSendGuess = getPlayer(); // TODO : TOGLIERE SE STESSO DAL RANDOM
                    // TODO funziona l'invio?? (al player playerSendGuess)
                    playerSendGuess.getReference().tell(new GuessMsg(trySequence), getSelf());
                    this.log(this.iAm.getName()+  " Guess " + playerSendGuess.getName());
                }).match(GuessMsg.class, msg -> {
                    // GuessMsg dal player che ha richiesto il guess
                    // Stringa chiesta dal players
                    Sequence guess = msg.getSequence();

                    // Invio al player la risposta di caratteri corretti o sbagliati
                    getSender().tell(new ReturnGuessMsg(this.iAm.getSequence().tryGuess(guess)), getSelf());
                    this.log(this.iAm.getName() +" Response ");
                }).match(ReturnGuessMsg.class, msg -> {
                    this.log(this.iAm.getName() + " Return Response");
                    // ReturnGuessMsg dal player (risposta di quanti caratteri giusti ho indovinato)
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

    /**
     * Creo il numero random da chiedere ad un guess
     * @param length Lunghezza della sequenza.
     * @return Sequenza generata.
     */
    private Sequence createNumber(int length){
        ArrayList <Integer> number = new ArrayList<>();
        for(int i = 0; i< length; i++){
            number.add(rand.nextInt(10));
        }

        return new SequenceImpl(number);
    }

    /*private  void solutionCorrect(ArrayList PlayerNumber, ArrayList myNumber){
        if(PlayerNumber == myNumber) {
            //TODO SEGNALO LA WIEW CHE C'È STATA UN VINCITORE
        }else{
            // TODO CONTROLLO DEI VARI NUMERI ALL'INTERNO QUALI CORRETTI E QUALI NO
        }
    }*/
    private Sequence getMyNumber(){
        return this.sequence;
    }

}
