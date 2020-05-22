package views.start;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * Use this panel to input value in the start view.
 */
public class InputPanel extends JPanel {
    private final JTextField valueText;
    private final JLabel errorLabel;

    public InputPanel(String labelText) {
        JLabel label = new JLabel(labelText);
        this.add(label);

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.red);
        errorLabel.setVisible(false);
        this.add(errorLabel);

        this.valueText = new JTextField(10);
        this.add(valueText);

        this.setSize(320, 30);
    }

    public Optional<Integer> getValue() {
        try {
            int number = Integer.parseInt(this.valueText.getText());
            if (number < 0) {
                this.showError("Number must be > 0");
                return Optional.empty();
            }

            this.errorLabel.setVisible(false);
            return Optional.of(number);
        } catch (NumberFormatException a) {
            this.showError("Number format exception");
            return Optional.empty();
        }
    }

    private void showError(String msg) {
        this.errorLabel.setText(msg);
        this.errorLabel.setVisible(true);
    }
}