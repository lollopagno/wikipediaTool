package model;

import java.util.ArrayList;

public class SequenceInfo {
    private ArrayList<Integer> numbers;
    private int rightPlaceNumbers;
    private int rightNumbers;

    public SequenceInfo(ArrayList<Integer> numbers, int rightNumbers, int rightPlaceNumbers){
        this.numbers = numbers;
        this.rightNumbers = rightNumbers;
        this.rightPlaceNumbers = rightPlaceNumbers;
    }

    public ArrayList<Integer> getNumbers() {
        return numbers;
    }

    public int getRightPlaceNumbers() {
        return rightPlaceNumbers;
    }

    public int getRightNumbers() {
        return rightNumbers;
    }
}
