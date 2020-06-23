package app;

import com.google.gson.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class RequestClient {

    final String imagePath = "src/main/java/app/bletchley-park-mansion.jpg";
    private final int x;
    private final int y;

    private final View view;

    public RequestClient(View view, int x, int y){
        this.view = view;
        this.x = x;
        this.y = y;
    }

    /**
     * HTTP POST to register a user
     * @param username name of a user
     */
    public void registerUser(String username) {

        //TODO da cambiare in chiamata POST quando Daniele crea l'API
        //Documentazione metodi HTTP: https://mkyong.com/java/how-to-send-http-request-getpost-in-java/

        String url = "https://java-travis-ci.herokuapp.com/players/";

        try {

            HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();
            httpClient.setRequestMethod("POST");

            // Send post request
            httpClient.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
                wr.writeBytes(username);
                wr.flush();
            }

            int responseCode = httpClient.getResponseCode();
            log("\nSending 'POST'  request to URL : " + url);
            log("Response Code : " + responseCode);

        }catch (Exception ex){
            log("Error url POST " + ex.getMessage());
        }

        // Get list user
        ArrayList<String> users = this.listUser();

        // Update view
        this.view.updateListUser(users);

        // Start game
        this.startGame();
    }

    /**
     * HTTP GET for extract list user
     * @return list of users in the game
     */
    private ArrayList<String> listUser() {

        StringBuilder resultAPI = new StringBuilder();
        ArrayList<String> response = new ArrayList();
        String url = "https://java-travis-ci.herokuapp.com/players";

        try {

            HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();
            httpClient.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()))) {
                String line;

                while ((line = in.readLine()) != null) {
                    resultAPI.append(line);
                }
            }

            int responseCode = httpClient.getResponseCode();
            log("\nSending 'GET' request to URL : " + url);
            log("Response Code : " + responseCode);

            if(responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);

            }else{

                // Convert to JsonArray
                JsonObject jsonResult = new Gson().fromJson(resultAPI.toString(), JsonObject.class);

                // Convert to ArrayList
                JsonArray jArray = jsonResult.getAsJsonArray();
                if (jArray != null) {
                    for (int i=0;i<jArray.size();i++){
                        response.add(jArray.get(i).toString());
                    }
                }
            }
        }catch (Exception ex){
            log("Error url GET " + ex.getMessage());
        }

        return response;
    }

    /**
     * Start puzzle game
     */
    public void startGame(){
        final PuzzleBoard puzzle = new PuzzleBoard(this.x, this.y, this.imagePath);
        puzzle.setVisible(true);
    }

    private void log(String msg){
        System.out.println(msg);
    }
}
