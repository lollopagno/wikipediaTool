package actors;

import actors.messages.*;
import akka.actor.ActorRef;
import info.PlayerInfo;
import model.Sequence;
import model.SequenceImpl;
import model.SequenceInfoGuess;
import views.players.PlayersView;

import java.util.ArrayList;
import java.util.Random;

public class PlayerActor extends MastermindActorImpl {

    private Sequence sequence;
    private ArrayList<PlayerInfo> players;
    private ActorRef judgeActor;
    private PlayerInfo iAm;

    private int stringLength;

    private final Random rand = new Random();

    public PlayersView view;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.log("Player created!");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()

                // StartMsg dal Judge
                .match(StartMsg.class, msg -> {

                    this.view = msg.getView();
                    // Lunghezza della stringa
                    this.stringLength = msg.getLength();
                    // Tutti i giocatori
                    this.players = msg.getAllPlayers();

                    // Crea il numero da indovinare
                    this.sequence = createNumber(this.stringLength);
                    this.judgeActor = msg.getJudge();
                    this.iAm = msg.getPlayer();

                    // Setto il numero che ha scelto il player
                    this.iAm.setSequence(this.sequence);

                    // StartMsg dal Judge
                    this.log("Player " + this.iAm.getName()+ " START MSG Received and NUMBER is "+this.sequence);

                    // Comunico alla view il numero scelto
                    this.view.addPlayer(msg.getName(), this.sequence);

                    getSender().tell(new ReadyMsg(this.players, this.iAm.getName()), getSelf());

                // StartTurn dal Judge
                }).match(StartTurn.class, msg -> {

                    this.log("Player "  + this.iAm.getName()+ " START TURN Received");

                    // Genera la stringa guess
                    Sequence trySequence = createNumber(this.stringLength);

                    // Invio il guess a un player scelto a caso
                    PlayerInfo playerSendGuess = getPlayer();

                    this.log("Player "  + this.iAm.getName()+ " send guess "+trySequence+" to the "+playerSendGuess.getName()
                            +" with number is "+playerSendGuess.getSequence());

                    playerSendGuess.getReference().tell(new GuessMsg(trySequence, this.iAm), getSelf());

                // GuessMsg dal player
                }).match(GuessMsg.class, msg -> {

                    this.log("Players "+this.iAm.getName() +" Response to the GUESS MSG at all players");

                    // Stringa chiesta dal players
                    Sequence guess = msg.getSequence();

                    // Risposta da inviare
                    SequenceInfoGuess response = this.iAm.getSequence().tryGuess(guess);

                    // Invio a tutti i player (tranne me stesso e al mittente) la risposta di caratteri corretti o sbagliati
                    ArrayList<PlayerInfo> playersTmp = this.players;
                    // Rimuovo me stesso
                    playersTmp.remove(this.iAm);

                    playersTmp.forEach(elem -> {

                        // Invio a tutti gli altri la risposta
                        elem.getReference().tell(new NumberAnswer(response.getRightNumbers(), response.getRightPlaceNumbers()), getSelf());
                    });

                    // Invio al mittente la risposta
                    getSender().tell(new ReturnGuessMsg(this.iAm.getSequence().tryGuess(guess)), getSelf());

                // ReturnGuessMsg dal player che risponde in funzione del guess richiesto
                }).match(ReturnGuessMsg.class, msg -> {

                    // ReturnGuessMsg dal player (risposta di quanti caratteri giusti ho indovinato)
                    int rightNumbers = msg.getSequence().getRightNumbers();
                    int rightPlaceNumbers = msg.getSequence().getRightPlaceNumbers();

                    this.log("Players "+this.iAm.getName() + " RETURN GUESS MSG with response:\nRight Numbers: "
                            +rightNumbers+"\nRight Place Number: "+rightPlaceNumbers+"\n");

                    // Invio al Judge il msg di fine turno (vado al prossimo giocatore o al nuovo turno)
                    this.judgeActor.tell(new EndTurn(),getSelf());

                // NumberAnswer dal player che invia A TUTTI la risposta
                }).match(NumberAnswer.class, msg -> {

                    this.log("Players "+this.iAm.getName()+" NUMBERS ANSWER\nRight Numbers: "+msg.getRightNumbers()+"\nRight Place Number: "+msg.getRightPlaceNumbers()+"\n");

                    // TODO Memorizzare informazione sulla risposta ricevuta

                }).build();
    }

    // Scelgo un player da inviare il guess
    private PlayerInfo getPlayer(){

        ArrayList<PlayerInfo> playersTmp;

        playersTmp = this.players;
        playersTmp.remove(this.iAm);

        int number = this.rand.nextInt(playersTmp.size());

        return playersTmp.get(number);
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
