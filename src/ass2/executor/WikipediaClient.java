package ass2.executor;

import ass2.GuiInterface;
import ass2.MyGraph;
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
    private JsonArray arrayLinks;

    public WikipediaClient(GuiInterface gui, MyGraph graph) {
        this.gui = gui;
        this.graph = graph;
    }

    //Fa la GET dell'URL passato
    public void parseURL(String concept) throws Exception {

        // TODO mettere Retrofit per la GET dell'URL

        StringBuilder result = new StringBuilder();
        URL url = new URL("https://it.wikipedia.org/w/api.php?action=parse&page="+concept+
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
        this.extractLink(result.toString(), concept);

    }

    //Estrae i links del URL parsato
    private void extractLink(String result, String concept){

        JsonObject json = new Gson().fromJson(result, JsonObject.class);
        json = json.get("parse").getAsJsonObject();
        this.arrayLinks = json.get("links").getAsJsonArray();

        // Inserisco i link nel grafico
        graph.insertVertex(this.arrayLinks, concept);
    }

    public int getLenghtLinks(){
        return this.arrayLinks.size();
    }

    public String getElemArray(int i){
        JsonObject elem = (JsonObject) this.arrayLinks.get(i);
        return elem.get("*").getAsString();
    }

    //Log
    void log(String msg){
        System.out.println(msg);
    }
}
