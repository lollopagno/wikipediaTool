package views.start;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class ActorPanel extends JPanel {
    private final JTextField actorsText;
    private final JLabel errorLabel;

    public ActorPanel(){
        JLabel actorsLabel = new JLabel("Actors number");
        this.add(actorsLabel);

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        errorLabel.setVisible(false);
        this.add(errorLabel);

        this.actorsText = new JTextField(10);
        this.add(actorsText);

        this.setSize(320, 30);
    }

    public Optional<Integer> getActors() {
        try {
            int number = Integer.parseInt(this.actorsText.getText());
            this.errorLabel.setVisible(false);
            return Optional.of(number);
        } catch (NumberFormatException a) {
            this.showError("Actor number format exception");
            return Optional.empty();
        }
    }

    public void showError(String msg) {
        this.errorLabel.setText(msg);
        this.errorLabel.setVisible(true);
    }
}
