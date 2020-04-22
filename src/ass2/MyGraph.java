package ass2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;

public class MyGraph {

    private final Graph<String, DefaultEdge> graph;
    private final List<Graph<String, DefaultEdge>> subGraphStrongConnectly;

    public MyGraph(GuiInterface gui){

        //Creo il grafo
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        this.graph.addVertex(gui.getConcept());

        //Algoritmo per disegnare il grafo
        StrongConnectivityAlgorithm<String, DefaultEdge> scAlg = new KosarajuStrongConnectivityInspector<>(graph);
        this.subGraphStrongConnectly = scAlg.getStronglyConnectedComponents();
    }

    //Inserisce i vertici e gli arche nel grafo
    public void insertVertex(JsonArray links, String currentVertex){

        for(JsonElement link: links){

            //Creo il vertice
            this.graph.addVertex(link.toString());

            //Aggiungo l'arco currentVertex --> link
            this.graph.addEdge(currentVertex, link.toString());
            log("Aggiugno l'arco tra "+currentVertex+" / "+link.toString());
        }
    }


    //Disegna il grafo
    public void drawGraph(){

        log("Strongly connected components:");
        for (Graph<String, DefaultEdge> stringDefaultEdgeGraph : this.subGraphStrongConnectly) {
            System.out.println(stringDefaultEdgeGraph);
        }
    }

    private void log(String msg){
        System.out.println(msg);
    }
}
