package actors.messages;

public class EndTurn implements Message {
    private final boolean sequenceFound;

    public EndTurn(boolean sequenceFound){
        this.sequenceFound = sequenceFound;
    }

    public boolean getSequenceFound() {
        return this.sequenceFound;
    }
}
