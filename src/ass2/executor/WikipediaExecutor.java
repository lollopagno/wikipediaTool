package ass2.executor;

import ass2.GuiInterface;
import ass2.MyGraph;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    //Faccio partire execMain (il primo executor)
    public void mainExec() {

        //Creiamo l'executor main
        this.execMain = Executors.newSingleThreadExecutor();
        execMain.execute(new WikipediaMainTask(this.wc, this.gui, this.graph));

        // Fine esecuzione execMain
        try {
            execMain.shutdown();
            // attendo fine esecuzione
            execMain.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        }catch (Exception ignored){}

        log(execMain.getClass().getName()+" è terminato?: "+execMain.isTerminated());
    }

    private void log(String msg){
        System.out.println(msg);
    }
}
