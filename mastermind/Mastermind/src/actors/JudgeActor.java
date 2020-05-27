package actors;

import actors.messages.ReadyMsg;
import actors.messages.StartGameMsg;
import actors.messages.StartMsg;
import actors.messages.StartTurn;
import model.SequenceInfoJudge;
import info.PlayerInfo;
import views.players.PlayersView;

import java.util.*;

public class JudgeActor extends MastermindActorImpl {
    private SequenceInfoJudge sequenceInfoJudge;
    private PlayersView view;
    private final List<PlayerInfo> players;
    private int readyMex = 0;

    public JudgeActor() {
        this.players = new ArrayList<PlayerInfo>();
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.log("Arbitro creato");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartGameMsg.class, msg -> {  // startGame che arriva dalla view
                    this.log("Judge START GAME Received:");
                    this.view = msg.getView();
                    this.startGame(msg.getPlayers(), msg.getLength());
                }).match(ReadyMsg.class, msg -> { // ready che arriva dai giocatori
                    //contatore per tutti i messaggi di ready // Aspettare tutti i player
                    this.log("Judge Ready Message Received:");
                    // TODO CORRETTO ASPETTARE TUTTI I MESSAGGI DI READY?
                    int number = getreadyNmex();
                    while (number < players.size()) {
                        //Thread.sleep(1);
                        log("wait");
                    }
                    //crea un ordine per i turni // Quando sono tutti pronti
                    // TODO ORDINE RANDOM DEI PLAYERS --> sequenceInfoJudge
                    // lista che dovrebbe essere random
                    this.sequenceInfoJudge.newOrderTurn();
                    for(int i = 0; i < players.size(); i++){
                        this.sequenceInfoJudge.getNextPlayers(i);
                        // TODO INVIO A UN ALTRO PLAYER -> COME?
                        getSelf().tell(new StartTurn(msg.getPlayers(), msg.getLength()),
                                getSelf());
                    }

                    //invio tentativo // startTurn // Generi sequenza e invii messaggio al primo
                    // TODO END_TURN
                    // Invia il messaggio all'attore successivo.
                })
                .build();
    }

    private void startGame(int players, int length) {
        for (int i = 0; i < players; i++) {
            PlayerInfo player =
                    new PlayerInfo("player_" + i, this.getContext(), players);
            this.players.add(player);
            // TODO: Generare tutti gli altri players.
            //this.view.addPlayer("player_"+i, );
        }

        this.players.forEach(elem ->
                //invia messaggio agli attori con lunghezza  e tutti i giocatori
                //getSelf è chi lo invia..
                elem.getReference().tell(
                        new StartMsg(length, this.players),
                        getSelf()));

    }

    private int getreadyNmex() {
        return readyMex++;
    }

}