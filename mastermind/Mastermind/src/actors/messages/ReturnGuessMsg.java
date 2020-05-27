package actors.messages;

import java.util.ArrayList;

public final class ReturnGuessMsg {
    private final ArrayList<Integer> sequence;

    public ReturnGuessMsg(ArrayList<Integer> sequence) {
        this.sequence = sequence;
    }

    public ArrayList<Integer> getSequence() {
        return this.sequence;
    }
}
