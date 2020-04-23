package ass2;

public class WikipediaTask implements Runnable {

    private final WikipediaClient wc;
    private final MyGraph graph;

    public WikipediaTask(WikipediaClient wc, MyGraph graph){

        this.wc = wc;
        this.graph = graph;

    }

    @Override
    public void run() {

        try{

            //Parsing Url
            wc.parseURL();

            //Disegna il grafo
            graph.drawGraph();

        }catch (Exception ex) {
            System.out.println(ex);
            //System.out.println("Non è stato possibile trovare nessun link per il concetto specificato!");
        }


    }
}
