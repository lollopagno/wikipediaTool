package app.remoteservices;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.Set;

public interface Puzzle {

    @PUT("/take/{player}/{id}")
    Call<ReturnMessage> take(@Path("player") final String player, @Path("id") final int id);

    @PUT("/release/{player}/{id}")
    Call<ReturnMessage> release(@Path("player") final String player, @Path("id") final int id);

    @GET("/state/{player}/{id}")
    Call<ReturnMessage> getState(@Path("player") final String player, @Path("id") final int id);

    @PUT("/move/{player}/{first}/{second}")
    Call<ReturnMessage> move(@Path("player") final String player, @Path("first") final int first, @Path("second") final int second);

    @GET("/mapping")
    Call<Set<Box>> getMappings();

    @DELETE("/game/reset")
    Call<ReturnMessage> gameReset();

}
