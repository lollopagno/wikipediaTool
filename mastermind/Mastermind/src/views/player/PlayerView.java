package views.player;

import views.components.PlayerSolution;
import views.components.SequenceSolutions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlayerView extends JPanel {
    private final JLabel name;
    private final JLabel sequence;
    private final JLabel operation;
    private final SequenceSolutions solutions;

    public PlayerView(String name) {
        this.name = new JLabel(name);
        this.add(this.name);

        this.sequence = new JLabel("");
        this.add(this.sequence);

        this.operation = new JLabel("operation");
        this.add(this.operation);

        this.solutions = new SequenceSolutions();
        this.add(this.solutions);

        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public String getName(){
        return this.name.getText();
    }

    public void setSequence(ArrayList<Integer> sequence){
        this.sequence.setText(sequence.toString());
    }

    public void setOperation(String operation){
        this.operation.setText(operation);
    }

    public void inputSolution(PlayerSolution solution){
        this.solutions.setSolution(solution);
    }
}
