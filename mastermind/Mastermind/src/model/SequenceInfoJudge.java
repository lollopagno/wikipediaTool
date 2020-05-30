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
    public PlayerInfo getNextPlayer(int currentIndex){

        if (currentIndex+1 < this.players.size()) {
            return this.players.get(currentIndex);
        }

        return null;
    }

    // Ricalcola l'ordine dei players per ogni turno
    public void newOrderTurn(){

        // Array temporaneo all'array players per gestire l'estrazione dei player
        ArrayList<PlayerInfo> playersTmp;
        playersTmp = this.players;

        ArrayList<PlayerInfo> newOrder = new ArrayList<>();

        for(int i =0; i < this.players.size(); i++) {

            // Scelgo un numero random
            int random = this.rand.nextInt(playersTmp.size());

            //Inserisco nel nuovo ordine l'elemento estratto
            newOrder.add((playersTmp.remove(random)));

            // Aggiungo l'ultimo player visto che è rimasto solo lui
            if(playersTmp.size() == 1){
                newOrder.add(playersTmp.remove(0));
            }
        }

        this.players = (ArrayList<PlayerInfo>) newOrder.clone();
    }

    // Visualizza stato ordine dell'ordine dei player rispetto al turno corrente
    public ArrayList<String> showTurn(){

        ArrayList<String> orderPlayer = new ArrayList<>();

        for (PlayerInfo player : this.players) {
            orderPlayer.add(player.getName());
        }
        return orderPlayer;
    }


    private void log(String message){
        synchronized (System.out){
            System.out.println(message);
        }
    }
}
