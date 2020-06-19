package actors.messages;

import model.PlayerReference;
import views.players.PlayersView;

import java.util.ArrayList;
import java.util.List;

public class StartMsg implements Message {
    private final int length;
    private final List<PlayerReference> allPlayers;
    private final PlayerReference player;
    private final PlayersView view;

    public StartMsg(int length, List<PlayerReference> players, PlayerReference player, PlayersView view)
    {
        this.length = length;
        this.allPlayers = new ArrayList<>(players);
        this.player = player;
        this.view = view;
    }

    public int getLength() {
        return length;
    }

    public List<PlayerReference> getAllPlayers() {
        return allPlayers;
    }

    public PlayerReference getPlayer() { return player; }

    public PlayersView getView(){ return  this.view; }
}
