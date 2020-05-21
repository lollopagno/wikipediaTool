package actors.messages;

public class EndTurn {
    private boolean sequenceFound;

    public EndTurn(boolean sequenceFound){
        this.sequenceFound = sequenceFound;
    }

    public boolean getSequenceFound() {
        return this.sequenceFound;
    }
}
