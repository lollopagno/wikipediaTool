package app;

import app.remoteservices.RemoteServices;
import app.remoteservices.ReturnMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.function.Consumer;

public class RequestClient {

    final String imagePath = "src/main/java/app/bletchley-park-mansion.jpg";
    private final int x;
    private final int y;

    public RequestClient(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * HTTP POST for register user name
     * @param name user name
     * @param action lambda-function
     */
    public void addPlayer(String name, Consumer<String> action) {

        Call<ReturnMessage> add = RemoteServices.getInstance().getPlayersService().addPlayer(name);
        add.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<ReturnMessage> call, Response<ReturnMessage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    action.accept(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ReturnMessage> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

    /**
     * HTTP GET for extract list user
     * @param action lambda-function
     */
    public void allUsers(Consumer<List<String>> action) {
        Call<List<String>> res = RemoteServices.getInstance().getPlayersService().allPlayers();
        res.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    action.accept(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

    /**
     * HTTP DELETE for delete user from server
     * @param name user name
     * @param action lambda-function
     */
    public void deleteUser(String name, Consumer<String> action) {

        Call<ReturnMessage> call = RemoteServices.getInstance().getPlayersService().deletePlayer(name);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<ReturnMessage> call, Response<ReturnMessage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    action.accept(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ReturnMessage> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

    /**
     * HTTP PUT for take a boxe
     * @param name user name
     * @param id id boxe
     * @param action lambda-function
     */
    public void takeBox(String name, Integer id, Consumer<String> action){

        log("take box id: "+id);
        Call<ReturnMessage> call = RemoteServices.getInstance().getPuzzleService().take(name, id);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<ReturnMessage> call, Response<ReturnMessage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    action.accept(response.toString());
                }
            }

            @Override
            public void onFailure(Call<ReturnMessage> call, Throwable t) {log(t.getMessage());}
        });
    }

    public void releaseBox(String name, Integer id, Consumer<String> action){

        log("Release, position box id: "+id);
        Call<ReturnMessage> call = RemoteServices.getInstance().getPuzzleService().release(name,id);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<ReturnMessage> call, Response<ReturnMessage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    action.accept(response.toString());
                }
            }

            @Override
            public void onFailure(Call<ReturnMessage> call, Throwable t) {log(t.getMessage());}
        });
    }
    public void moveBox(String name, Integer id,Integer id2, Consumer<String> action){

        log("Move "+id + "with "+id2);
        Call<Boolean> call = RemoteServices.getInstance().getPuzzleService().move(name,id, id2);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    action.accept(response.toString());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

    /**
     * Start puzzle game
     */
    public void startGame(String username) {
        final PuzzleBoard puzzle = new PuzzleBoard(this.x, this.y, this.imagePath, username);
        puzzle.setVisible(true);
    }

    private void log(String msg) {
        synchronized (System.out) {
            System.out.println("[Info] "+msg);
        }
    }
}