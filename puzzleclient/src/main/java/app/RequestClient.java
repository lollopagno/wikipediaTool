package app;

import app.remoteservices.RemoteServices;
import app.remoteservices.ReturnMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.Consumer;

public class RequestClient {

    final String imagePath = "src/main/java/app/bletchley-park-mansion.jpg";
    private final int x;
    private final int y;

    private HttpsURLConnection httpClient;

    //Documentazione metodi HTTP: https://mkyong.com/java/how-to-send-http-request-getpost-in-java/

    public RequestClient(RegisterView registerView, int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * HTTP POST to register a user
     *
     * @param username name of a user
     */
    public void registerUser(String username) {

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

            if (responseCode != 200) {
                throw new RuntimeException("Http POST failed: " + responseCode);
            }

        } catch (Exception ex) {
            log("Http POST failed:  " + ex.getMessage());
        }
    }

    /**
     * HTTP GET for extract list user
     *
     * @return list of users in the game
     */
    public ArrayList<String> listUser() {

        StringBuilder resultAPI = new StringBuilder();
        ArrayList<String> response = new ArrayList<>();
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

            if (responseCode != 200) {
                throw new RuntimeException("Http GET failed:  " + responseCode);
            }else{
                // Convert to JsonArray
                JsonArray jsonArray = new JsonParser().parse(resultAPI.toString()).getAsJsonArray();

                // Convert to ArrayList
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        response.add(jsonArray.get(i).toString());
                    }
                }
            }
        } catch (Exception ex) {
            log("Http GET failed:  " + ex.getMessage());
        }
        return response;
    }

    /**
     * HTTP DELETE for delete user from server
     */
    public void deleteUser(String name, Consumer<String> action) {
        Call<ReturnMessage> call = RemoteServices.getInstance().getPlayersService().deletePlayer(name);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ReturnMessage> call, Response<ReturnMessage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    log(response.body().getMessage());
                    action.accept(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ReturnMessage> call, Throwable t) {
                synchronized (System.out) {
                    System.out.println(t.getMessage());
                }
            }
        });
    }

    /**
     * Start puzzle game
     */
    public void startGame() {
        final PuzzleBoard puzzle = new PuzzleBoard(this.x, this.y, this.imagePath);
        puzzle.setVisible(true);
        deleteUser("pippo", this::log);
    }

    private void log(String msg) {
        synchronized (System.out) {
            System.out.println(msg);
        }
    }
}