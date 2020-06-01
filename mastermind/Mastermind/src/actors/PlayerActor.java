package actors;

import actors.messages.*;
import akka.actor.ActorRef;
import info.PlayerInfo;
import model.Sequence;
import model.SequenceInfoGuess;
import views.players.PlayersView;

import java.util.List;

public class PlayerActor extends MastermindActorImpl {

    private List<PlayerInfo> others;
    private ActorRef judgeActor;
    private PlayerInfo iAm;

    public PlayersView view;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.log(" pre start");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()

                // StartMsg dal Judge
                .match(StartMsg.class, msg -> {
                    this.view = msg.getView();
                    // Save players references.
                    this.judgeActor = msg.getJudge();
                    this.iAm = msg.getPlayer();
                    this.others = msg.getAllPlayers();
                    this.others.remove(iAm);

                    // Generate the sequence of current actor.
                    this.iAm.generateSequence(msg.getLength());

                    // StartMsg from Judge and view notification.
                    this.log("START MSG Received and NUMBER is " + iAm.getSequence());
                    this.view.addPlayer(msg.getName(), iAm.getSequence());

                    // TODO: Ho modificato questo messaggio per tornare solo gli altri. Non dovrebbe succedere nulla.
                    getSender().tell(new ReadyMsg(this.others), getSelf());

                // StartTurn dal Judge
                })
                .match(StartTurn.class, msg -> {
                    this.log(" START TURN Received");

                    // Send a guess to next unsolved player.
                    PlayerInfo playerSendGuess = getNextUnSolvedPlayer();
                    if(playerSendGuess == null) {
                        // TODO: Dovrei aver vinto il gioco.
                        // TODO: Capire perché rimane a null e il primo giocatore automaticamente vince il gioco.
                        this.log("I've win!!!");
                    } else {
                        // Generate a new guess based on previous guesses.
                        Sequence trySequence = playerSendGuess.extractGuess();
                        this.log("Send guess " + trySequence + " to the " + playerSendGuess.getName()
                                + " with number is " + playerSendGuess.getSequence());

                        // TODO: Perché viene inviato il riferimento a iAm? Quando sarebbero recuperabili tramite una getSender()?
                        playerSendGuess.getReference().tell(new GuessMsg(trySequence, this.iAm), getSelf());
                    }
                // GuessMsg dal player
                })
                .match(GuessMsg.class, msg -> {

                    this.log(" Response to the GUESS MSG at all players");

                    // Calculate the response.
                    Sequence guess = msg.getSequence();
                    SequenceInfoGuess response = this.iAm.getSequence().tryGuess(guess);

                    // Send the number response to all players except the sender.
                    others.forEach(elem -> elem.getReference().tell(new NumberAnswer(response.getRightNumbers(), response.getRightPlaceNumbers()), getSelf()));

                    // Send to the sender the full response.
                    getSender().tell(new ReturnGuessMsg(response), getSelf());

                // ReturnGuessMsg dal player che risponde in funzione del guess richiesto
                })
                .match(ReturnGuessMsg.class, msg -> {

                    // ReturnGuessMsg dal player (risposta di quanti caratteri giusti ho indovinato)
                    int rightNumbers = msg.getSequence().getRightNumbers();
                    int rightPlaceNumbers = msg.getSequence().getRightPlaceNumbers();

                    this.log(" RETURN GUESS MSG with response:\nRight Numbers: "
                            +rightNumbers+"\nRight Place Number: "+rightPlaceNumbers+"\n");

                    // Memorize the result of the guess.
                    this.others.forEach(elem -> {
                        if(elem.getName().equals(getSender().path().name())){
                            elem.setTry(msg.getSequence());
                        }
                    });

                    // Invio al Judge il msg di fine turno (vado al prossimo giocatore o al nuovo turno)
                    this.judgeActor.tell(new EndTurn(),getSelf());

                // NumberAnswer dal player che invia A TUTTI la risposta
                })
                .match(NumberAnswer.class, msg -> {

                    this.log(" NUMBERS ANSWER\nRight Numbers: "+msg.getRightNumbers()+"\nRight Place Number: "+msg.getRightPlaceNumbers()+"\n");

                    // TODO Memorizzare informazione sulla risposta ricevuta.
                    // In verità è inutile memorizzare la risposta se non si ha anche la sequenza ad essa collegata.
                    // Infatti la risposta del prof è stata totalmente inutile.

                }).build();
    }

    /**
     * Select a player to guess.
     * @return A player info. Return null if all other players are solved.
     */
    private PlayerInfo getNextUnSolvedPlayer() {
        for (PlayerInfo info: others) {
            if (!info.isSolved())
                return info;
        }
        return null;
    }
}
