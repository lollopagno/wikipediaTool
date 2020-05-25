package model;

import java.util.List;

public interface Sequence {
    List<Integer> getSequence();

    void setSequence(List<Integer> numbers);

    SequenceInfoGuess tryNumbers(List<Integer> numbers);
}
