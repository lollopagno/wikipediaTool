package app;

import app.remoteservices.RemoteServices;
import app.remoteservices.ReturnMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class RequestClient {
    private static RequestClient singleton;

    public static RequestClient instance() {
        if (singleton == null)
            singleton = new RequestClient();
        return singleton;
    }

    private RequestClient() {}

    final String imagePath = "src/main/java/app/bletchley-park-mansion.jpg";

    /**
     * HTTP POST for register user name
     *
     * @param name   user name
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
     *
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
     *
     * @param name user name
     */
    public void deleteUser(String name) {
        Call<ReturnMessage> call = RemoteServices.getInstance().getPlayersService().deletePlayer(name);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<ReturnMessage> call, Response<ReturnMessage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    log(response.body().getMessage());
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
     *
     * @param name   user name
     * @param button tile button
     */
    public void takeBox(String name, TileButton button) {

        Call<ReturnMessage> call = RemoteServices.getInstance().getPuzzleService().take(name, button.getTile().getOriginalPosition());
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<ReturnMessage> call, Response<ReturnMessage> response) {
                if (response.isSuccessful() && response.body() != null) {

                    button.setBorder(BorderFactory.createLineBorder(Color.red));
                    button.setColor(Color.red);
                    button.getTile().setTaker(name);

                    log("Take box id: " + button.getTile().getOriginalPosition());
                    log(response.body().getMessage() + "");
                }
            }

            @Override
            public void onFailure(Call<ReturnMessage> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

    /**
     * HTTP GET for get state of specific boxe
     *
     * @param name   name user
     * @param id     id box
     * @param action lambda-function
     */
    public void checkStateBox(String name, Integer id, Consumer<String> action) {

        Call<ReturnMessage> call = RemoteServices.getInstance().getPuzzleService().getState(name, id);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<ReturnMessage> call, Response<ReturnMessage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getMessage().equals("true")) {
                        action.accept("true");
                    } else {
                        action.accept("false");
                    }
                }
            }

            @Override
            public void onFailure(Call<ReturnMessage> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

    /**
     * HTTP PUT for release a boxe
     *
     * @param name   name user
     * @param button tile button
     */
    public void releaseBox(String name, TileButton button) {
        Call<ReturnMessage> call = RemoteServices.getInstance().getPuzzleService().release(name, button.getTile().getOriginalPosition());
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<ReturnMessage> call, Response<ReturnMessage> response) {
                if (response.isSuccessful() && response.body() != null) {

                    button.setBorder(BorderFactory.createLineBorder(Color.gray));
                    button.setColor(Color.gray);

                    log("Release, position box id: " + button.getTile().getOriginalPosition());
                    log(response.body().getMessage() + "");
                }
            }

            @Override
            public void onFailure(Call<ReturnMessage> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

    /**
     * HTTP GET for get list tile
     *
     * @param action lambda-function
     */
    public void mappingBox(Consumer<List<Tile>> action) {
        Call<List<Tile>> call = RemoteServices.getInstance().getPuzzleService().getMappings();
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<List<Tile>> call, Response<List<Tile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    action.accept(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Tile>> call, Throwable t) {
                log(t.getMessage());
            }
        });

    }

    /**
     * HTTP PUT for move the box
     *
     * @param name   name user
     * @param id     initial position
     * @param id2    final position
     * @param action lambda-function
     */
    public void moveBox(String name, Integer id, Integer id2, Consumer<String> action) {

        log("Move id: " + id + " with id: " + id2);
        Call<Boolean> call = RemoteServices.getInstance().getPuzzleService().move(name, id, id2);
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
        final PuzzleBoard puzzle = new PuzzleBoard(this.imagePath, username);
        puzzle.setVisible(true);
    }

    private void log(String msg) {
        synchronized (System.out) {
            System.out.println("[Info] " + msg);
        }
    }
}