package ass2;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WikipediaClient {

    private final GuiInterface gui;
    private final MyGraph graph;
    private final Gson gson = new Gson();

    public WikipediaClient(GuiInterface gui, MyGraph graph) {
        this.gui = gui;
        this.graph = graph;
    }

    //Fa la GET dell'URL passato
    public void parseURL() throws Exception {

        // TODO mettere Retrofit per la GET dell'URL

        StringBuilder result = new StringBuilder();
        URL url = new URL("https://it.wikipedia.org/w/api.php?action=parse&page="+this.gui.getConcept()+
                "&format=json&section=0&prop=links");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        //Parse del URL per estrarre i link
        this.extractLink(result.toString());

    }

    //Estrae i links del URL parsato
    private void extractLink(String result){

        JsonObject json = new Gson().fromJson(result, JsonObject.class);
        json = json.get("parse").getAsJsonObject();
        JsonArray jsonArray = json.get("links").getAsJsonArray();

        // Inserisco i link nel grafico
        // TODO capire come memorizzare il vertice corrente
        graph.insertVertex(jsonArray, gui.getConcept());
    }

    //Log
    void log(String msg){
        System.out.println(msg);
    }
}
