package ass2.view;

import ass2.model.classes.mygraph.Node;
import ass2.model.classes.mygraph.SimpleGraph;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;

public class VisualizerPanel extends JPanel implements View {
    private static final int RADIUS = 3;
    private SimpleGraph graph;

    private long getDX(){
        return this.getSize().width / 2 - 20;
    }

    private long getDy() {
        return this.getSize().height / 2 - 20;
    }

    public VisualizerPanel(int w, int h) {
        Graph graphStream = new SingleGraph("graph");
        Viewer viewer = new Viewer(graphStream, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        org.graphstream.ui.view.View view = viewer.addDefaultView(false);
        setSize(w, h);
        this.graph = null;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, this.getWidth(), this.getHeight());

        if (this.graph != null) {
            for(Node node: this.graph.getNodes()) {
                this.drawNode(g2, node);
            }
        }
    }

    private void drawNode(Graphics2D g2, Node node) {
        int x0 = (int)(this.getDX() + node.getX() * this.getDX());
        int y0 = (int)(this.getDy() - node.getY() * this.getDy());
        g2.drawOval(x0, y0, (RADIUS * 2), (RADIUS * 2));
        g2.drawString(node.getTitle(), x0 + 5, y0 + 5);
        node.getEdges().forEach(b -> drawEdge(g2, node, b));
    }

    private void drawEdge(Graphics2D g2, Node from, Node to) {
        int x0 = (int) (this.getDX() + from.getX() * this.getDX());
        int y0 = (int) (this.getDy() - from.getY() * this.getDy());
        int x1 = (int) (this.getDX() + to.getX() * this.getDX());
        int y1 = (int) (this.getDy() - to.getY() * this.getDy());
        g2.drawLine(x0, y0, x1, y1);
    }

    @Override
    public void display(SimpleGraph graph) {
        this.graph = graph;
        repaint();
    }
}
