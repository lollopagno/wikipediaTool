package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class StartView extends JFrame implements ActionListener, KeyListener {
    private final JTextField actorsText;
    private final JTextField lengthText;
    private final JTextField timeText;

    public StartView() {
        this.setTitle("Start");
        this.setSize(320, 240);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        // Actors.
        JLabel actorsLabel = new JLabel("Actors number");
        this.actorsText = new JTextField(10);
        JPanel actors = new JPanel(new FlowLayout());
        actors.setSize(320, 30);
        actors.add(actorsLabel);
        actors.add(actorsText);
        this.add(actors);

        // Sequence length.
        JLabel lengthLabel = new JLabel("Length number");
        this.lengthText = new JTextField(10);
        JPanel length = new JPanel(new FlowLayout());
        length.setSize(320, 30);
        length.add(lengthLabel);
        length.add(lengthText);
        this.add(length);

        // Time between each turn.
        JLabel timeLabel = new JLabel("Time between each turn");
        this.timeText = new JTextField(10);
        JPanel time = new JPanel(new FlowLayout());
        time.setSize(320, 30);
        time.add(timeLabel);
        time.add(timeText);
        this.add(time);

        // Button start.
        JButton start = new JButton("Start");
        start.addActionListener(this);
        this.add(start);

        this.pack();
        this.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        generateGameView();
    }

    public static void main(String[] args) {
        StartView view = new StartView();
        view.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        JButton source = (JButton) e.getSource();
        if (e.getKeyCode() == KeyEvent.VK_ENTER &&
                source.getActionCommand().equals("Start")) {
            this.generateGameView();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Prepare the next view where the game runs.
     */
    private void generateGameView() {
        String sl = this.lengthText.getText();
        int length = Integer.parseInt(sl);
        int nPlayers = Integer.parseInt(this.actorsText.getText());
        JFrame game = new GameView(length, nPlayers);
        game.setVisible(true);
        this.setVisible(false);
    }
}
