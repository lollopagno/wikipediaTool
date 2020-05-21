package actors.messages;

public class StartGameMsg {
    private int length;
    private int players;

    public StartGameMsg(int length, int players)
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
