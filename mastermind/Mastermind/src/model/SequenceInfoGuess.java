package model;

public class SequenceInfoGuess {
    private final Sequence numbers;
    private final int rightPlaceNumbers;
    private final int rightNumbers;

    public SequenceInfoGuess(Sequence numbers, int rightNumbers, int rightPlaceNumbers){
        this.numbers = numbers;
        this.rightNumbers = rightNumbers;
        this.rightPlaceNumbers = rightPlaceNumbers;
    }

    public Sequence getNumbers() {
        return numbers;
    }

    // Ritorna il numero delle cifre CORRETTE AL POSTO GIUSTO
    public int getRightPlaceNumbers() {
        return rightPlaceNumbers;
    }

    // Ritorna il numero delle cifre CORRETTE MA NON AL POSTO GIUSTO
    public int getRightNumbers() {
        return rightNumbers;
    }
}
