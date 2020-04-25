package ass2.model.classes;

import ass2.controller.Controller;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;
import java.util.Set;

public class TentoniGraph {
    private final Graph<String, DefaultEdge> graph;
    private List<Graph<String, DefaultEdge>> subGraphStrongConnectly;
    private Controller controller;

    public TentoniGraph(Controller controller) {
        // Creo il grafo.
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        this.controller = controller;
    }

    /**
     * Creo il vertice iniziale: coincide con il primo concetto dato in input.
     *
     * @param concept Concetto da inserire in cima.
     */
    public void setFirstVertex(String concept) {
        this.graph.addVertex(concept);
        this.notifyUpdate();
    }

    /**
     * Inserisce i vertici e gli archi nel grafo.
     * Successivamente segnala al controller che il modello è stato aggiornato.
     * @param links Link scaricati.
     * @param currentVertex Vertice corrente.
     */
    public void insertVertex(Set<WikiLink> links, String currentVertex) {
        log("\n\n*** Aggiungo i vertici e gli archi ***\n");
        for (WikiLink link : links) {
            this.graph.addVertex(link.getText());

            //Aggiungo l'arco currentVertex --> link
            this.graph.addEdge(currentVertex, link.getText());
            log("Aggiugno l'arco tra " + currentVertex + " and " + link.getText());
        }

        log("\nHo aggiunto per " + currentVertex + ": " + links.size() + " archi!");
        this.notifyUpdate();
    }

    private void notifyUpdate() {
        // this.controller.modelUpdated(this.graph.vertexSet(), this.graph.edgeSet());
    }

    /**
     * Esegue il log del messaggio.
     * @param msg Messaggio.
     */
    private void log(String msg) {
        synchronized (System.out) {
            System.out.println("[" + Thread.currentThread().getName() + "] " + msg);
        }
    }
}
