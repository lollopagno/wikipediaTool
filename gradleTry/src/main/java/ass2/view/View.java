package ass2.view;

public interface View {
    /**
     * Use this method to add only one node to the main graph.
     * @param from Name of the node.
     */
    void display(String from);

    /**
     * Use this method to add one node with a edge to another node.
     */
    void display(String from, String to);
}