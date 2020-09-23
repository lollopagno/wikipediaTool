package views.players.player;

import model.Sequence;
import model.SequenceInfoGuess;
import views.players.player.solutions.SolutionDetailPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The single player panel view implementation.
 */
public class PlayerPanel extends JPanel implements PlayerView {
    private final String name;
    private final JPanel solutionsPanel;
    private final Set<SolutionDetailPanel> solutions;

    public PlayerPanel(String name, Sequence sequence) {
        this.name = name;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel namePanel = new JPanel();
        JLabel nameTip = new JLabel("Name:");
        JLabel nameLabel = new JLabel(name);
        namePanel.add(nameTip, BorderLayout.LINE_START);
        namePanel.add(nameLabel, BorderLayout.LINE_END);
        this.add(namePanel);

        JPanel sequencePanel = new JPanel();
        JLabel sequenceTip = new JLabel("Sequence:");
        JLabel sequenceLabel = new JLabel(sequence.toString());
        sequencePanel.add(sequenceTip, BorderLayout.LINE_START);
        sequencePanel.add(sequenceLabel, BorderLayout.LINE_END);
        this.add(sequencePanel);

        this.solutions = new HashSet<>();
        this.solutionsPanel = new JPanel();
        this.solutionsPanel.setLayout(new BoxLayout(this.solutionsPanel, BoxLayout.PAGE_AXIS));
        JScrollPane pane = new JScrollPane(this.solutionsPanel);
        this.add(pane);

        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void inputSolution(String to, SequenceInfoGuess sequence) {
        Optional<SolutionDetailPanel> panel =
                this.solutions.stream()
                        .filter(f -> f.getName().equals(to))
                        .findFirst();
        if (panel.isPresent()) {
            SolutionDetailPanel sol = panel.get();
            sol.setSequence(sequence.getNumbers());
            sol.setInfo(sequence.getRightPlaceNumbers(), sequence.getRightNumbers());
            SwingUtilities.invokeLater(() -> {
                sol.repaint();
                sol.revalidate();
            });
        } else {
            SolutionDetailPanel newPanel = new SolutionDetailPanel();
            newPanel.setName(to);
            newPanel.setSequence(sequence.getNumbers());
            newPanel.setInfo(sequence.getRightPlaceNumbers(), sequence.getRightNumbers());
            this.solutions.add(newPanel);
            this.solutionsPanel.add(newPanel);
            this.repaint();
            this.revalidate();
        }
    }

    @Override
    public void showSolved(String to) {
        this.solutions.stream()
                .filter(f -> f.getName().equals(to))
                .findFirst()
                .ifPresent(SolutionDetailPanel::showSolved);
    }
}