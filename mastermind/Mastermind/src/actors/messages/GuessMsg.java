package actors.messages;

import java.util.ArrayList;

public final class GuessMsg {
    private final ArrayList<Integer> sequence;

    public GuessMsg(ArrayList<Integer> sequence) {
        this.sequence = sequence;
    }

    public ArrayList<Integer> getSequence() {
        return this.sequence;
    }
}
