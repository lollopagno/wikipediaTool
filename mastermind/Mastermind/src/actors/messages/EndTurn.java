package actors.messages;

public class EndTurn implements Message {
    final boolean win;

    public EndTurn() {
        this(false);
    }

    public EndTurn(boolean win) {
        this.win = win;
    }
}