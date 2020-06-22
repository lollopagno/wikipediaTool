package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class View extends JFrame implements ActionListener, KeyListener {

    private final JTextField username;
    private final JPanel controlPanel;

    private final RequestClient client;

    public View() {

        this.client = new RequestClient();

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
        SwingUtilities.invokeLater(this::listUser);
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

        try {
            // POST - registrazione utente
            log("POST register user: " + username);
            this.client.registerUser(username);
        } catch (Exception ex) {
            log("Error url POST " + ex.getMessage());
        }
    }

    /**
     * Mostra la lista degli utenti che stanno giocando
     */
    private void listUser(){

        ArrayList<String> users = new ArrayList<>();

        try{
            // GET - lista di utenti che partecipano
            users = this.client.listUser();
            this.controlPanel.removeAll();
        }catch(Exception ex){
            log("Error url GET " + ex.getMessage());
        }

        createTable(users);

    }

    /**
     * Creazione tabella con tutti gli utenti
     * @param users
     */
    private void createTable(ArrayList<String> users){

        //TODO aggiornare ogni 30 sec la tabella per nuovi utenti

        users.forEach((player) -> {
            JLabel label = new JLabel();
            label.setText(player);
            this.controlPanel.add(label);
        });
        this.controlPanel.setVisible(true);

        this.client.startGame();
    }

    private void log(String msg){
        System.out.println(msg);
    }
}

