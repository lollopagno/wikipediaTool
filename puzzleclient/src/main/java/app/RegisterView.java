package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
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

    public RegisterView() {

        // Client Object
        this.client = RequestClient.instance();

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

        // Action close view user
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                job.shutdown();
            }
        });

        this.job.scheduleAtFixedRate(this::updateView, 0, 30000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        registerUser(this.username.getText());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            registerUser(this.username.getText());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * RegisterUser button action
     * @param username user name
     */
    private void registerUser(String username) {
        this.client.addPlayer(username, msg -> {
            log(msg + ": " + username);

            // Start game
            final PuzzleBoard puzzle = new PuzzleBoard(username);
            puzzle.setVisible(true);
        });
    }


    /**
     * Create table with all users
     */
    private void updateView() {
        // Get list user
        this.client.allUsers(list -> {
            Vector<Vector<String>> rowData = new Vector<>();

            log("Update table in view every 30s");

            // Add user into vector
            list.forEach(user -> {
                Vector<String> vector = new Vector<>();
                vector.add(user);
                rowData.add(vector);
            });

            // Model for create JTable not editable
            TableModel model = new DefaultTableModel(rowData, this.columnTable) {
                public boolean isCellEditable(int row, int column) {
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

        });
    }

    private void log(String msg) {
        synchronized (System.out) {
            System.out.println("[Info] " + msg);
        }
    }
}