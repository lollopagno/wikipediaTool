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
        this.log("Arbitro creato");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartGameMsg.class, msg -> {
                    // Msg dalla View
                    this.log("Judge START GAME Received");
                    this.view = msg.getView();
                    this.startGame(msg.getPlayers(), msg.getLength());
                }).match(ReadyMsg.class, msg -> {
                    // Msg dai player di READY
                    this.log("Judge" +  msg.getPlayerName() + " Ready Message Received");

                    this.allReadyMsg ++;

                    if (this.allReadyMsg == players.size()) {

                        // Setto il nuovo ordine del turno corrente turno
                        this.sequenceInfoJudge.newOrderTurn();

                        // Do la parola al giocatore in base all'ordine
                        PlayerInfo currentPlayer = this.sequenceInfoJudge.getNextPlayers(this.currentIndexTurn);
                        currentPlayer.getReference().tell(new StartTurn(), getSelf());

                        this.currentIndexTurn++;
                    }

                }).match(EndTurn.class, msg-> {
                    // Msg dal player di endTurn (è terminato SOLO il suo turno)
                    // Inizio un nuovo turno
                    if (currentIndexTurn == this.players.size()){

                        this.currentIndexTurn = 0;
                        this.sequenceInfoJudge.newOrderTurn();
                    }

                    PlayerInfo currentPlayer = this.sequenceInfoJudge.getNextPlayers(this.currentIndexTurn);
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
                elem.getReference().tell( new StartMsg(length, this.players, elem.getName(), elem, view), getSelf()));
    }
}