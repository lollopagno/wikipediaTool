package ass2.view;

import ass2.controller.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame implements ActionListener {
    private Controller controller;
    private JTextField conceptText, entryText;

    public MainFrame() {
        // Set some defaults.
        int width = 500;
        int height = 400;
        this.setTitle("Wikipedia tool");
        this.setSize(width, height);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        this. addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent ev){
                System.exit(-1);
            }
            public void windowClosed(WindowEvent ev){
                System.exit(-1);
            }
        });

        // Set controls.
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setSize(width, (int)(height * 0.1));
        this.conceptText = new JTextField(10);
        controlPanel.add(conceptText);
        this.entryText = new JTextField(10);
        controlPanel.add(entryText);
        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(this);
        controlPanel.add(btnStart);

        // Set contentPanel.
        VisualizerPanel contentPanel = new VisualizerPanel(width, (int)(height * 0.9));

        // Set controller.
        controller = new MainController(contentPanel);

        // Compose view.
        this.getContentPane().add(controlPanel, BorderLayout.PAGE_START);
        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Start")){
            String concept = this.conceptText.getText();
            String entry = this.entryText.getText();
            this.controller.fetchConcept(concept, Integer.parseInt(entry));
        }
    }
}
