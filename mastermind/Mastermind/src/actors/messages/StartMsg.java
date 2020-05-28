package actors.messages;

import info.PlayerInfo;
import views.players.PlayersView;

import java.util.ArrayList;

public class StartMsg implements Message {
    private final int length;
    public String name;
    private final ArrayList<PlayerInfo> allPlayers;
    private final PlayerInfo player;
    private PlayersView view;

    public StartMsg(int length, ArrayList<PlayerInfo> players, String name, PlayerInfo player, PlayersView view)
    {
        this.length = length;
        this.allPlayers = players;
        this.name = name;
        this.player = player;
        this.view = view;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<PlayerInfo> getAllPlayers() {
        return allPlayers;
    }

    public PlayerInfo getPlayer() { return player; }

    public String getName() { return this.name; }

    public PlayersView getView(){ return  this.view; }
}
