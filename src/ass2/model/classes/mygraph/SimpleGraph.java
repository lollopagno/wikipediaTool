package ass2.model.classes.mygraph;

import ass2.controller.Controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SimpleGraph {
    private List<Node> nodes;
    private Controller controller;

    public SimpleGraph(Controller controller) {
        this.nodes = new LinkedList<>();
        this.controller = controller;
    }

    public void addNode(String title) throws IllegalArgumentException {
        if (this.getNode(title) != null)
            throw new IllegalArgumentException();
        Node node = new Node(title);
        Random r = new Random();
        node.setX(-1 + r.nextDouble() * 2);
        node.setY(-1 + r.nextDouble() * 2);
        this.nodes.add(node);
        this.controller.modelUpdated();
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

    public Node getNode(String title) {
        for (Node e : this.nodes)
            if (e.getTitle().equals(title)) return e;
        return null;
    }

    public Node getNode(int index) throws IllegalArgumentException {
        if(index > this.nodes.size()) throw new IllegalArgumentException();
        return this.nodes.get(index);
    }

    public void addEdge(String from, String to) throws IllegalArgumentException {
        Node checkFrom = null, checkTo = null;
        for (Node e : this.nodes) {
            if (e.getTitle().equals(from)) checkFrom = e;
            if (e.getTitle().equals(to)) checkTo = e;
        }
        if (checkFrom == null || checkTo == null)
            throw new IllegalArgumentException();
        else {
            checkFrom.addEdge(checkTo);
            this.controller.modelUpdated();
        }
    }
}
