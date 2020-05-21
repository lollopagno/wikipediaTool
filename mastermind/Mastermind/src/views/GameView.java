package views;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class GameView extends JFrame implements ItemListener, MyView {
    public GameView(int nplayers){
        this.setTitle("Game");
        this.setSize(800,600);

        JComboBox<String> players = new JComboBox<>();
        players.addItemListener(this);
        for(int i = 0; i < nplayers; i++){
            players.addItem("Player " + i);
        }
        this.getContentPane().add(players);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        System.out.println(e.getItem());
    }

    @Override
    public void playerReady(String player) {
        SwingUtilities.invokeLater(() -> System.out.println(player));
    }

    @Override
    public void solutionUpdated(String from, String to, ArrayList<Integer> sequence) {
        SwingUtilities.invokeLater(() -> System.out.println(from));
    }

    @Override
    public void solutionFound(String from, String to) {
        SwingUtilities.invokeLater(() -> System.out.println(from));
    }

    @Override
    public void playerWin(String player) {
        SwingUtilities.invokeLater(() -> System.out.println(player));
    }
}
