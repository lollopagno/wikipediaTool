package ass2;

public class Main {

    public static void main(String[] args) {

        //Gui
        GuiInterface gui = new GuiInterface();
        gui.setConcept();
        gui.setLevel();

        //Graph
        MyGraph myGraph = new MyGraph(gui);

        //Wikipedia Client
        WikipediaClient wc = new WikipediaClient(gui, myGraph);

        try{
            wc.parseURL();

            //Disegna il grafo
            myGraph.drawGraph();

        }catch (Exception ex) {
            System.out.println("Non è stato possibile trovare nessun link per il concetto specificato!");
        }
    }
}
