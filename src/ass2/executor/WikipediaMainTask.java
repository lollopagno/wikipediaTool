package ass2.executor;

import ass2.GuiInterface;
import ass2.MyGraph;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
            wc.parseURL();

            // Creo i prossimi pool di executor
            /*
                Non c'è bisogno di fare controlli specifici
                poichè qui il livello di profondità è sempre > 0
             */
            //TODO creo il pool di thread per i successivi link
            ExecutorService exec = Executors.newFixedThreadPool(wc.getLenghtLinks());

            //Controlli da effettuare per creare i pool di executor successivi al primo gruppo
            /*if (gui.updateLevel()){

            }else {
                log("--> Ho terminato la ricerca. Livello: "+gui.getLevel());
            }*/

            //Disegna il grafo
            graph.drawGraph();

        }catch (Exception ex) {
            System.out.println("Non è stato possibile trovare nessun link per il concetto specificato!");
        }
    }

    private synchronized void log(String msg){
        System.out.println(msg);
    }
}
