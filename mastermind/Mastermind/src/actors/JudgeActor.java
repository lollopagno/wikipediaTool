package actors;

import actors.messages.ReadyMsg;
import actors.messages.StartGameMsg;
import actors.messages.StartMsg;
import info.PlayerInfo;
import views.MyView;

import java.util.LinkedList;
import java.util.List;

public class JudgeActor extends MastermindActorImpl {
    private MyView view;
    private final List<PlayerInfo> players;
    private int readyMex = 0;

    public JudgeActor() {
        this.players = new LinkedList<>();
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.log("Arbitro creato");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartGameMsg.class, msg -> {  // startGame che arriva dalla wiew
                    this.log("Judge START GAME Received:");
                    this.startGame(msg.getPlayers(), msg.getLength());
                }).match(ReadyMsg.class, msg -> { // ready che arriva dai giocatori
                    //contatore per tutti i messaggi di ready // Aspettare tutti i player
                    int number = getreadyNmex();
                    while (number < players.size()) {
                        //Thread.sleep(1);
                        log("wait");
                    }
                    //crea un ordine per i turni // Quando sono tutti pronti
                    // TODO CONTATORI, ORDINE CASUALE E INVIO TENTATIVO
                    //invio tentativo // startTurn // Generi sequenza e invii messaggio al primo
                    // TODO END_TURN
                    // Invia il messaggio all'attore successivo.
                })
                .build();
    }

    private void startGame(int players, int length) {
        // TODO: Generare tutti gli altri players.
        for (int i = 0; i < players; i++) {
            PlayerInfo player =
                    new PlayerInfo("player_" + i, this.getContext());
            this.players.add(player);
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