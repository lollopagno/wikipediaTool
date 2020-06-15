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
        int rightPlaceNumbers = 0;

        // Memorizza se un numero l'ho già conteggiato oppure no.
        List<Boolean> visited = new LinkedList<>();
        sequence.forEach(e -> visited.add(false));

        List<Integer> rights = new LinkedList<>();
        for(int i = 0; i < 10; i++)
            rights.add(0);

        // Verifico ogni singolo numero del guess.
        for (int iGuess = 0; iGuess < sequence.size(); iGuess++) {
            int nGuess = sequence.get(iGuess);
            // Lo cerco nella lista del player.
            for (int iPlayer = 0; iPlayer < this.numbers.size(); iPlayer++) {
                if (nGuess == this.numbers.get(iPlayer)) {
                    // Ho trovato il numero e verifico se è nella giusta posizione.
                    if (iGuess == iPlayer) {
                        // Se è anche nella giusta posizione passo al ciclo successivo.
                        rightPlaceNumbers++;
                        rights.set(nGuess, rights.get(nGuess) - 1);
                        /*
                        // Se lo avevo già messo nei numeri giusti allora lo tolgo.
                        if(visited.get(iPlayer)){
                            visited.set(iPlayer, false);
                            rightCount = false;
                            rightNumbers--;
                        }
                        */

                        break;
                    }

                    if(!visited.get(iPlayer)) {
                        rights.set(nGuess, rights.get(nGuess) + 1);
                        visited.set(iPlayer, true);
                    }
                }
            }
        }

        int rightNumbers = 0;
        for(int i = 0; i < 10; i++) {
            int elem = rights.get(i);
            rightNumbers += Math.max(elem, 0);
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
