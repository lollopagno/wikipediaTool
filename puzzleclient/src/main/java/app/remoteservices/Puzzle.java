package app.remoteservices;

import app.Tile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface Puzzle {
    @GET("/mappings")
    Call<List<Tile>> getMappings();

    @PUT("/take/{player}/{id}")
    Call<Boolean> take(@Path("player") final String player, @Path("id") final int id);

    @PUT("/move/{player}/{first}/{second}")
    Call<Boolean> move(@Path("player") final String player, @Path("first") final int first, @Path("second") final int second);
}
