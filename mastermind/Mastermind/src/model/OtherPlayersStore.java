package model;

import actors.messages.NumberAnswer;
import akka.actor.ActorRef;
import info.PlayerInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OtherPlayersStore {
    private List<PlayerInfo> others;

    public OtherPlayersStore(){
        this.others = new LinkedList<>();
    }

    public void addPlayer(PlayerInfo info) {
        this.others.add(info);
    }

    /**
     * Save a guess made to another player.
     * @param name Another player name.
     * @param guess Guess tried.
     */
    public void saveGuess(String name, SequenceInfoGuess guess) {
        others.stream()
                .filter(f ->
                        f.getName().equals(name))
                .findFirst()
                .ifPresent(player -> {
            player.setTry(guess);
        });
    }

    /**
     * Send the number response to all players except the sender.
     * @param response Response to send.
     * @param self Actor ref to self.
     */
    public void notifyOtherPlayersAboutResponse(SequenceInfoGuess response, ActorRef self){
        others.forEach(playerInfo ->
                playerInfo
                        .getReference()
                        .tell(
                                new NumberAnswer(
                                        response.getRightNumbers(),
                                        response.getRightPlaceNumbers()
                                ),
                                self));
    }

    public Optional<PlayerInfo> getNextUnsolvedPlayer() {
        StringBuilder builder = new StringBuilder();
        this.others.forEach(info -> {
            if(!info.isSolved()) {
                builder.append(info.getName());
            }
        });
        String name = builder.toString();
        return others.stream().filter(f -> f.getName().equals(name)).findFirst();
    }
}
