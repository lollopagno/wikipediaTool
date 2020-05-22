package views;

import views.components.PlayerSolution;

import java.util.ArrayList;

public interface MyView {
    /**
     * Add a new registered player to the view.
     * Show the name and the generated sequence.
     * @param player Registered player.
     */
    void playerReady(String player, ArrayList<Integer> sequence);

    void solutionUpdated(String from, PlayerSolution solution);

    void solutionFound(String from, String to);

    void playerWin(String player);
}
