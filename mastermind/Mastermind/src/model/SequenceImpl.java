package model;

import info.PlayerInfo;

import java.util.ArrayList;
import java.util.LinkedList;

public class SequenceImpl implements Sequence{

    ArrayList<Integer> numbers = new ArrayList<>();

    @Override
    // Ritorna la sequenza random scelta da un players
    public ArrayList<Integer> getSequence() {
        return numbers;
    }

    @Override
    //Setta la sequenza random scelta da un players
    public void setSequence(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    //Verifico il numero di cifre corrette (corrette al posto giusto, corrette ma non al posto giusto)
    public SequenceInfoGuess tryNumbers(ArrayList<Integer> numbers) {

        //TODO DA VERIFICARE CON TEST

        int rightNumbers = 0;
        int rightPlaceNumbers = 0;

        // Per ogni elemento del guess verifico se è presente nella stringa scelta dal players
        for(int indexGuess = 0; indexGuess<numbers.size(); indexGuess++){

            // Estraggo il numero del guess
            int numberGuess = numbers.get(indexGuess);

            //Lo cerco nella stringa del players
            for(int indexPlayer = 0; indexPlayer<this.numbers.size(); indexPlayer++){

                if (numberGuess == this.numbers.get(indexPlayer)){

                    // Ho trovato il numero
                    // Verifico se è nella giusta posizione
                    if(indexGuess == indexPlayer) rightPlaceNumbers++;
                    else rightNumbers++;
                    break;
                }
            }
        }

        return new SequenceInfoGuess(this.numbers, rightNumbers, rightPlaceNumbers);
    }
}
