package info;

import actors.PlayerActor;
import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import model.Sequence;
import model.SequenceImpl;
import model.SequenceInfoGuess;

import java.util.*;
import java.util.function.Consumer;

public class PlayerInfo {
    private final String name;
    private final ActorRef reference;
    // TODO: Capire cosa sia questo player. Deve essere un indice? Identificativo? (In quel caso non basta il nome?
    private final int player;

    private Sequence sequence;
    SequenceInfoGuess last1try, last2try;
    Set<Sequence> alltries;

    public PlayerInfo(String name, ActorContext context, int player) {
        this.name = name;
        this.reference = context.actorOf(Props.create(PlayerActor.class), name);
        this.player = player;
        this.last1try = this.last2try = null;
        alltries = new TreeSet<>();
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
     *
     * @return True if is solved, false then.
     */
    public boolean isSolved() {
        return this.last1try != null && this.last1try.getRightPlaceNumbers() == this.sequence.getSequence().size();
    }

    /**
     * Save the previous guess only if is better than the second before.
     *
     * @param guess Guess to save.
     */
    public void setTry(SequenceInfoGuess guess) {
        if (this.last1try != null && guess.getRightPlaceNumbers() > this.last1try.getRightPlaceNumbers()) {
            this.last2try = this.last1try;
        }
        this.last1try = guess;
    }

    public Sequence extractGuess() {
        // TODO: Da completare. Ritornare una sequenza coerente con i tentativi precedenti.
        int length = this.sequence.getSequence().size();
        if (this.last1try != null && this.last2try != null) {
            return createElaborateSequence(length);
        }
        return createNumber(length);
    }

    /**
     * Create my random sequence.
     *
     * @param length Sequence length.
     * @return Generated sequence.
     */
    private Sequence createNumber(int length) {
        Random r = new Random();
        List<Integer> number = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            number.add(r.nextInt(10));
        }
        return new SequenceImpl(number);
    }

    private Sequence createElaborateSequence(int lenght) {
        Sequence seq;
        do {
            // TODO: This generation may be interrupted.
            seq = createNumber(length);
        } while (alltries.contains(seq));
        forEachRandomInit(seq.getSequence(), System.out::println);
        return seq;
    }

    /**
     * Utility method.
     * @param list List to fetch.
     * @param action Action to do.
     */
    public void forEachRandomInit(List<Integer> list, Consumer<? super Integer> action){
        // Generate the new random start index.
        int s = new Random().nextInt(list.size());
        // Start from random index. Repeat at max length times.
        for (int i = s, j = 0; j < list.size(); i++, j++) {
            if (i == list.size())
                i = 0;

            // Pass the current item to the consumer.
            action.accept(list.get(i));
        }
    }
}