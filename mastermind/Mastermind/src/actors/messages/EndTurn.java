package actors.messages;

public class EndTurn implements Message {
    boolean win;

    public EndTurn() {
        this(false);
    }

    public EndTurn(boolean win) {
        this.win = win;
    }

    public boolean hasPlayerWin() {
        return this.win;
    }
}