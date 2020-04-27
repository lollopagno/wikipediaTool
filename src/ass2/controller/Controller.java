package ass2.controller;

public interface Controller {
    /**
     * Fetch Wikipedia for the concept many time how much are entries.
     * @param concept Concept to fetch.
     * @param entry Times to fetch.
     */
    void fetchConcept(String concept, int entry);

    /**
     * Signal the view for a node update.
     * @param from Title of the node.
     */
    void modelUpdated(String from);

    /**
     * Signal the view for a edge update.
     * @param from Title of the from node.
     * @param to Title of the to node.
     */
    void modelUpdated(String from, String to);
}
