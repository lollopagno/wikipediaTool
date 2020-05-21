package info;

import java.util.Random;

public class MastermindNumber {
    private int number;
    private boolean ok;

    public MastermindNumber(int n){
        this.number = n;
        this.ok = false;
    }

    public int getNumber() {
        return this.number;
    }

    public boolean getOk() {
        return this.ok;
    }

    public void setNumber(int n) {
        this.number = number;
    }

    public void setFound() {
        this.ok = true;
    }
}
