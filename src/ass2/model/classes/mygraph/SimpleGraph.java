package ass2.model.classes.mygraph;

import ass2.controller.Controller;

import java.util.LinkedList;
import java.util.List;

public class SimpleGraph {
    private List<Node> nodes;
    private Controller controller;

    public SimpleGraph(Controller controller) {
        this.nodes = new LinkedList<>();
        this.controller = controller;
    }

    public void addNode(String title) throws IllegalArgumentException {
        // Check if I've already added that node.
        if (this.getNode(title) != null)
            throw new IllegalArgumentException();

        Node node = new Node(title);
        this.nodes.add(node);
        this.controller.modelUpdated(title);
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

    public Node getNode(String title) {
        for (Node e : this.nodes)
            if (e.getTitle().equals(title))
                return e;
        return null;
    }

    public Node getNode(int index) throws IllegalArgumentException {
        if (index > this.nodes.size())
            throw new IllegalArgumentException();
        return this.nodes.get(index);
    }

    public void addEdge(String from, String to) throws IllegalArgumentException {
        Node checkFrom = null, checkTo = null;
        for (Node e : this.nodes) {
            // Fetch all nodes for from and to nodes.
            // We make only one cycle on all nodes.
            if (e.getTitle().equals(from))
                checkFrom = e;
            if (e.getTitle().equals(to))
                checkTo = e;
        }

        // Check if those nodes are present or not.
        if (checkFrom == null || checkTo == null) {
            throw new IllegalArgumentException();
        } else {
            checkFrom.addEdge(checkTo);
            this.controller.modelUpdated(from, to);
        }
    }
}