package app;

import app.remoteservices.Players;
import app.remoteservices.Puzzle;
import app.remoteservices.RemoteServices;
import app.remoteservices.ReturnMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServicesTests {
    Puzzle puzzle;
    Players players;

    @BeforeEach public void beforeEach() {
        puzzle = RemoteServices.getInstance().getPuzzleService();
        players = RemoteServices.getInstance().getPlayersService();
    }

    @Test public void testAddPlayers() {
        Call<ReturnMessage> msg = players.addPlayer("daniele");
        msg.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ReturnMessage> call, Response<ReturnMessage> response) {
                if(response.isSuccessful()) {
                    assertNotNull(response.body());
                    assertTrue(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<ReturnMessage> call, Throwable t) {
                assertNotNull(t);
            }
        });
        assertNotNull(msg);
    }

    @Test public void testDeletePlayers(){
        Call<ReturnMessage> msg = players.deletePlayer("daniele");
        msg.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ReturnMessage> call, Response<ReturnMessage> response) {
                if (response.isSuccessful()) {
                    Call<List<String>> pl = players.allPlayers();
                    pl.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<List<String>> call1, Response<List<String>> response1) {
                            assertTrue(response1.isSuccessful());
                            assertNotNull(response1.body());
                            assertTrue(response1.body().stream().anyMatch(f -> f.equals("daniele")));
                        }

                        @Override
                        public void onFailure(Call<List<String>> call, Throwable t) {
                            assertNotNull(t);
                        }
                    });
                    assertNotNull(pl);
                }
            }

            @Override
            public void onFailure(Call<ReturnMessage> call, Throwable t) {
                assertNotNull(t);
            }
        });
        assertNotNull(msg);
    }
}
