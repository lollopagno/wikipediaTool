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
        if(last1try.getNumbers() == null) {
            last1try = guess;
            return;
        }

        if (guess.getRightPlaceNumbers() > last1try.getRightPlaceNumbers() ||
                guess.getRightPlaceNumbers() == last1try.getRightPlaceNumbers() &&
                        guess.getRightNumbers() >= last1try.getRightNumbers()) {
            last2try = last1try;
            last1try = guess;
        }
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
        int tries = 0;
        do {
            tries++;
            // TODO: This generation may be interrupted.
            Random r = new Random();
            List<Integer> last1 = new ArrayList<>(last1try.getNumbers().getSequence());
            List<Integer> last2 = new ArrayList<>(last2try.getNumbers().getSequence());
            int rightPlaced = last1try.getRightPlaceNumbers();
            int right = last1try.getRightNumbers();
            List<Integer> number = new ArrayList<>(lenght);
            last1.forEach(e -> number.add(-1));

            /*
            Riempire la sequenza risultato con tutti i numeri che ho
            messo nella giusta posizione.
             */
            /*
            Step a: Scorrere tutta la seq1 alla ricerca del prossimo
            numero uguale al corrispondente nella seq2. Questi numeri
            sono sicuro che siano giusti e li aggiungo subito nella
            soluzione. Continuo fino a quando non ho scorso tutta la seq1.
             */
            // Generate the new random start index.
            int s = new Random().nextInt(lenght);
            // Start from random index. Repeat at max length times.
            for (int i = s, j = 0; j < lenght; i++, j++) {
                if (i == lenght)
                    i = 0;

                int l1 = last1.get(i);
                int l2 = last2.get(i);
                if (l1 == l2) {
                    number.set(i, last1.get(i));
                    rightPlaced--;
                }
            }

            // Il successivo passaggio lo devo svolgere solo se ci sono dei numeri giusti.
            if (right > 0 && rightPlaced > 0) {
                /*
                    Scorro nuovamente tutta la seq1 a partire da un indice casuale
                    alla ricerca del prossimo numero che è contenuto anche nella
                    seq2 ma che non è nella stessa posizione nella seq1.
                    Questo sono sicuro essere un numero giusto, che nella sequenza 2
                    era mal posizionato ma che potrebbe essere ben posizionato nella seq1.
                    Quindi lo aggiungo alla soluzione. Continuo finché non ho scorso
                    nuovamente tutta la seq1. Ovviamente lo devo mettere in una
                    posizione diversa, altrimenti sarebbe stato ben posizionato.
                 */
                for (int i = r.nextInt(lenght - rightPlaced), j = 0;
                     j < lenght && rightPlaced > 0;
                     i++, j++) {
                    if (i == lenght)
                        i = 0;

                    // Se ho già sfruttato questo numero continuo.
                    int m = last1.get(i);
                    if (number.get(i) != -1 || m < 0)
                        continue;

                    // Controllo se last2 contiene il numero e nel caso lo aggiungo ai numbers.
                    for (int x = 0; x < lenght; x++) {
                        if (last2.get(x) == m) {
                            int rn = getRandomFreeIndexNotI(number, i);
                            number.set(rn, m);

                            // Decremento entrambi perché li ho usati entrambi in questo passaggio.
                            rightPlaced--;
                            right--;
                        }
                    }
                }
            }

            /*
            A questo punto dovrei essere riuscito a trovare tutte le occorrenze
            dei numeri correttamente posizionati che avevo rilevato nella seq1.
            Se così non fosse, dovrei prendere dei numeri casuali dalla seq1
            per finire tutti i numeri ben posizionati.
             */
            for (int i = r.nextInt(lenght), j = 0;
                 j < lenght && rightPlaced > 0;
                 i++, j++) {
                if (i == lenght)
                    i = 0;

                // Se ho già sfruttato questo numero continuo.
                if (number.get(i) != -1 || last1.get(i) < 0 || last2.get(i) < 0)
                    continue;

                int n = last1.get(i);
                number.set(i, n);
                rightPlaced--;
            }

            /*
            Trovare tutti i numeri che ho indovinato ma mal posizionato.
             */
            for (int i = 0; i < lenght && right > 0; i++) {
                // Controllo che non sia un numero che ho già gestito.
                if (number.get(i) != -1)
                    continue;

                // Controllo se last2 contiene il numero e nel caso lo aggiungo ai numbers.
                int n = last1.get(i);
                // Controllo se last2 contiene il numero e nel caso lo aggiungo ai numbers.
                for (int x = 0; x < lenght; x++) {
                    if (last2.get(x) == n) {
                        int rn = getRandomFreeIndexNotI(number, i);
                        number.set(rn, n);
                        right--;
                    }
                }
            }

            for (int i = r.nextInt(lenght), j = 0;
                 j < lenght && right > 0;
                 i++, j++) {
                if (i == lenght)
                    i = 0;

                int n = last1.get(i);
                if (n != -1) {
                    int rn = getRandomFreeIndexNotI(number, i);
                    number.set(rn, n);
                    last1.set(rn, -1);
                    right--;
                }
            }

            /*
             * Terza esplorazione: riempio casualmente tutti i rimanenti slot.
             */
            List<Integer> filled = fillNumberWithRand(lenght, number);

            seq = new SequenceImpl(filled);
        } while (alltries.contains(seq));
        alltries.add(seq);
        return seq;
    }

    /**
     * Utility method.
     *
     * @param list   List to fetch.
     * @param action Action to do.
     */
    public void forEachRandomInit(List<Integer> list, Consumer<? super Integer> action) {
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

    /**
     * Fill the number with random Integers.
     *
     * @param length Length to fill.
     * @param number Base to fill.
     * @return New number filled.
     */
    List<Integer> fillNumberWithRand(int length, List<Integer> number) {
        Random r = new Random();
        List<Integer> tmp = new ArrayList<>(number);
        // Fill the number.
        for (int i = 0; i < number.size(); i++)
            if (tmp.get(i) == -1)
                tmp.set(i, r.nextInt(10));

        // Fill the length.
        for (int i = number.size(); i < length; i++)
            tmp.add(r.nextInt(10));

        return tmp;
    }

    /**
     * Get a random index from the list number that is not i.
     *
     * @param number List.
     * @param i      Index to not extract.
     * @return Random index.
     */
    int getRandomFreeIndexNotI(List<Integer> number, int i) {
        Random r = new Random();
        int length = number.size();
        int ri, t = 0;

        /*
         Continuo ad estrarre numeri casuali finché non ne trovo uno idoneo.
         Se non riesco lo metto nella prima posizione libera.
         */
        do {
            ri = r.nextInt(length);
            t++;
        } while (ri != i &&
                t < length);
        if (t == length)
            for (int y = 0; y < length; y++)
                if (number.get(y) == -1)
                    ri = y;

        return ri;
    }
}