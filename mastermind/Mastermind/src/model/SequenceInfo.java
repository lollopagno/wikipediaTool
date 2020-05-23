package model;

import java.util.List;

public class SequenceInfo {
    private final List<Integer> numbers;
    private final int rightPlaceNumbers;
    private final int rightNumbers;

    public SequenceInfo(List<Integer> numbers, int rightNumbers, int rightPlaceNumbers){
        this.numbers = numbers;
        this.rightNumbers = rightNumbers;
        this.rightPlaceNumbers = rightPlaceNumbers;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public int getRightPlaceNumbers() {
        return rightPlaceNumbers;
    }

    public int getRightNumbers() {
        return rightNumbers;
    }
}
