package app.remoteservices;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface Players {
    @GET("/players")
    Call<List<String>> allPlayers();

    @POST("/players")
    Call<ReturnMessage> addPlayer(@Body final String user);

    @DELETE("/players/{name}")
    Call<ReturnMessage> deletePlayer(@Path("name") final String name);
}
