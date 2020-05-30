package info;

import actors.PlayerActor;
import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import model.Sequence;
import model.SequenceImpl;
import model.SequenceInfoGuess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerInfo {
    private final String name;
    private final ActorRef reference;
    // TODO: Capire cosa sia questo player. Deve essere un indice? Identificativo? (In quel caso non basta il nome?
    private final int player;

    private Sequence sequence;
    SequenceInfoGuess last1try, last2try;

    public PlayerInfo(String name, ActorContext context, int player) {
        this.name = name;
        this.reference = context.actorOf(Props.create(PlayerActor.class), name);
        this.player = player;
        this.last1try = this.last2try = null;
    }

    public String getName() {
        return this.name;
    }

    public ActorRef getReference() {
        return reference;
    }

    public void generateSequence(int length) {
        this.sequence = createNumber(length);
    }

    public Sequence getSequence() {
        return this.sequence;
    }

    /**
     * The Player Sequence is solved if all numbers of the last try are right placed.
     * @return True if is solved, false then.
     */
    public boolean isSolved() {
        return this.last1try.getRightPlaceNumbers() == this.sequence.getSequence().size();
    }

    /**
     * Save the previous guess only if is better than the second before.
     * @param guess Guess to save.
     */
    public void setTry(SequenceInfoGuess guess){
        if(guess.getRightPlaceNumbers() > this.last1try.getRightPlaceNumbers()) {
            this.last2try = this.last1try;
            this.last1try = guess;
        }
    }

    public Sequence extractGuess(){
        // TODO: Da completare. Ritornare una sequenza coerente con i tentativi precedenti.
        return createNumber(this.sequence.getSequence().size());
    }

    /**
     * Create my random sequence.
     * @param length Sequence length.
     * @return Generated sequence.
     */
    private Sequence createNumber(int length){
        Random r = new Random();
        List<Integer> number = new ArrayList<>();
        for(int i = 0; i< length; i++){
            number.add(r.nextInt(10));
        }
        return new SequenceImpl(number);
    }
}