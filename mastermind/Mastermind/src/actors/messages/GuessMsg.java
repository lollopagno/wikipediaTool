package actors.messages;

import info.PlayerInfo;
import model.Sequence;
import model.SequenceImpl;

import java.util.ArrayList;

public final class GuessMsg implements Message {

    private final Sequence sequence;
    private PlayerInfo player;

    public GuessMsg(Sequence sequence, PlayerInfo player) {
        this.sequence = sequence;
        this.player = player;
    }

    public Sequence getSequence() {
        return this.sequence;
    }

    public PlayerInfo getPlayer() {
        return this.player;
    }

}
