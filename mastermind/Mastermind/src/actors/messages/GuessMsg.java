package actors.messages;

import info.PlayerInfo;
import model.Sequence;
import model.SequenceImpl;

import java.util.ArrayList;

public final class GuessMsg implements Message {

    private final Sequence sequence;

    public GuessMsg(Sequence sequence) {
        this.sequence = sequence;
    }

    public Sequence getSequence() {
        return this.sequence;
    }

}
