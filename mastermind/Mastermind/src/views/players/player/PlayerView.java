package views.players.player;

import model.SequenceInfoGuess;

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
    void inputSolution(String to, SequenceInfoGuess sequence);

    /**
     * Show the player to solved.
     * @param to Player solved.
     */
    void showSolved(String to);
}