package views.start;

import views.game.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

public class StartView extends JFrame implements ActionListener, KeyListener {
    private final ActorPanel actors;
    private final JTextField lengthText;
    private final JTextField timeText;

    public StartView() {
        this.setTitle("Start");
        this.setSize(320, 240);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        // Actors.
        actors = new ActorPanel();
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
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
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
        Optional<Integer> actors = this.actors.getActors();
        actors.ifPresent(actNumber -> {
            JFrame game = new GameView(length, actNumber);
            game.setVisible(true);
            this.setVisible(false);
        });
    }
}
