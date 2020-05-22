package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartView extends JFrame implements ActionListener {
    private final JTextField actorsText;
    private final JTextField lengthText;

    public StartView() {
        this.setTitle("Start");
        this.setSize(320, 240);

        JLabel actorsLabel = new JLabel("Actors number");
        actorsText = new JTextField(10);
        JPanel actors = new JPanel(new FlowLayout());
        actors.setSize(320, 30);
        actors.add(actorsLabel);
        actors.add(actorsText);

        JLabel lengthLabel = new JLabel("Length number");
        lengthText = new JTextField(10);
        JPanel length = new JPanel(new FlowLayout());
        length.setSize(320, 30);
        length.add(lengthLabel);
        length.add(lengthText);

        JButton start = new JButton("Start");
        start.addActionListener(this);
        JPanel panel = new JPanel();
        panel.add(actors);
        panel.add(length);
        panel.add(start);

        this.getContentPane().add(panel);
        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        String sl = this.lengthText.getText();
        int length = Integer.parseInt(sl);
        int nPlayers = Integer.parseInt(this.actorsText.getText());
        JFrame game = new GameView(length, nPlayers);
        game.setVisible(true);
    }

    public static void main(String[] args) {
        StartView view = new StartView();
        view.setVisible(true);
    }
}
