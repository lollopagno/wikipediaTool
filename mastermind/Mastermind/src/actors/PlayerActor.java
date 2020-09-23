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
    boolean jumpTurn = false, sent = false;

    public PlayersView view;

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

                    getSender().tell(new ReadyMsg(), getSelf());
                })
                .match(StartTurn.class, msg -> {
                    // StartTurn dal Judge.
                    jumpTurn = false;
                    sent = false;
                    startExtraction();
                })
                .match(JumpTurn.class, msg -> {
                    //Tempo finito salta il turno
                    if (sent)
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
                    getSender().tell(new ReturnGuessMsg(response), getSelf());
                })
                .match(ReturnGuessMsg.class, msg -> {
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
                    // Not memorized solution.
                }).match(EndGame.class, msg -> {
                    // Judge declare end game. Stop the player.
                    this.iAm.stopPlayer(getContext());
                }).build();
    }

    /**
     * Start Thread generate guess
     */
    private void startExtraction() {
        Executors.newSingleThreadExecutor().execute(this::generateSequenceForPlayer);
    }

    private void generateSequenceForPlayer() {
        // Generate a new guess based on previous guesses.
        Optional<PlayerInfo> info = this.others.getNextUnsolvedPlayer();
        Sequence trySequence;
        if (info.isPresent()) {
            // Questa azione può essere molto dispendiosa, occorre eseguirla su un altro Thread.
            trySequence = info.get().extractGuess(iAm.getSequence().getSequence().size());
            if (jumpTurn) {
                this.log("------------ out of time --------------");
            } else {
                sent = true;
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