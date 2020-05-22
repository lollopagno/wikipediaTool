package views.components;

import java.util.List;

public class PlayerSolution {
    private final String name;
    private List<Integer> sequence;
    private int rightPlacedNumbers;
    private int rightNumbers;

    public PlayerSolution(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getSequence() {
        return sequence;
    }

    public int getRightPlacedNumbers() {
        return rightPlacedNumbers;
    }

    public int getRightNumbers() {
        return rightNumbers;
    }

    public void setSequence(List<Integer> sequence) {
        this.sequence = sequence;
    }

    public void setRightPlacedNumbers(int rightPlacedNumbers) {
        this.rightPlacedNumbers = rightPlacedNumbers;
    }

    public void setRightNumbers(int rightNumbers) {
        this.rightNumbers = rightNumbers;
    }
}
