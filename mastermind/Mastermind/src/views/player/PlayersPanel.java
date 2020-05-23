package views.player;

import model.SequenceInfo;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Panel container of all other player details panels.
 */
public class PlayersPanel extends JPanel implements PlayersView {
    private final Set<PlayerView> players;
    private final JPanel panel;

    public PlayersPanel() {
        this.players = new HashSet<>();
        this.panel = new JPanel();
        this.panel.setPreferredSize(new Dimension(630, 480));
        JScrollPane pane = new JScrollPane(this.panel);
        this.add(pane);
    }

    @Override
    public void addPlayer(String player, List<Integer> sequence) {
        PlayerView view = new PlayerPanel(player, sequence);
        this.players.add(view);
        this.panel.add((Component) view, BorderLayout.CENTER);
        this.panel.updateUI();
    }

    @Override
    public void inputSolution(String from, String to, SequenceInfo sequence) {
        Optional<PlayerView> view = this.getPlayerViewByName(from);
        view.ifPresent(v -> v.inputSolution(to, sequence));
    }

    @Override
    public void playerWin(String player) {
        System.out.println("Stop all!!! A player win!!!");
    }

    /**
     * Filtra il set dei players.
     * @param name Nome del giocatore da scegliere
     * @return Player scelto.
     */
    private Optional<PlayerView> getPlayerViewByName(String name) {
        return this.players.stream()
                .filter(f -> f.getName().equals(name))
                .findFirst();
    }
}
