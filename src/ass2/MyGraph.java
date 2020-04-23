package ass2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;

public class MyGraph {

    private final Graph<String, DefaultEdge> graph;
    private List<Graph<String, DefaultEdge>> subGraphStrongConnectly;

    public MyGraph(GuiInterface gui){

        //Creo il grafo
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        //Creo il vertice iniziale: coincide con il primo concetto dato in input
        this.graph.addVertex(gui.getConcept());

    }

    //Inserisce i vertici e gli arche nel grafo
    public void insertVertex(JsonArray links, String currentVertex){

        log("\n\n*** Aggiungo i vertici e gli archi ***\n");
        for(JsonElement link: links){

            //Creo il vertice
            JsonObject elem = link.getAsJsonObject();
            this.graph.addVertex(elem.get("*").getAsString());

            //Aggiungo l'arco currentVertex --> link
            this.graph.addEdge(currentVertex, elem.get("*").getAsString());
            log("Aggiugno l'arco tra "+currentVertex+" and "+elem.get("*").getAsString());
        }
    }

    //Disegna il grafo
    public void drawGraph(){

        //Operazioni preliminari per disegnare il grafo
        StrongConnectivityAlgorithm<String, DefaultEdge> scAlg = new KosarajuStrongConnectivityInspector<>(this.graph);
        this.subGraphStrongConnectly = scAlg.getStronglyConnectedComponents();

        log("\n\n*** Disegno il grafo ***\n");
        for (int i = 0; i < this.subGraphStrongConnectly.size(); i++) {
            System.out.println(this.subGraphStrongConnectly.get(i));
        }
    }

    //Log
    private void log(String msg){
        System.out.println(msg);
    }
}
