package actors;

import actors.messages.StartGameMsg;
import actors.messages.StartMsg;
import info.PlayerInfo;
import views.MyView;

import java.util.LinkedList;
import java.util.List;

public class JudgeActor extends MastermindActorImpl {
    private MyView view;
    private final List<PlayerInfo> players;

    public JudgeActor(){
        this.players = new LinkedList<>();
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.log("Arbitro creato");
    }

    @Override
    public Receive createReceive() {
        // TODO Crea la receive per i messaggi
        // TODO READY
        // Aspettare tutti i player
        // Quando sono tutti pronti
        // Generi sequenza e invii messaggio al primo
        // TODO END_TURN
        // Invia il messaggio all'attore successivo.
        return receiveBuilder().match(StartGameMsg.class, msg -> {
            this.log("Judge START GAME Received:");
            this.startGame(msg.getPlayers(), msg.getLength());
        }).build();
    }

    private void startGame(int players, int length) {
        // TODO: Generare tutti gli altri players.
        for(int i = 0; i < players; i++) {
            PlayerInfo player =
                    new PlayerInfo("player_" + i, this.getContext());
            this.players.add(player);
        }

        this.players.forEach(elem ->
                elem.getReference().tell(
                        new StartMsg(length, this.players),
                        getSelf()));
    }
}
