package actors.messages;

import model.SequenceInfoGuess;

public final class ReturnGuessMsg implements Message {
    private final SequenceInfoGuess sequence;

    public ReturnGuessMsg(SequenceInfoGuess sequence) {
        this.sequence = sequence;
    }

    public SequenceInfoGuess getSequence() {
        return this.sequence;
    }
}
