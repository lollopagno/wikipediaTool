package actors;

import actors.messages.*;
import model.SequenceInfoJudge;
import info.PlayerInfo;
import views.players.PlayersView;

import java.util.*;

public class JudgeActor extends MastermindActorImpl {
    private SequenceInfoJudge sequenceInfoJudge;
    private PlayersView view;
    private final ArrayList<PlayerInfo> players;
    private int allReadyMsg = 0;
    private int currentIndexTurn = 0;

    public JudgeActor() {
        this.players = new ArrayList<>();
    }

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
                    this.log("READY MESSAGE Received by " + msg.getPlayerName());

                    this.allReadyMsg++;

                    if (this.allReadyMsg == players.size()) {

                        // Setto il nuovo ordine del turno corrente turno
                        this.sequenceInfoJudge.newOrderTurn();

                        this.log("Judge set new Order Turn: " + this.sequenceInfoJudge.showTurn());

                        // Do la parola al giocatore in base all'ordine
                        PlayerInfo nextPlayer = this.sequenceInfoJudge.getNextPlayer(this.currentIndexTurn);
                        nextPlayer.getReference().tell(new StartTurn(), getSelf());

                        this.currentIndexTurn++;
                    }

                    // Msg dal player di endTurn (è terminato SOLO il suo turno)
                })
                .match(EndTurn.class, msg -> {

                    // Check if need to start a new turn.
                    if (currentIndexTurn == this.players.size()) {

                        this.currentIndexTurn = 0;
                        this.sequenceInfoJudge.newOrderTurn();
                    }

                    PlayerInfo currentPlayer = this.sequenceInfoJudge.getNextPlayer(this.currentIndexTurn);
                    log("currentPlayer " + currentPlayer);
                    this.log("Judge send MSG at next players: " + currentPlayer.getName());
                    currentPlayer.getReference().tell(new StartTurn(), getSelf());

                    this.currentIndexTurn++;

                }).build();
    }

    // Crea i player all'inzio del gioco
    private void startGame(int players, int length) {

        for (int i = 0; i < players; i++) {
            PlayerInfo player = new PlayerInfo("player_" + i, this.getContext(), players);
            this.players.add(player);
        }

        // Creo l'istanza per gestire ogni turno di una partita
        this.sequenceInfoJudge = new SequenceInfoJudge(this.players);

        this.players.forEach(elem ->
                elem.getReference().tell(new StartMsg(length, this.players, elem.getName(), elem, view, getSelf()), getSelf()));
    }
}