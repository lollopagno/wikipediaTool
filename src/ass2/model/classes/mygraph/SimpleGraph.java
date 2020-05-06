package ass2.model.classes.mygraph;

import ass2.controller.Controller;

import java.util.LinkedList;
import java.util.List;

public class SimpleGraph implements AssignmentGraph {
    private List<Node> nodes;
    private Controller controller;
    public int numberNode;

    public SimpleGraph(Controller controller) {
        this.nodes = new LinkedList<>();
        this.controller = controller;
        this.numberNode = 0;
    }

     public synchronized void addNode(String title) throws IllegalArgumentException {

        if (this.getNode(title) != null)
            return;

        Node node = new Node(title);
        this.nodes.add(node);
        this.incNode();
        this.controller.modelUpdated(title);
        this.controller.displayNumber();
    }

    public synchronized List<Node> getNodes() {
        return this.nodes;
    }

    public synchronized Node getNode(String title) {
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

    public synchronized void addEdge(String from, String to) throws IllegalArgumentException {

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
            throw new IllegalArgumentException("Missing one node of the edge.");
        } else {
            checkFrom.addEdge(checkTo);
            this.controller.modelUpdated(from, to);
        }
    }

    //Incrementa il numero di Nodi del grafo
    private void incNode(){
        this.numberNode += 1;
    }

    // Ritorna il numeor di nodi del grafo
    public int getNumberNode(){
        return this.numberNode;
    }

}
