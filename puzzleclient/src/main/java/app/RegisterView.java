package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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

public class RegisterView extends JFrame implements ActionListener, KeyListener {

    private final JTextField username;
    private final JPanel controlPanel;
    private final Vector<String> columnTable;

    private final RequestClient client;

    private final ScheduledExecutorService job = Executors.newSingleThreadScheduledExecutor();

    public RegisterView(int x, int y) {

        // Client Object
        this.client = new RequestClient(this, x, y);

        // Params View
        this.setTitle("Register User");
        this.setSize(500, 500);
        this.setLayout(new BorderLayout());

        // Button Register User
        JButton registerUser = new JButton("Register User");
        registerUser.addActionListener(this);
        registerUser.addKeyListener(this);

        // JTextField
        this.username = new JTextField(10);

        // Control Panel
        this.controlPanel = new JPanel(new FlowLayout());
        this.controlPanel.setSize(500, (int) (500 * 0.1));

        this.controlPanel.add(registerUser);
        this.controlPanel.add(this.username);
        this.add(this.controlPanel, BorderLayout.CENTER);

        //Set column Table
        this.columnTable = new Vector<>();
        this.columnTable.add("User");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        registerUser(this.username.getText());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            registerUser(this.username.getText());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * RegisterUser button action
     * @param username user name
     */
    private void registerUser(String username) {

        // Register user
        this.client.registerUser(username);

        // Update view
        this.updateListUser();

        // Start game
        this.client.startGame();
    }

    /**
     * Execution job: update list user view
     */
    public void updateListUser() {

        SwingUtilities.invokeLater(() -> this.setTitle("List user"));
        this.job.scheduleAtFixedRate(this::updateView, 0, 30000, TimeUnit.MILLISECONDS);
    }

    /**
     * Create table with all users
     */
    private void updateView(){

        // Get list user
        ArrayList<String> users = this.client.listUser();
        Vector<Vector<String>> rowData = new Vector<>();

        log("[Executor] --> Update table in view");

        // Add user into vector
        users.forEach(user -> {
            Vector<String> vector = new Vector<>();
            vector.add(user);
            rowData.add(vector);
        });

        // Model for create jTable not editable
        TableModel model = new DefaultTableModel(rowData, this.columnTable)
        {
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        SwingUtilities.invokeLater(() -> {

            // Table Users
            JTable table = new JTable(model);
            table.setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(table);

            this.controlPanel.removeAll();
            this.controlPanel.add(scrollPane);

            this.repaint();
            this.revalidate();
        });
    }

    private void log(String msg){
        System.out.println(msg);
    }
}

