package actors.messages;

import info.PlayerInfo;

import java.util.List;

public class ReadyMsg implements Message {
    private final List<PlayerInfo> players;

    public ReadyMsg(List<PlayerInfo> players) {
        this.players = players;
    }

    public List<PlayerInfo> getPlayers() {
        return this.players;
    }
}