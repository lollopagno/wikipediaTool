package model;

import java.util.List;
import java.util.Optional;

public class SequenceImpl implements Sequence {
    List<Integer> numbers;

    public SequenceImpl(List<Integer> numbers){
        this.numbers = numbers;
    }

    @Override
    // Ritorna la sequenza random scelta da un players
    public List<Integer> getSequence() {
        return numbers;
    }

    @Override
    //Setta la sequenza random scelta da un players
    public void setSequence(List<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    //Verifico il numero di cifre corrette (corrette al posto giusto, corrette ma non al posto giusto)
    public SequenceInfoGuess tryNumbers(List<Integer> numbers) {
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

        return new SequenceInfoGuess(this, rightNumbers, rightPlaceNumbers);
    }

    @Override
    public String toString() {
        Optional<String> value = this.numbers.stream()
                .map(Object::toString)
                .reduce(String::concat);
        if(!value.isPresent())
            throw new NumberFormatException("The sequence is not a good number.");
        return value.get();
    }
}
