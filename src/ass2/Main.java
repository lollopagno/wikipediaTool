package ass2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        //Gui
        GuiInterface gui = new GuiInterface();

        //Graph
        MyGraph graph = new MyGraph(gui);

        //Wikipedia Client
        WikipediaClient wc = new WikipediaClient(gui, graph);

        // WikipediaExecutor
        WikipediaExecutor we = new WikipediaExecutor(gui, wc, graph);

        //Faccio partire il primo executor
        we.start();
    }
}
