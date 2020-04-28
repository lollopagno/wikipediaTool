package ass2.model.services;

import ass2.model.classes.WikiLink;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class WikiClient {

    private String baseUrl;

    public WikiClient() {
        this.baseUrl = "https://it.wikipedia.org/w/api.php";
    }

    /**
     * Fa la GET dell'URL passato
     */
    public Set<WikiLink> parseURL(String concept) throws Exception {

        // TODO mettere Retrofit per la GET dell'URL

        StringBuilder result = new StringBuilder();
        URL url = new URL(this.baseUrl + "?action=parse&page=" + concept +
                "&format=json&section=0&prop=links");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream input;
        try {
            input = conn.getInputStream();
        } catch (IOException e) {
            this.log("No concept for " + concept);
            return null;
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        // Parse del URL per estrarre i link
        return this.extractLink(result.toString(), concept);
    }

    /**
     * Estrae i links del URL parsato.
     */
    private Set<WikiLink> extractLink(String result, String concept) {
        Set<WikiLink> links = new HashSet<>();
        JsonObject json = new Gson().fromJson(result, JsonObject.class);
        json = json.get("parse").getAsJsonObject();
        for (JsonElement elem : json.get("links").getAsJsonArray()) {
            if(elem.getAsJsonObject().get("exists") != null) {
                links.add(new WikiLink(elem, concept));
            }
        }
        return links;
    }

    private void log(String msg) {
        synchronized (System.out) {
            System.out.println("[" + Thread.currentThread().getName() + "] " + msg);
        }
    }
}