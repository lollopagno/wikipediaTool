package ass2.model.classes.mygraph;

public interface AssignmentGraph {
    /**
     * Add a node to the graph.
     * Title must be univoke in the entire graph.
     * @param title Title of the node.
     */
    void addNode(String title);

    /**
     * Add a edge to the graph.
     * @param from Origin node.
     * @param to Destination node.
     */
    void addEdge(String from, String to);
}
