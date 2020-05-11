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
           throw new IllegalArgumentException();

        Node node = new Node(title);
        this.nodes.add(node);
        this.incNode();
        this.controller.modelUpdated(title);
        this.controller.displayNumber();
    }

    public synchronized Node getNode(String title) {
        for (Node e : this.nodes)
            if (e.getTitle().equals(title))
                return e;
        return null;
    }

    public synchronized void addEdge(String from, String to) throws IllegalArgumentException {

        Node checkFrom = null, checkTo = null;

        for (Node e : this.nodes) {

            if (e.getTitle().equals(from))
                checkFrom = e;
            if (e.getTitle().equals(to))
                checkTo = e;
        }

        if (checkFrom == null || checkTo == null) {
            throw new IllegalArgumentException("Missing one node of the edge.");
        } else {
            checkFrom.addEdge(checkTo);
            this.controller.modelUpdated(from, to);
        }
    }

    //Incrementa il numero di Nodi del grafo
    private synchronized void incNode(){
        this.numberNode += 1;
    }

    // Ritorna il numeor di nodi del grafo
    public synchronized int getNumberNode(){
        return this.numberNode;
    }

}
