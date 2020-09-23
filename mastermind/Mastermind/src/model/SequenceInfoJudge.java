package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SequenceInfoJudge {
    ArrayList<PlayerReference> players;
    int playerIndex;

    // Setta i players di una partita
    public SequenceInfoJudge(List<PlayerReference> players) {
        playerIndex = 0;
        this.players = (ArrayList<PlayerReference>) ((ArrayList<PlayerReference>) players).clone();
    }

    /**
     * Get the next player. Increment the player index.
     * Calculate a new turn if needed.
     *
     * @return Turn player info.
     */
    public PlayerReference getNextPlayer() {
        if (playerIndex + 1 > this.players.size())
            newOrderTurn();

        return this.players.get(playerIndex++);
    }

    /**
     * Generate a new turn sequence. Reset the player index.
     */
    public void newOrderTurn() {
        Random rand = new Random();
        List<PlayerReference> playersTmp = this.players;
        ArrayList<PlayerReference> newOrder = new ArrayList<>();
        int numberActor = this.players.size();

        // Put a random player in the order.
        for (int i = 0; i < numberActor; i++) {

            int random = rand.nextInt(playersTmp.size());
            newOrder.add(playersTmp.remove(random));
        }

        this.players = newOrder;
        playerIndex = 0;
    }

    public int getNPlayers() {
        return this.players.size();
    }

    public ArrayList<PlayerReference> showPlayer() {
        return this.players;
    }
}
