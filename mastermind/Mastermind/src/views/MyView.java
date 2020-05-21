package views;

import java.util.ArrayList;

public interface MyView {
    void playerReady(String player);

    void solutionUpdated(String from, String to, ArrayList<Integer> sequence);

    void solutionFound(String from, String to);

    void playerWin(String player);
}
