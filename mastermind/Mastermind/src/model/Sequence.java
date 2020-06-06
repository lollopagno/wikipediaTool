package model;

import java.util.List;

/**
 * Represent the list of numbers to find for each player.
 */
public interface Sequence extends Comparable<Sequence> {
    /**
     * Return the list of numbers.
     * @return
     */
    List<Integer> getSequence();

    /**
     * Set the list of numbers.
     * @param numbers List of numbers.
     */
    void setSequence(List<Integer> numbers);

    /**
     * Try a guess by another player.
     * @param guess The list of numbers from another player.
     * @return Info about the guess.
     */
    SequenceInfoGuess tryGuess(Sequence guess);
}
