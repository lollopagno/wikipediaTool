package views;

import views.components.PlayerSolution;

import java.util.ArrayList;

public interface MyView {
    /**
     * Aggiunge alla view un nuovo player che si è registrato
     * con successo presso un arbitro con nome e sequenza.
     * @param player Player che si è registrato.
     */
    void playerReady(String player, ArrayList<Integer> sequence);

    void solutionUpdated(String from, PlayerSolution solution);

    void solutionFound(String from, String to);

    void playerWin(String player);
}
