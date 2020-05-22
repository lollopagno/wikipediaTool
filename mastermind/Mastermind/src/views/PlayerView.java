package views;

import javax.swing.*;
import java.util.ArrayList;
import java.util.stream.Stream;

public class PlayerView extends JPanel {
    private final JLabel name;
    private final JLabel sequence;
    private final JLabel operation;

    public PlayerView(String name){
        this.name = new JLabel(name);
        this.sequence = new JLabel("");
        this.operation = new JLabel("operation");
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
}
