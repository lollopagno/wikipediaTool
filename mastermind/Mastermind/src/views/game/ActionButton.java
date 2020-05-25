package views.game;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ActionButton extends JButton {
    public ActionButton(String label, ActionListener listener){
        super(label);
        this.addActionListener(listener);
    }
}
