package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class View extends JFrame implements ActionListener, KeyListener {

    private final JTextField username;
    private final JPanel controlPanel;
    private Vector<String> columnTable;

    private final RequestClient client;

    private final ScheduledExecutorService job = Executors.newSingleThreadScheduledExecutor();

    public View(int x, int y) {

        // Client Object
        this.client = new RequestClient(this, x, y);

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

        //Set column Table
        this.columnTable = new Vector<>();
        this.columnTable.add("User");
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
     * RegisterUser button action
     * @param username user name
     */
    private void registerUser(String username) {
        this.client.registerUser(username);
    }

    /**
     * Execution job: update list user view
     * @param users list user name
     */
    public void updateListUser(ArrayList<String> users) {

        this.controlPanel.removeAll();
        this.setTitle("List user");

        this.job.scheduleAtFixedRate(() -> updateView(users), 0, 30000, TimeUnit.MILLISECONDS);
    }

    /**
     * Create table with all users
     * @param users list user name
     */
    private void updateView(ArrayList<String> users){

        log("[Executor] --> Update table in view");

        // Row data
        Vector<Vector<String>> rowData = new Vector<>();
        //Vector<String> data = new Vector<>(users);
        Vector<String> data = new Vector<>();
        users.forEach((player) -> {
            data.add(player);
        });

        // Table Users
        final JTable table = new JTable(rowData, this.columnTable);
        JScrollPane scrollPane = new JScrollPane(table);
        this.controlPanel.add(scrollPane);

        SwingUtilities.invokeLater(() -> {
            this.repaint();
            this.revalidate();
        });
    }
    private void log(String msg){
        System.out.println(msg);
    }
}

