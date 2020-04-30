package ass2.model.classes.mygraph;

import java.util.HashSet;
import java.util.Set;

public class Node {
    private String title;
    private Set<Node> edges;

    public Node(String title) {
        this.title = title;
        this.edges = new HashSet<>();
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