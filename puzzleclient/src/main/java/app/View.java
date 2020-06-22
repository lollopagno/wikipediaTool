package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Vector;

public class View extends JFrame implements ActionListener, KeyListener {

    private final JTextField username;
    private final JPanel controlPanel;

    private final RequestClient client;

    public View() {

        // Client Object
        this.client = new RequestClient(this, 5, 3);

        // Params View
        this.setTitle("Register User");
        this.setSize(320, 240);
        this.setLayout(new BorderLayout());

        // Button Register User
        JButton registerUser = new JButton("Register User");
        registerUser.addActionListener(this);
        registerUser.addKeyListener(this);

        // JTextField
        this.username = new JTextField(10);

        // Control Panel
        this.controlPanel = new JPanel(new FlowLayout());
        this.controlPanel.setSize(320, (int) (240 * 0.1));

        this.controlPanel.add(registerUser);
        this.controlPanel.add(this.username);
        this.add(this.controlPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> registerUser(this.username.getText()));
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            SwingUtilities.invokeLater(() -> registerUser(this.username.getText()));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * Azione del pulsante registerUser
     * @param username nome dell'utente
     */
    private void registerUser(String username) {

        this.client.registerUser(username);
        log("POST register user: " + username);
    }

    /**
     * Creazione tabella con tutti gli utenti
     * @param users
     */
    public void createTable(ArrayList<String> users) {

        //TODO aggiornare ogni 30 sec la tabella per nuovi utenti
        // Implementare un NewSingleThread

        // Remove all object into view
        this.controlPanel.removeAll();

        // Column
        Vector<String> column = new Vector<>();
        column.add("User");

        // Row data
        Vector<Vector<String>> rowData = new Vector<>();
        Vector<String> data = new Vector<>();
        users.forEach((player) -> {
            data.add(player);
        });

        final JTable table = new JTable(rowData, column);
        JScrollPane scrollPane = new JScrollPane(table);
        this.controlPanel.add(scrollPane);

        // New title
        this.setTitle("List user");
        SwingUtilities.invokeLater(() -> { this.repaint(); this.revalidate(); });
    }

    private void log(String msg){
        System.out.println(msg);
    }
}

