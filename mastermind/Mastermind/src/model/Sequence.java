package model;

import java.util.ArrayList;
import java.util.List;

public interface Sequence {
    List<Integer> getSequence();

    void setSequence(ArrayList<Integer> numbers);

    SequenceInfoGuess tryNumbers(ArrayList<Integer> numbers);
}
