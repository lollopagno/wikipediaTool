package actors.messages;

import info.PlayerInfo;

import java.util.List;

public class StartTurn implements Message {
    private final int length;
    private final List<PlayerInfo> players;

    public StartTurn(int length, List<PlayerInfo> players)
    {
        this.length = length;
        this.players = players;
    }

    public int getLength() {
        return this.length;
    }


    public List<PlayerInfo> getPlayers() {
        return players;
    }
}
