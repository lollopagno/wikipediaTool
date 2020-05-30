package actors;

import actors.messages.*;
import model.SequenceInfoJudge;
import info.PlayerInfo;
import views.players.PlayersView;

import java.util.*;

public class JudgeActor extends MastermindActorImpl {
    // TODO: Valutare se spostare tutto dentro a questa entità.
    private SequenceInfoJudge sequenceInfoJudge;
    private PlayersView view;
    private int allReadyMsg = 0;
    private int currentIndexTurn = 0;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.log("Judge created!");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()

                // StartGameMsg inviato dalla view
                .match(StartGameMsg.class, msg -> {
                    this.log("Judge START GAME Received");
                    this.view = msg.getView();
                    this.startGame(msg.getPlayers(), msg.getLength());

                    // Msg dai player di READY
                })
                .match(ReadyMsg.class, msg -> {
                    this.allReadyMsg++;
                    this.log("READY MESSAGE Received by " + msg.getPlayerName() + " [" + this.allReadyMsg + "/" + this.sequenceInfoJudge.getNPlayers() + "]");

                    if (this.allReadyMsg == this.sequenceInfoJudge.getNPlayers()) {
                        // Set the new player order.
                        this.sequenceInfoJudge.newOrderTurn();
                        this.log("Judge set new Order Turn: " + this.sequenceInfoJudge.showTurn());

                        // Wake up a new player.
                        wakeUpNextPlayer();
                    }

                    // Msg dal player di endTurn (è terminato SOLO il suo turno)
                })
                .match(EndTurn.class, msg -> {
                    // Check if need to start a new turn.
                    if (currentIndexTurn == this.sequenceInfoJudge.getNPlayers()) {
                        this.currentIndexTurn = 0;
                        this.sequenceInfoJudge.newOrderTurn();
                    }

                    // Wake up a new player.
                    wakeUpNextPlayer();

                }).build();
    }

    /**
     * Send the msg start turn to the next player.
     */
    private void wakeUpNextPlayer() {
        PlayerInfo nextPlayer = this.sequenceInfoJudge.getNextPlayer(this.currentIndexTurn);
        nextPlayer.getReference().tell(new StartTurn(), getSelf());
        this.log("Judge sent START TURN MSG at player: " + nextPlayer.getName());
        this.currentIndexTurn++;
    }

    /**
     * Create players at the start of game.
     * @param nPlayers Total player number.
     * @param length Sequence length.
     */
    private void startGame(int nPlayers, int length) {
        final List<PlayerInfo> players = new ArrayList<>();
        for (int i = 0; i < nPlayers; i++) {
            PlayerInfo player = new PlayerInfo("player_" + i, this.getContext(), nPlayers);
            players.add(player);
        }

        // Create the judge instance to manage all turns between players.
        this.sequenceInfoJudge = new SequenceInfoJudge(players);

        // Inizialize all players.
        players.forEach(elem ->
                elem.getReference().tell(new StartMsg(length, players, elem.getName(), elem, view, getSelf()), getSelf()));
    }
}