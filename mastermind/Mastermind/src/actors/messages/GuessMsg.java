package actors.messages;

import model.Sequence;

public final class GuessMsg implements Message {

    private final Sequence sequence;

    public GuessMsg(Sequence sequence) {
        this.sequence = sequence;
    }

    public Sequence getSequence() {
        return this.sequence;
    }

}
