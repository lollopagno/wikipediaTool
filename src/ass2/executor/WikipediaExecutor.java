package ass2.executor;

import ass2.GuiInterface;
import ass2.MyGraph;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WikipediaExecutor {

    private final GuiInterface gui;
    private final WikipediaClient wc;
    private final MyGraph graph;
    private ExecutorService execMain;

    public WikipediaExecutor(GuiInterface gui, WikipediaClient wc, MyGraph graph){

        this.gui = gui;
        this.wc = wc;
        this.graph = graph;
    }

    //Faccio partire execMain (il primo executor)
    public void mainExec() {

        //Creiamo l'executor main
        this.execMain = Executors.newSingleThreadExecutor();
        this.execMain.execute(new WikipediaMainTask(this.wc, this.gui, this.graph));

        // Attendo la terminazione del execMain
        try {
            this.execMain.shutdown();
            this.execMain.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        }catch (Exception e){e.printStackTrace();}

    }

    //Facciamo partire gli altri executor dopo il mainExec
    public void taskExec(){

        //Controlla se è necessario continuare con la ricerca in profondità
        if (gui.updateLevel()){

            for(int i=0; i <wc.getLenghtLinks(); i++) {
                ExecutorService exec = Executors.newSingleThreadExecutor();

                WikipediaClient wcExec = new WikipediaClient(this.gui, this.graph);
                exec.execute(new WikipediaTask(wcExec, graph, wc.getElemArray(i)));

                // Attendo la terminazione del exec
                try {
                    exec.shutdown();
                    exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

                }catch (Exception e){e.printStackTrace();}
            }

        } else {
                log("--> Ho terminato la ricerca. Livello: "+gui.getLevel());
        }
    }

    private void log(String msg){
        System.out.println(msg);
    }
}
