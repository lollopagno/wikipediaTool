package app.remoteservices;

import app.Tile;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface Puzzle {
    @GET("/mappings")
    Call<List<Tile>> getMappings();
}
