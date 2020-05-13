package ass2.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ResourcesFrame extends JFrame implements ResourcesView, ActionListener {
    private static final int WIDTH = 240;
    private static final int HEIGHT = 160;
    private final JLabel resources;
    private final JLabel reactiveness;
    private int number;

    public ResourcesFrame() {
        this.setTitle("Resources");
        this.setSize(WIDTH, HEIGHT);
        this.setLocation(0, 500);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(new FlowLayout());
        this.resources = new JLabel();
        this.reactiveness = new JLabel();
        JButton testReactiveness = new JButton("Test");
        testReactiveness.addActionListener(this);
        controlPanel.add(resources);
        controlPanel.add(this.reactiveness);
        controlPanel.add(testReactiveness);

        this.number = 0;

        this.getContentPane().add(controlPanel, BorderLayout.CENTER);
    }

    @Override
    public void putResources(int n) {
        synchronized (this) {
            this.number += n;
        }
        this.displayNumber();
    }

    @Override
    public void useResources(int n) {
        synchronized (this) {
            this.number -= n;
        }
        this.displayNumber();
    }

    public void displayNumber() {
        SwingUtilities.invokeLater(() -> this.resources.setText("Resources to process: " + number));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Random r = new Random();
        SwingUtilities.invokeLater(() -> this.reactiveness.setText("Test reactiveness " + r.nextInt()));
    }
}
