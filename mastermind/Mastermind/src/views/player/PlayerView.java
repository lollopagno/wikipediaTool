package views.player;

import model.SequenceInfo;

/**
 * The single player view interface.
 */
public interface PlayerView {
    String getName();

    /**
     * Show the guess made to another player.
     *
     * @param to       Guess Player Target.
     * @param sequence Guess Tried.
     */
    void inputSolution(String to, SequenceInfo sequence);
}