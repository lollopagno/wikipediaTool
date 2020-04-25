package ass2.model.classes.mygraph;

import java.util.HashSet;
import java.util.Set;

public class Node {
    private String title;
    private Set<Node> edges;
    private double x, y;

    public Node(String title) {
        this.title = title;
        this.edges = new HashSet<>();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getTitle() {
        return this.title;
    }

    public Set<Node> getEdges() {
        return this.edges;
    }

    public void addEdge(Node node) {
        this.edges.add(node);
    }
}