package actors.messages;

import info.PlayerInfo;

import java.util.List;

public class StartMsg implements Message {
    private final int length;
    public String name;
    private final List<PlayerInfo> players;

    public StartMsg(int length, List<PlayerInfo> players, String name)
    {
        this.length = length;
        this.players = players;
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public List<PlayerInfo> getPlayers() {
        return players;
    }

    public String getName() { return this.name; }
}
