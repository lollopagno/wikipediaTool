package actors;

import views.MyView;

public class JudgeActor {
    private MyView view;

    public JudgeActor(MyView view){
        this.view = view;
    }

    public void preStart() {
        // TODO Crea tutti i player.
    }

    public void createReceive() {
        // TODO Crea la receive per i messaggi
        // TODO READY
            // Aspettare tutti i player
            // Quando sono tutti pronti
                // Generi sequenza e invii messaggio al primo
        // TODO END_TURN
            // Invia il messaggio all'attore successivo.
    }
}
