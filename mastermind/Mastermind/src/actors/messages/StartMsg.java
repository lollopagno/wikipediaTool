package actors.messages;

import info.PlayerInfo;

import java.util.ArrayList;

public class StartMsg implements Message {
    private final int length;
    public String name;
    private final ArrayList<PlayerInfo> allPlayers;
    private final PlayerInfo player;

    public StartMsg(int length, ArrayList<PlayerInfo> players, String name, PlayerInfo player)
    {
        this.length = length;
        this.allPlayers = players;
        this.name = name;
        this.player = player;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<PlayerInfo> getAllPlayers() {
        return allPlayers;
    }

    public PlayerInfo getPlayer() { return player; }

    public String getName() { return this.name; }
}
