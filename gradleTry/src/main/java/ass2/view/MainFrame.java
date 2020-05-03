package ass2.view;

import ass2.controller.Controller;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame implements ActionListener, View {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private Controller controller;
    private JTextField conceptText, entryText;
    private Graph graph;
    public int entryView;


    public MainFrame(String title, Controller controller) {
        // Set some defaults.
        this.setTitle(title);
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(-1);
            }

            public void windowClosed(WindowEvent ev) {
                System.exit(-1);
            }
        });

        // Set controls.
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setSize(WIDTH, (int) (HEIGHT * 0.1));
        this.conceptText = new JTextField(10);
        controlPanel.add(conceptText);
        this.entryText = new JTextField(10);
        controlPanel.add(entryText);
        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(this);
        controlPanel.add(btnStart);

        // Set contentPanel.
        JPanel graphPanel = new JPanel(new GridLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(WIDTH, (int)(HEIGHT * 0.9));
            }
        };
        graphPanel.setSize(WIDTH, (int) (HEIGHT * 0.9));
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
        graph = new SingleGraph("Tutorial");
        graph.setStrict(false);
        graph.setAutoCreate(true);
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();
        ViewPanel viewPanel = viewer.addDefaultView(false);
        graphPanel.add(viewPanel);

        // Set controller.
        this.controller = controller;

        // Compose view.
        this.getContentPane().add(controlPanel, BorderLayout.PAGE_START);
        this.getContentPane().add(graphPanel, BorderLayout.CENTER);

        // This may cause problems.
        // this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.graph.clear();
        if (e.getActionCommand().equals("Start")) {
            String concept = this.conceptText.getText();
            try {
                this.entryView = Integer.parseInt(this.entryText.getText());
                this.controller.fetchConcept(concept, entryView);
            }catch(Exception ex){
                log("Non è stato digitato un intero come livello di profondità nella ricerca!");
            }
        }
    }

    @Override
    public void display(final String from, final String to) {
        colorElement(from, drawNode(from));
        colorElement(from, drawEdge(from, to));
    }

    @Override
    public void display(final String from) {
        colorElement(from, drawNode(from));
    }

    /**
     * Draw an edge on the main graph.
     * @param title Node title.
     * @return Edge drawed.
     */
    public Node drawNode(final String title){
        this.graph.addNode(title);
        Node node = this.graph.getNode(title);
        node.addAttribute("ui.label", title);
        return node;
    }

    /**
     * Draw an edge on the main graph.
     * @param from Node from.
     * @param to Node to.
     * @return Edge drawed.
     */
    public Edge drawEdge(final String from, final String to) {
        this.graph.addEdge(from + to, from, to);
        Edge element = this.graph.getEdge(from + to);
        if(element == null) {
            // This maybe cause when an edge is already placed before with another direction.
            element = this.graph.getEdge(to + from);
        }
        return element;
    }

    /**
     * Color an element based on the master concept.
     * @param master String of the master concept.
     * @param element Element to decorate.
     */
    public void colorElement(final String master, final Element element) {
        byte[] bytes = master.getBytes();
        int sum = 0;
        for (byte aByte : bytes) {
            sum += aByte;
        }
        int color = (sum % 200) + 25;
        try {
            element.addAttribute(
                    "ui.style",
                    "fill-color: rgb(" + color + "," + color + "," + color + ");");
        } catch (NullPointerException e) {
            synchronized (System.out) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    public int getEntryView(){
        return this.entryView;
    }

    private void log(String msg){
        synchronized (System.out)
        {
            System.out.println("[" + Thread.currentThread().getName() + "] " + msg);
        }
    }
}