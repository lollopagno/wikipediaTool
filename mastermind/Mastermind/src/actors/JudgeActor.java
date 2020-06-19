package actors;

import actors.messages.*;
import model.PlayerReference;
import model.SequenceInfoJudge;
import views.players.PlayersView;

import java.util.ArrayList;
import java.util.List;

public class JudgeActor extends MastermindActorImpl {
    // TODO: Valutare se spostare tutto dentro a questa entità.
    private SequenceInfoJudge sequenceInfoJudge;
    private PlayersView view;
    private int allReadyMsg = 0;
    private int timeBetweenTurns = 0;

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
                    this.startGame(msg.getPlayers(), msg.getLength());
                })
                .match(ReadyMsg.class, msg -> {
                    // Msg dai player di READY
                    this.allReadyMsg++;
                    String senderName = getSender().path().name();
                    // this.log("READY MESSAGE Received by " + senderName);
                    // this.log("Ready players -> [" + this.allReadyMsg + "/" + this.sequenceInfoJudge.getNPlayers() + "]");

                    if (this.allReadyMsg == this.sequenceInfoJudge.getNPlayers()) {
                        // Set the new player order.
                        this.sequenceInfoJudge.newOrderTurn();
                        this.log("Judge set new Order Turn: " + this.sequenceInfoJudge.showTurn());
                        // Default Value
                        this.allReadyMsg = 0;

                        // Wake up a new player.
                        wakeUpNextPlayer();
                    }
                })
                .match(EndTurn.class, msg -> {
                    // Msg dal player di endTurn (è terminato SOLO il suo turno)
                    this.allReadyMsg++;
                    if (this.allReadyMsg == this.sequenceInfoJudge.getNPlayers()) {
                        // Set the new player order.
                        this.sequenceInfoJudge.newOrderTurn();
                        this.log("Judge set new Order Turn: " + this.sequenceInfoJudge.showTurn());
                        // Default Value
                        this.allReadyMsg = 0;
                    }

                    waitTime();
                    // Wake up a new player.
                    wakeUpNextPlayer();
                }).match(PlayerWin.class, msg -> {
                    // Vittoria di un giocatore
                    String message = msg.getPlayerWinn().getName() + " has won!";
                    this.log(message);
                    for (PlayerReference player : this.sequenceInfoJudge.showPlayer()) {
                        player.getRef().tell(new EndGame(), getSelf());
                    }
                    view.showMessage(message);
                }).build();
    }

    /**
     * Send the msg start turn to the next player.
     */
    private void wakeUpNextPlayer() {
        PlayerReference nextPlayer = this.sequenceInfoJudge.getNextPlayer();
        nextPlayer.getRef().tell(new StartTurn(), getSelf());
        // this.log("Judge sent START TURN MSG at player: " + nextPlayer.getName());
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
        {
            elem.getRef().tell(
                    new StartMsg(
                            length,
                            players,
                            elem,
                            view),
                    getSelf());
            waitTime();
        });
    }

    /**
     * Wait the necessary time between turns.
     */
    void waitTime() {
        try {
            Thread.sleep(this.timeBetweenTurns);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}