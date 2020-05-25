package views.player;

import model.Sequence;

import javax.swing.*;

public class SolutionDetailPanel extends JPanel {
    private final JLabel name, sequence, otherInfo;

    public SolutionDetailPanel() {
        this.name = new JLabel("Nome");
        this.add(name);

        this.sequence = new JLabel("Sequence");
        this.add(sequence);

        this.otherInfo = new JLabel("Other info");
        this.add(this.otherInfo);
    }

    public String getName() {
        return this.name.getText();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setSequence(Sequence sequence) {
        this.sequence.setText(sequence.toString());
        System.out.println("Impostato il valore a " + sequence.toString());
        this.updateUI();
    }

    public void setInfo(int rightPlacedNumbers, int rightNumbers) {
        this.otherInfo.setText(String.format("%d - %d", rightPlacedNumbers, rightNumbers));
        this.updateUI();
    }
}