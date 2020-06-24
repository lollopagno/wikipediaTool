package app.remoteservices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteServices {
    Players players;
    Puzzle puzzle;
    static RemoteServices instance;

    public static RemoteServices getInstance() {
        if(instance == null)
            instance = new RemoteServices();
        return instance;
    }

    private RemoteServices(){}

    private static final String BASE_URL = "https://java-travis-ci.herokuapp.com/";

    public Players getPlayersService() {
        if(players == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            players = retrofit.create(Players.class);
        }

        return players;
    }

    public Puzzle getPuzzleService() {
        if(puzzle == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            puzzle = retrofit.create(Puzzle.class);
        }

        return puzzle;
    }
}
