package actors.messages;

import info.PlayerInfo;

import java.util.List;

public class ReadyMsg implements Message {
    private final List<PlayerInfo> players;
    private final String name;

    public ReadyMsg(List<PlayerInfo> players, String name) {
        this.name = name;
        this.players = players;
    }

    public List<PlayerInfo> getPlayers() {
        return this.players;
    }
    public String getPlayerName() {return this.name;  }
}
