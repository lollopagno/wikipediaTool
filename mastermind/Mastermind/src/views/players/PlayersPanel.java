package views.players;

import model.Sequence;
import model.SequenceInfoGuess;
import views.players.player.PlayerPanel;
import views.players.player.PlayerView;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
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
        // this.panel.setPreferredSize(new Dimension(599, 1600));
        JScrollPane pane = new JScrollPane(this.panel);
        this.add(pane);
    }

    @Override
    public void addPlayer(String player, Sequence sequence) {
        PlayerView view = new PlayerPanel(player, sequence);
        this.players.add(view);
        this.panel.add((Component) view, BorderLayout.CENTER);
        this.panel.updateUI();
    }

    @Override
    public void inputSolution(String from, String to, SequenceInfoGuess sequence) {
        Optional<PlayerView> view = this.getPlayerViewByName(from);
        view.ifPresent(v -> v.inputSolution(to, sequence));
        this.updateUI();
        this.panel.updateUI();
    }

    @Override
    public void playerWin(String player) {
        System.out.println("Stop all!!! A player win!!!");
    }

    /**
     * Filtra il set dei players.
     *
     * @param name Nome del giocatore da scegliere
     * @return Player scelto.
     */
    private Optional<PlayerView> getPlayerViewByName(String name) {
        return this.players.stream()
                .filter(f -> f.getName().equals(name))
                .findFirst();
    }
}