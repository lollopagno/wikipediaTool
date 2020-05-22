package actors.messages;

public class StartGameMsg {
    private final int length, players, time;

    public StartGameMsg(int length, int players, int time)
    {
        this.length = length;
        this.players = players;
        this.time = time;
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
}
