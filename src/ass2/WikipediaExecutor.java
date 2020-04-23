package ass2;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WikipediaExecutor {

    private ExecutorService execMain;
    private final GuiInterface gui;
    private final WikipediaClient wc;
    private final MyGraph graph;

    public WikipediaExecutor(GuiInterface gui, WikipediaClient wc, MyGraph graph){

        this.gui = gui;
        this.wc = wc;
        this.graph = graph;
    }

    //Faccio partire execMain
    public void start(){

        //Creiamo l'executor main
        this.execMain = Executors.newSingleThreadExecutor();

        //Gui
        gui.setConcept();
        gui.setLevel();

        //Inserisco il primo vertice
        graph.setFirstVertex();

        execMain.execute(new WikipediaTask(this.wc, this.graph));
    }
}
