package model;

import info.PlayerInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OtherPlayersStore {
    final int length;
    private final List<PlayerInfo> others;

    public OtherPlayersStore(int length) {
        this.others = new LinkedList<>();
        this.length = length;
    }

    public void addPlayer(PlayerInfo info) {
        this.others.add(info);
    }

    /**
     * Save a guess made to another player.
     *
     * @param name  Another player name.
     * @param guess Guess tried.
     */
    public void saveGuess(String name, SequenceInfoGuess guess) {
        others.stream()
                .filter(f ->
                        f.getName().equals(name))
                .findFirst()
                .ifPresent(player -> player.setTry(guess));
    }

    public Optional<PlayerInfo> getNextUnsolvedPlayer() {
        String name = "";
        for (PlayerInfo info : this.others) {
            if (!info.isSolved(length)) {
                name = name.concat(info.getName());
                break;
            }
        }

        if (name.isEmpty())
            return Optional.empty();

        String finalName = name;
        return others.stream().filter(f -> f.getName().equals(finalName)).findFirst();
    }
}
