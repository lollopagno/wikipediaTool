package model;

import java.util.ArrayList;

public class SequenceImpl implements Sequence{
    ArrayList<Integer> numbers;

    @Override
    public ArrayList<Integer> getSequence() {
        return numbers;
    }

    @Override
    public void setSequence(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    public SequenceInfo tryNumbers(ArrayList<Integer> numbers) {
        return new SequenceInfo(this.numbers, 1, 1);
    }
}
