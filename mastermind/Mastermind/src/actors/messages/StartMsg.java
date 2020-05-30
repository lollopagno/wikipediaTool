package actors.messages;

import akka.actor.ActorRef;
import info.PlayerInfo;
import views.players.PlayersView;

import java.util.List;

public class StartMsg implements Message {
    private final int length;
    public String name;
    private final List<PlayerInfo> allPlayers;
    private final PlayerInfo player;
    private final ActorRef judgeActor;
    private final PlayersView view;

    public StartMsg(int length, List<PlayerInfo> players, String name, PlayerInfo player, PlayersView view, ActorRef judgeActor)
    {
        this.length = length;
        this.allPlayers = players;
        this.name = name;
        this.player = player;
        this.view = view;
        this.judgeActor = judgeActor;
    }

    public int getLength() {
        return length;
    }

    public List<PlayerInfo> getAllPlayers() {
        return allPlayers;
    }

    public PlayerInfo getPlayer() { return player; }

    public String getName() { return this.name; }

    public PlayersView getView(){ return  this.view; }

    public ActorRef getJudge() { return judgeActor; }
}
