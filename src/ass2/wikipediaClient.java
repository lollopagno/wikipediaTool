package ass2;

import java.net.URL;

public class wikipediaClient {

    private final GuiInterface gui;

    public wikipediaClient(GuiInterface gui) {
        this.gui = gui;
    }


    public void parseURL() throws Exception{

        URL url = new URL("https://it.wikipedia.org/w/api.php?action=parse&page="+this.gui.getConcept()+
                        "&format=json&section=0&prop=links");



    }
}
