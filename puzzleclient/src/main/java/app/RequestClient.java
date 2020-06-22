package app;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class RequestClient {

    final int n = 3;
    final int m = 5;

    final String imagePath = "src/main/java/app/bletchley-park-mansion.jpg";

    // POST per registrare un utente
    public void registerUser(String username) throws Exception{

        //TODO da cambiare in chiamata POST quando Daniele crea l'API
        //Documentazione metodi HTTP: https://mkyong.com/java/how-to-send-http-request-getpost-in-java/

        String url = "https://java-travis-ci.herokuapp.com/players/"+username;
        HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();

        httpClient.setRequestMethod("GET");

    }

    // GET per mostrare la lista degli utenti registrati
    public ArrayList<String> listUser() throws Exception{

        String url = "https://java-travis-ci.herokuapp.com/players";
        HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();

        httpClient.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            ArrayList<String> response = new ArrayList<>();
            String line;

            while ((line = in.readLine()) != null) {
                response.add(line);
            }

            return response;
        }
    }

    public void startGame(){

        final PuzzleBoard puzzle = new PuzzleBoard(n, m, imagePath);
        puzzle.setVisible(true);
    }

}
