package views.players;

import model.Sequence;
import model.SequenceInfoGuess;

import java.util.ArrayList;

public interface PlayersView {
    /**
     * Add a new registered player to the view.
     * Show the name and the generated sequence.
     *
     * @param player Registered player.
     */
    void addPlayer(String player, ArrayList<Integer> sequence);

    /**
     * Show the guess made by a player to another.
     *
     * @param from     Player that tried the guess.
     * @param to       Player that received the guess.
     * @param sequence Sequence info tried.
     */
    void inputSolution(String from, String to, SequenceInfoGuess sequence);

    /**
     * Show when a player win.
     *
     * @param player Player that had win.
     */
    void playerWin(String player);
}