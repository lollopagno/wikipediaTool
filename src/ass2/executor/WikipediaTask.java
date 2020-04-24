package ass2.executor;

import ass2.MyGraph;

public class WikipediaTask implements Runnable{

    private final WikipediaClient wc;
    private final MyGraph graph;
    private String concept;

    public WikipediaTask(WikipediaClient wc, MyGraph graph, String concept){

        this.wc = wc;
        this.graph = graph;
        this.concept = concept;

    }

    @Override
    public void run() {

        //Parse del URL
        try {
            log(this.getClass().getName()+"  sto lavorando per "+this.concept);
            wc.parseURL(this.concept);

            //Disegna il grafo
            log("\n\n*** Disegno il grafo *** per "+this.concept);
            this.graph.drawGraph();

        }catch (Exception ex){
            log("Non è stato possibile trovare nessun link per il concetto: "+this.concept);
        }
    }

    private void log(String msg) {
        System.out.println(msg);
    }
}
