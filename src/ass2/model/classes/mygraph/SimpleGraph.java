package ass2.model.classes.mygraph;

import ass2.controller.Controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SimpleGraph implements AssignmentGraph {
    private final List<Node> nodes;
    private final Controller controller;

    public SimpleGraph(Controller controller) {
        this.nodes = new LinkedList<>();
        this.controller = controller;
    }

    public synchronized void addNode(String title) throws IllegalArgumentException {
        if (this.getOptionalNode(title).isPresent())
            throw new IllegalArgumentException(title);

        Node node = new Node(title);
        this.nodes.add(node);
        this.controller.modelUpdated(title);
    }

    public synchronized Optional<Node> getOptionalNode(String title) {
        return this.nodes.stream().filter(elem -> elem.getTitle().equals(title)).findAny();
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
            throw new IllegalArgumentException("Missing one node in " + from + " or " + to + ".");
        } else {
            checkFrom.addEdge(checkTo);
            this.controller.modelUpdated(from, to);
        }
    }

    @Override
    public int getNodeNumber() {
        return this.nodes.size();
    }
}
