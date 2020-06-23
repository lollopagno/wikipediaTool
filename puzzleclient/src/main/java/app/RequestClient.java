package app;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class RequestClient {

    final String imagePath = "src/main/java/app/bletchley-park-mansion.jpg";
    private final int x;
    private final int y;

    private HttpsURLConnection httpClient;

    public RequestClient(View view, int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * HTTP POST to register a user
     * @param username name of a user
     */
    public ArrayList<String> registerUser(String username) {

        //Documentazione metodi HTTP: https://mkyong.com/java/how-to-send-http-request-getpost-in-java/

        String url = "https://java-travis-ci.herokuapp.com/players/";

        try {

            this.httpClient = (HttpsURLConnection) new URL(url).openConnection();
            this.httpClient.setRequestMethod("POST");

            // Send post request
            this.httpClient.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(this.httpClient.getOutputStream())) {
                wr.writeBytes(username);
                wr.flush();
            }

            int responseCode = this.httpClient.getResponseCode();
            log("\nSending 'POST'  request to URL : " + url);
            log("Response Code : " + responseCode);

        }catch (Exception ex){
            log("Error url POST " + ex.getMessage());
        }

        // Get list user
        return this.listUser();
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
            this.httpClient = (HttpsURLConnection) new URL(url).openConnection();
            this.httpClient.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(this.httpClient.getInputStream()))) {
                String line;

                while ((line = in.readLine()) != null) {
                    resultAPI.append(line);
                }
            }

            int responseCode = this.httpClient.getResponseCode();
            log("\nSending 'GET' request to URL : " + url);
            log("Response Code : " + responseCode);

            if(responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);

            }else{
                System.out.println(resultAPI);
                // Convert to JsonArray
                JsonArray jsonArray = new JsonParser().parse(resultAPI.toString()).getAsJsonArray();

                // Convert to ArrayList
                if (jsonArray != null) {
                    for (int i=0; i<jsonArray.size(); i++){
                        response.add(jsonArray.get(i).toString());
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
