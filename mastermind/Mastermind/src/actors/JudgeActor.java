package actors;

import actors.messages.ReadyMsg;
import actors.messages.StartGameMsg;
import actors.messages.StartMsg;
import actors.messages.StartTurn;
import akka.actor.ActorContext;
import info.PlayerInfo;
import views.player.PlayersView;

import java.util.*;

public class JudgeActor extends MastermindActorImpl {
    private PlayersView view;
    private final List<PlayerInfo> players;
    private List<PlayerInfo> playersTemporary;
    private ArrayList<Integer> order;
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
                    int number = getreadyNmex();
                    while (number < players.size()) {
                        //Thread.sleep(1);
                        log("wait");
                    }
                    //crea un ordine per i turni // Quando sono tutti pronti
                    // TODO CONTATORI, ORDINE CASUALE E INVIO TENTATIVO
                    this.playersTemporary = this.players;
                    // TODO ORDINE RANDOM DEI PLAYERS --> sequenceInfoJudge
                    // lista che dovrebbe essere random
                    this.playersTemporary.forEach(elem ->
                            elem.getReference().tell(
                                    new StartTurn(msg.getPlayers(), msg.getLength()),
                                    getSelf()));



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
                    new PlayerInfo("player_" + i, this.getContext(), players);
            this.players.add(player);
            this.view.addPlayer("player_"+i, //lista di interi);
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
    public PlayerInfo getRandomElement(List<PlayerInfo> list)
    {
        Random rand = new Random();
        PlayerInfo number = list.get(rand.nextInt(list.size()));
        list.remove(number);
        return number;
    }
}