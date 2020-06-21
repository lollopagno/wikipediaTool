package actors;

import actors.messages.*;
import akka.actor.ActorRef;
import info.PlayerInfo;
import model.OtherPlayersStore;
import model.Sequence;
import model.SequenceInfoGuess;
import views.players.PlayersView;

import java.util.Optional;
import java.util.concurrent.Executors;

public class PlayerActor extends MastermindActorImpl {

    private ActorRef judgeActor;
    private PlayerInfo iAm;
    OtherPlayersStore others;
    boolean jumpTurn = false, haveSend = false;

    public PlayersView view;

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartMsg.class, msg -> {
                    // StartMsg dal Judge
                    this.view = msg.getView();

                    // Save players references.
                    this.judgeActor = getSender();
                    iAm = new PlayerInfo(msg.getPlayer());

                    // Generate the sequence of current actor.
                    iAm.generateSequence(msg.getLength());
                    others = new OtherPlayersStore(msg.getLength());

                    // StartMsg from Judge and view notification.
                    this.view.addPlayer(iAm.getName(), iAm.getSequence());

                    // Save all other players in the store.
                    msg.getAllPlayers().forEach(elem -> {
                        if (elem.getName().equals(iAm.getName()))
                            return;
                        others.addPlayer(new PlayerInfo(elem));
                    });

                    // TODO: Ho modificato questo messaggio per tornare solo gli altri. Non dovrebbe succedere nulla.
                    getSender().tell(new ReadyMsg(), getSelf());
                })
                .match(StartTurn.class, msg -> {
                    // StartTurn dal Judge.
                    jumpTurn = false;
                    haveSend = false;
                    startExtraction();
                })
                .match(JumpTurn.class, msg -> {
                    //Tempo finito salta il turno
                    if (haveSend)
                        return;

                    this.jumpTurn = true;
                    this.log("----------------- Jump Turn Received -------------------");
                    this.view.showMessage(String.format("%s salta il turno.", iAm.getName()));
                    this.judgeActor.tell(new EndTurn(), getSelf());
                })
                .match(GuessMsg.class, msg -> {
                    // GuessMsg dal player. Calculate the response.
                    Sequence guess = msg.getSequence();
                    SequenceInfoGuess response = this.iAm.getSequence().tryGuess(guess);

                    // Send to the sender the full response and notify others.
                    others.notifyOtherPlayersAboutResponse(response, getSelf());
                    getSender().tell(new ReturnGuessMsg(response), getSelf());
                })
                .match(ReturnGuessMsg.class, msg -> {
                    // ReturnGuessMsg dal player che risponde in funzione del guess richiesto
                    int rightNumbers = msg.getSequence().getRightNumbers();
                    int rightPlaceNumbers = msg.getSequence().getRightPlaceNumbers();

                    // this.log("RETURN GUESS MSG with response:\nRight Numbers: "
                    //         + rightNumbers + "\nRight Place Number: " + rightPlaceNumbers + "\n");

                    // Save the guess and notify the view.
                    String enemy = getSender().path().name();
                    this.others.saveGuess(enemy, msg.getSequence());
                    view.inputSolution(iAm.getName(), enemy, msg.getSequence());
                    if (msg.getSequence().getNumbers().getSequence().size() == msg.getSequence().getRightPlaceNumbers()) {
                        view.playerSolved(iAm.getName(), enemy);
                        view.showMessage("Sequenza indovinata a " + enemy + ".");
                    }

                    // Invio al Judge il msg di fine turno (vado al prossimo giocatore o al nuovo turno)
                    this.judgeActor.tell(new EndTurn(), getSelf());
                })
                .match(NumberAnswer.class, msg -> {
                    // NumberAnswer dal player che invia A TUTTI la risposta

                    // TODO Memorizzare informazione sulla risposta ricevuta.
                    // In verità è inutile memorizzare la risposta se non si ha anche la sequenza ad essa collegata.
                    // Infatti la risposta del prof è stata totalmente inutile.
                }).match(EndGame.class, msg -> {
                    // Judge declare end game. Stop the player.
                    // this.log("My execution is finished!");
                    this.iAm.stopPlayer(getContext());
                }).build();
    }

    private void startExtraction() {
        Executors.newSingleThreadExecutor().execute(this::generateSequenceForPlayer);
    }

    private void generateSequenceForPlayer() {
        // Generate a new guess based on previous guesses.
        Optional<PlayerInfo> info = this.others.getNextUnsolvedPlayer();
        Sequence trySequence;
        if (info.isPresent()) {
            // Questa azione può essere molto dispendiosa, occore eseguirla su un altro Thread.
            trySequence = info.get().extractGuess(iAm.getSequence().getSequence().size());
            if (jumpTurn) {
                this.log("------------ out of time --------------");
            } else {
                haveSend = true;
                // this.log("Send guess " + trySequence.getSequence() + " to the " + info.get().getName());

                // TODO: Perché viene inviato il riferimento a iAm? Quando sarebbero recuperabili tramite una getSender()?
                info.get().getReference().tell(new GuessMsg(trySequence), getSelf());
            }
        } else {
            // Controllo se il giocatore ha vinto.
            this.log("I've win!!");
            // Invio al Judge il messaggio di vittoria
            this.judgeActor.tell(new PlayerWin(), getSelf());
        }
    }
}