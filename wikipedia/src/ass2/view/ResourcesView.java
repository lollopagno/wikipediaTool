package ass2.view;

public interface ResourcesView {
    /**
     * Dichiara di aggiungere un risorsa che deve essere richiesta al server.
     * @param n
     */
    void putResources(int n);

    /**
     * Dichiara di aver utilizzato una risorsa che deve essere rimossa.
     * @param n
     */
    void useResources(int n);
}
