package actors.messages;

import model.SequenceInfoGuess;

import java.util.ArrayList;

public final class ReturnGuessMsg {
    private final SequenceInfoGuess sequence;

    public ReturnGuessMsg(SequenceInfoGuess sequence) {
        this.sequence = sequence;
    }

    public SequenceInfoGuess getSequence() {
        return this.sequence;
    }
}
