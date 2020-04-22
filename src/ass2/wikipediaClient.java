package ass2;

import retrofit2.Retrofit;

public class wikipediaClient {

    private final GuiInterface gui;
    private Retrofit retrofit;


    public wikipediaClient(GuiInterface gui) {
        this.gui = gui;
    }


    public void parseURL(){

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://it.wikipedia.org/w/api.php?action=parse&page="+this.gui.getConcept()+
                        "&format=json&section=0&prop=links")
                .build();

    }
}
