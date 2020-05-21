package actors.messages;

import info.PlayerInfo;

import java.util.List;

public class StartMsg implements Message {
    private final int length;
    private final List<PlayerInfo> players;

    public StartMsg(int length, List<PlayerInfo> players)
    {
        this.length = length;
        this.players = players;
    }

    public int getLength() {
        return length;
    }

    public List<PlayerInfo> getPlayers() {
        return players;
    }
}
