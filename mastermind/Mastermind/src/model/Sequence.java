package model;

import java.util.List;

/**
 * Represent the list of numbers to find for each player.
 */
public interface Sequence extends Comparable<Sequence> {
    /**
     * Return the list of numbers.
     *
     * @return The Integers List.
     */
    List<Integer> getSequence();

    /**
     * Try a guess by another player.
     *
     * @param guess The list of numbers from another player.
     * @return Info about the guess.
     */
    SequenceInfoGuess tryGuess(Sequence guess);
}
