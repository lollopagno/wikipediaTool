package actors.messages;

import info.PlayerInfo;

import java.util.List;

public class ReadyMsg {
    private final List<PlayerInfo> players;
    private final int length;

    public ReadyMsg(List<PlayerInfo> players, int length) {
        this.players = players;
        this.length= length;
    }

    public int getLength() {
        return this.length;
    }

    public List<PlayerInfo> getPlayers() {
        return this.players;
    }
}
