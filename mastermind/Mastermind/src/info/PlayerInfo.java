package info;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import model.PlayerReference;
import model.Sequence;
import model.SequenceImpl;
import model.SequenceInfoGuess;

import java.util.*;

public class PlayerInfo {
    private final String name;
    private final ActorRef reference;

    private Sequence sequence;
    SequenceInfoGuess last1try, last2try;
    final Set<Sequence> allTries;

    public PlayerInfo(PlayerReference reference) {
        this.name = reference.getName();
        this.reference = reference.getRef();
        this.last1try = this.last2try = null;
        allTries = new TreeSet<>();
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
     * Stop lifecycle Actor.
     *
     * @param context Actor context.
     */
    public void stopPlayer(ActorContext context) {
        context.stop(this.reference);
    }

    public boolean isSolved(int length) {
        return last1try != null && last1try.getNumbers() != null && last1try.getRightPlaceNumbers() == length;
    }

    /**
     * Save the previous guess only if is better than the second before.
     *
     * @param guess Guess to save.
     */
    public void setTry(SequenceInfoGuess guess) {
        if (last1try == null || last1try.getNumbers() == null) { // This is for the first time.
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

    public Sequence extractGuess(int length) {
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

    private Sequence createElaborateSequence(int length) {
        Sequence seq;
        int tries = 0;
        do {
            tries++;
            Random r = new Random();
            List<Integer> last1 = new ArrayList<>(last1try.getNumbers().getSequence());
            List<Integer> last2 = new ArrayList<>(last2try.getNumbers().getSequence());
            int rightPlaced = last1try.getRightPlaceNumbers();
            int right = last1try.getRightNumbers();
            List<Integer> number = new ArrayList<>(length);
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
            int s = new Random().nextInt(length);
            // Start from random index. Repeat at max length times.
            for (int i = s, j = 0; j < length && rightPlaced > 0; i++, j++) {
                if (i == length)
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
                for (int i = r.nextInt(length - rightPlaced), j = 0;
                     j < length && rightPlaced > 0;
                     i++, j++) {
                    if (i == length)
                        i = 0;

                    // Se ho già sfruttato questo numero continuo.
                    int m = last1.get(i);
                    if (number.get(i) != -1 || m < 0)
                        continue;

                    // Controllo se last2 contiene il numero e nel caso lo aggiungo ai numbers.
                    for (int x = 0; x < length; x++) {
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
            for (int i = r.nextInt(length), j = 0;
                 j < length && rightPlaced > 0;
                 i++, j++) {
                if (i == length)
                    i = 0;

                // Se ho già sfruttato questo numero continuo.
                if (number.get(i) != -1 || last1.get(i) < 0 || last2.get(i) < 0)
                    continue;

                int n = last1.get(i);
                number.set(i, n);
                rightPlaced--;
            }

            /*
             * Trovo tutti i numeri che ho indovinato ma mal posizionato.
             */
            for (int i = r.nextInt(length), j = 0;
                 j < length && right > 0;
                 i++, j++) {
                if (i == length)
                    i = 0;

                // Controllo che non sia un numero che ho già gestito.
                if (number.get(i) != -1)
                    continue;

                // Controllo se last2 contiene il numero e nel caso lo aggiungo ai numbers.
                int n = last1.get(i);
                // Controllo se last2 contiene il numero e nel caso lo aggiungo ai numbers.
                for (int x = 0; x < length; x++) {
                    if (last2.get(x) == n) {
                        int rn = getRandomFreeIndexNotI(number, i);
                        number.set(rn, n);
                        right--;
                        break;
                    }
                }
            }

            for (int i = r.nextInt(length), j = 0;
                 j < length && right > 0;
                 i++, j++) {
                if (i == length)
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
            List<Integer> filled = fillNumberWithRand(length, number);

            seq = new SequenceImpl(filled);
        } while (allTries.contains(seq) && tries < 10000);
        allTries.add(seq);
        return seq;
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