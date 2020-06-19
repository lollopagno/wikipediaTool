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
    private final JLabel info;

    public PlayersPanel() {
        this.players = new HashSet<>();
        this.panel = new JPanel();
        this.info = new JLabel("Info label");
        this.panel.add(this.info);
        this.panel.setPreferredSize(new Dimension(599, 1600));
        JScrollPane pane = new JScrollPane(this.panel);
        this.add(pane);
    }

    @Override
    public void addPlayer(String player, Sequence sequence) {
        PlayerView view = new PlayerPanel(player, sequence);
        this.players.add(view);
        this.panel.add((Component) view, BorderLayout.CENTER);
        this.panel.repaint();
        this.panel.revalidate();
    }

    @Override
    public void inputSolution(String from, String to, SequenceInfoGuess sequence) {
        Optional<PlayerView> view = this.getPlayerViewByName(from);
        view.ifPresent(v -> v.inputSolution(to, sequence));
        this.panel.repaint();
        this.panel.revalidate();
        this.repaint();
        this.revalidate();
    }

    @Override
    // Non funzionante
    public void refresh(){

        System.out.println("Refresh view");
        this.players.clear();
        this.repaint();
        this.revalidate();
    }

    @Override
    public void showMessage(String message) {
        SwingUtilities.invokeLater(() -> this.info.setText("Info: " + message));
    }

    @Override
    public void playerSolved(String from, String to) {
        this.getPlayerViewByName(from).ifPresent(p -> p.showSolved(to));
        SwingUtilities.invokeLater(() -> this.info.setText(String.format("Player %s solved %s", from, to)));
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