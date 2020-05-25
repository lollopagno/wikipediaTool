package views.player;

import model.SequenceInfoGuess;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The single player panel view implementation.
 */
public class PlayerPanel extends JPanel implements PlayerView {
    private final String name;
    private final JPanel solutionsPanel;
    private final Set<SolutionDetailPanel> solutions;

    public PlayerPanel(String name, List<Integer> sequence) {
        this.name = name;
        this.add(new JLabel(name));
        Optional<String> value = sequence.stream()
                .map(Object::toString)
                .reduce(String::concat);
        value.ifPresent(v -> this.add(new JLabel(v)));

        this.solutions = new HashSet<>();
        this.solutionsPanel = new JPanel();
        this.add(this.solutionsPanel);

        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void inputSolution(String to, SequenceInfoGuess sequence) {
        Optional<SolutionDetailPanel> panel = this.solutions.stream().filter(f -> f.getName().equals(to)).findFirst();
        if (panel.isPresent()) {
            panel.ifPresent(i -> {
                i.setSequence(sequence.getNumbers());
                i.setInfo(sequence.getRightPlaceNumbers(), sequence.getRightNumbers());
                i.updateUI();
            });
        } else {
            SolutionDetailPanel newPanel = new SolutionDetailPanel();
            newPanel.setName(to);
            newPanel.setSequence(sequence.getNumbers());
            newPanel.setInfo(sequence.getRightPlaceNumbers(), sequence.getRightNumbers());
            // TODO: Deve essere aggiunta anche ad un pannello visualizzabile.
            this.solutions.add(newPanel);
            this.solutionsPanel.add(newPanel);
        }
        this.solutionsPanel.updateUI();
        // this.updateUI();
    }
}