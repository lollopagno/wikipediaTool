package actors.messages;

public class NumberAnswer {

    private int rightNumbers;
    private int rightPlaceNumbers;

    public NumberAnswer(int rightNumbers, int rightPlaceNumbers) {

        this.rightNumbers = rightNumbers;
        this.rightPlaceNumbers = rightPlaceNumbers;
    }

    public int getRightNumbers(){ return this.rightNumbers;}

    public int getRightPlaceNumbers(){ return this.rightPlaceNumbers;}
}
