package actors.messages;

public class StartMsg {
    private int length;
    private int players;

    public StartMsg(int length, int players)
    {
        this.length = length;
        this.players = players;
    }

    public int getLength() {
        return length;
    }

    public int getPlayers() {
        return players;
    }
}
