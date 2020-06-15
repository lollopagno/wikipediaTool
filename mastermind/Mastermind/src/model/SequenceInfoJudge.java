package model;

import info.PlayerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Memorizza l'array di players e (forse) gestisce il prossimo players di un turno e ricalcola l'ordine per ogni turno
public class SequenceInfoJudge {
    ArrayList<PlayerInfo> players;
    int playerIndex;

    // Setta i players di una partita
    public SequenceInfoJudge(List<PlayerInfo> players) {
        playerIndex = 0;
        this.players = (ArrayList<PlayerInfo>)((ArrayList<PlayerInfo>)players).clone();
    }

    /**
     * Get the next player. Increment the player index.
     * Calculate a new turn if needed.
     * @return Turn player info.
     */
    public PlayerInfo getNextPlayer() {
        if (playerIndex + 1 > this.players.size())
            newOrderTurn();

        return this.players.get(playerIndex++);
    }

    /**
     * Generate a new turn sequence. Reset the player index.
     */
    public void newOrderTurn() {

        Random rand = new Random();
        List<PlayerInfo> playersTmp = this.players;
        ArrayList<PlayerInfo> newOrder = new ArrayList<>();
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

    /**
     * Visualizza stato ordine dell'ordine dei player rispetto al turno corrente
     * @return Array List.
     */
    public ArrayList<String> showTurn() {

        ArrayList<String> orderPlayer = new ArrayList<>();

        for (PlayerInfo player : this.players) {
            orderPlayer.add(player.getName());
        }
        return orderPlayer;
    }
}
