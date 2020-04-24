package ass2.executor;

import ass2.GuiInterface;
import ass2.MyGraph;

public class WikipediaMainTask implements Runnable {

    private final WikipediaClient wc;
    private final MyGraph graph;
    private final GuiInterface gui;

    public WikipediaMainTask(WikipediaClient wc, GuiInterface gui, MyGraph graph){

        this.wc = wc;
        this.gui = gui;
        this.graph = graph;
    }

    @Override
    public void run() {

        try{

            //Gui
            gui.setConcept();
            gui.setLevel();

            //Inserisco il primo vertice
            graph.setFirstVertex();

            //Parsing Url
            wc.parseURL(gui.getConcept());

            //Disegna il grafo
            log("\n\n*** Disegno il grafo *** per "+gui.getConcept());
            graph.drawGraph();

        }catch (Exception ex) {
            log("\nNon è stato possibile trovare nessun link per il concetto: "+gui.getConcept()+"\n");
        }
    }

    private synchronized void log(String msg){
        System.out.println(msg);
    }
}
