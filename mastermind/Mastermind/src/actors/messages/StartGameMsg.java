package actors.messages;

import views.players.PlayersView;

public class StartGameMsg implements Message {
    private final int length, players, time;
    private final PlayersView view;

    public StartGameMsg(int length, int players, int time, PlayersView view)
    {
        this.length = length;
        this.players = players;
        this.time = time;
        this.view = view;
    }

    public int getLength() {
        return this.length;
    }

    public int getPlayers() {
        return this.players;
    }

    public int getTime() {
        return this.time;
    }

    public PlayersView getView() {
        return this.view;
    }
}
