package ass2;

public class Main {

    public static void main(String[] args) {

        //Gui
        GuiInterface gui = new GuiInterface();
        gui.setValue();

        //Graph
        MyGraph myGraph = new MyGraph(gui);

        //Wikipedia Client
        WikipediaClient wc = new WikipediaClient(gui, myGraph);

        try{
            wc.parseURL();
        }catch (Exception ex) {
            System.out.println(ex);
        }

        //Disegno il grafo
        myGraph.drawGraph();
    }
}
