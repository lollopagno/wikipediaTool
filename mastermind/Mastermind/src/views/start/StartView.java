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
    private final InputPanel actors, length, time;
    private boolean busy;

    public StartView() {
        this.busy = false;
        this.setTitle("Start");
        this.setSize(320, 240);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        // Actors.
        this.actors = new InputPanel("Actor number");
        this.add(this.actors);

        // Sequence length.
        this.length = new InputPanel("Length number");
        this.add(this.length);

        // Time between each turn.
        this.time = new InputPanel("Time per turn");
        this.add(this.time);

        // Button start.
        JButton start = new JButton("Start");
        start.addActionListener(this);
        start.addKeyListener(this);
        this.add(start, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(this::generateGameView);
    }

    public static void main(String[] args) {
        StartView view = new StartView();
        view.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            SwingUtilities.invokeLater(this::generateGameView);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * Prepare the next view where the game runs.
     */
    private void generateGameView() {
        if(this.busy)
            return;
        this.busy = true;

        try{
            Optional<Integer> actors = this.actors.getValue();
            Optional<Integer> length = this.length.getValue();
            Optional<Integer> time = this.time.getValue();
            actors.ifPresent(a -> length.ifPresent(l -> time.ifPresent(t -> {
                JFrame game = new GameView(l, a, t);
                game.setVisible(true);
                this.setVisible(false);
            })));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.busy = false;
        }
    }
}
