package model;

import java.util.*;

public class SequenceImpl implements Sequence {
    List<Integer> numbers;

    public SequenceImpl(List<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    // Ritorna la sequenza random scelta da un players
    public List<Integer> getSequence() {
        return numbers;
    }

    @Override
    // Setta la sequenza random scelta da un players
    public void setSequence(List<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    public SequenceInfoGuess tryGuess(Sequence guess) {
        List<Integer> sequence = guess.getSequence();
        int rightNumbers = 0;
        int rightPlaceNumbers = 0;

        // Memorizza se un numero l'ho già conteggiato oppure no.
        List<Boolean> visited = new LinkedList<>();
        sequence.forEach(e -> visited.add(true));

        // Verifico ogni singolo numero del guess.
        for (int iGuess = 0; iGuess < sequence.size(); iGuess++) {
            int nGuess = sequence.get(iGuess);
            // Lo cerco nella lista del players.
            for (int iPlayer = 0; iPlayer < this.numbers.size(); iPlayer++) {
                if (nGuess == this.numbers.get(iPlayer)) {
                    // Ho trovato il numero e verifico se è nella giusta posizione.
                    // In questo ciclo contava 2 volte i numeri giusti.
                    if (iGuess == iPlayer)
                        rightPlaceNumbers++;
                    else if(visited.get(iPlayer)){
                        visited.set(iPlayer, false);
                        rightNumbers++;
                    }
                }
            }
        }
        return new SequenceInfoGuess(guess, rightNumbers, rightPlaceNumbers);
    }

    @Override
    public String toString() {
        Optional<String> value = this.numbers.stream()
                .map(Object::toString)
                .reduce(String::concat);
        if (!value.isPresent())
            throw new NumberFormatException("The sequence is not a good number.");
        return value.get();
    }

    @Override
    public int compareTo(Sequence sequence) {
        if(numbers.size() != sequence.getSequence().size())
            throw new IllegalArgumentException("Comparation of sequences of different size.");

        for(int i = 0; i < numbers.size(); i++){
            int order = numbers.get(i).compareTo(sequence.getSequence().get(i));
            // If there's an order, I'll return it.
            if(order != 0)
                return order;
        }
        
        // Return that the sequences are equals.
        return 0;
    }
}
