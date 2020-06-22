package actors;

import actors.messages.*;
import akka.actor.ActorSystem;
import model.PlayerReference;
import model.SequenceInfoJudge;
import views.players.PlayersView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class JudgeActor extends MastermindActorImpl {
    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture future;
    private SequenceInfoJudge sequenceInfoJudge;
    private PlayersView view;
    private int allReadyMsg = 0;
    private int timeBetweenTurns = 0;
    boolean playerWon = false;

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartGameMsg.class, msg -> {
                    // StartGameMsg inviato dalla view
                    String message = "Judge START GAME Received";
                    this.view = msg.getView();
                    this.view.showMessage(message);
                    this.timeBetweenTurns = msg.getTime();
                    startGame(msg.getPlayers(), msg.getLength());
                })
                .match(ReadyMsg.class, msg -> {
                    // Msg dai player di READY
                    this.allReadyMsg++;

                    if (this.allReadyMsg == this.sequenceInfoJudge.getNPlayers()) {
                        // Set the new player order and wake up another player.
                        this.sequenceInfoJudge.newOrderTurn();

                        this.allReadyMsg = 0;
                        wakeUpNextPlayer();
                    }
                })
                .match(EndTurn.class, msg -> {
                    // Msg dal player di endTurn (è terminato SOLO il suo turno)
                    this.allReadyMsg++;
                    if (this.allReadyMsg == this.sequenceInfoJudge.getNPlayers()) {
                        // Set the new player order.
                        this.sequenceInfoJudge.newOrderTurn();
                        this.allReadyMsg = 0;
                    }

                    // Wake up a new player.
                    wakeUpNextPlayer();
                })
                .match(PlayerWin.class, msg -> {
                    // Partita terminata: Vittoria di un giocatore
                    String message = getSender().path().name() + " has won!";
                    this.log(message);
                    playerWon = true;
                    this.stopExecutionPlayer();
                    view.showMessage(message);
                })
                .match(EndGameJudge.class, msg -> {
                    // Partita terminata: Pressione pulsante STOP
                    this.stopExecutionPlayer();

                    ActorSystem myReferenceJudge = msg.getSystem();
                    myReferenceJudge.stop(msg.getReference());
                    log("Stop Game!");
                })
                .build();
    }

    /**
     * Stop players executions.
     */
    private void stopExecutionPlayer() {
        this.sequenceInfoJudge.showPlayer().forEach(player -> player.getRef().tell(new EndGame(), getSelf()));
    }


    /**
     * Send the msg start turn to the next player.
     * If a player have already won, return the func.
     */
    private void wakeUpNextPlayer() {
        if (playerWon)
            return;

        PlayerReference nextPlayer = this.sequenceInfoJudge.getNextPlayer();
        nextPlayer.getRef().tell(new StartTurn(), getSelf());

        startTimer(nextPlayer);
    }

    /**
     * Calcolo del timer.
     *
     * @param player Player to stop.
     */
    private void startTimer(PlayerReference player) {
        if (future != null && !future.isDone() && !future.isCancelled())
            future.cancel(true);

        future = service.schedule(
                () -> player.getRef().tell(new JumpTurn(), getSelf()),
                this.timeBetweenTurns, TimeUnit.MILLISECONDS);
    }

    /**
     * Create players at the start of game.
     *
     * @param nPlayers Total player number.
     * @param length   Sequence length.
     */
    private void startGame(int nPlayers, int length) {
        final List<PlayerReference> players = new ArrayList<>();
        for (int i = 0; i < nPlayers; i++) {
            PlayerReference player = new PlayerReference("player_" + i, this.getContext());
            players.add(player);
        }

        // Create the judge instance to manage all turns between players.
        this.sequenceInfoJudge = new SequenceInfoJudge(players);

        // Inizialize all players.
        players.forEach(elem ->
                elem.getRef().tell(
                        new StartMsg(
                                length,
                                players,
                                elem,
                                view),
                        getSelf()));
    }
}