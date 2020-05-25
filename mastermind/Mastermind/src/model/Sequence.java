package model;

import java.util.ArrayList;

public interface Sequence {
    ArrayList<Integer> getSequence();

    void setSequence(ArrayList<Integer> numbers);

    SequenceInfoGuess tryNumbers(ArrayList<Integer> numbers);
}
