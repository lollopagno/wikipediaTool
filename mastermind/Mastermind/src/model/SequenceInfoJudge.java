package model;

import info.PlayerInfo;

import java.util.ArrayList;
import java.util.Random;

// Memorizza l'array di players e (forse) gestisce il prossimo players di un turno e ricalcola l'ordine per ogni turno
public class SequenceInfoJudge {

    public ArrayList<PlayerInfo> players;
    private Random rand = new Random();

    // Setta i players di una partita
    public SequenceInfoJudge(ArrayList<PlayerInfo> players){

        this.players = (ArrayList<PlayerInfo>) players.clone();
    }

    // Restituisce il prossimo giocatore di un turno
    public PlayerInfo getNextPlayers(int currentIndex){

        if (currentIndex+1 < this.players.size()) {
            return this.players.get(currentIndex+1);
        }

        return null;
    }

    // Ricalcola l'ordine dei players per ogni turno
    public void newOrderTurn(){

        ArrayList<PlayerInfo> newOrder = new ArrayList<>();

        for(int i = 0; i<this.players.size(); i++) {

            // Scelgo un numero random
            int random = this.rand.nextInt(this.players.size());

            //Inserisco nel nuovo ordine l'elemento estratto
            newOrder.add(this.players.remove(random));
        }

        this.players = (ArrayList<PlayerInfo>) newOrder.clone();
    }
}
