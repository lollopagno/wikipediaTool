package info;

import java.util.ArrayList;
import java.util.Random;

public class PlayerInfo {
    public ArrayList<MastermindNumber> number;

    public ArrayList<Integer> getSequence(){
        ArrayList<Integer> sequence = new ArrayList<>();
        Random r = new Random();
        number.forEach(elem -> {
            if(!elem.getOk())
                elem.setNumber(r.nextInt(10));
            sequence.add(elem.getNumber());
        });
        return null;// Di una nuova sequenza generata dinamicamente in base alla precedente.
    }
}
