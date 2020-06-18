package actors;

import actors.messages.*;
import akka.actor.ActorRef;
import info.PlayerInfo;
import model.OtherPlayersStore;
import model.Sequence;
import model.SequenceInfoGuess;
import views.players.PlayersView;

public class PlayerActor extends MastermindActorImpl {

    private ActorRef judgeActor;
    private PlayerInfo iAm;
    OtherPlayersStore others = new OtherPlayersStore();

    public PlayersView view;

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()

                .match(StartMsg.class, msg -> {
                    // StartMsg dal Judge

                    this.view = msg.getView();

                    // Save players references.
                    this.judgeActor = msg.getJudge();
                    iAm = msg.getPlayer();

                    // Generate the sequence of current actor.
                    iAm.generateSequence(msg.getLength());

                    // StartMsg from Judge and view notification.
                    this.log("START MSG Received and NUMBER is " + iAm.getSequence());
                    this.view.addPlayer(msg.getName(), iAm.getSequence());

                    // Save all other players in the store.
                    msg.getAllPlayers().forEach(elem -> {
                        if (elem.getName().equals(iAm.getName()))
                            return;
                        others.addPlayer(elem);
                    });

                    // TODO: Ho modificato questo messaggio per tornare solo gli altri. Non dovrebbe succedere nulla.
                    getSender().tell(new ReadyMsg(), getSelf());
                })

                .match(StartTurn.class, msg -> {
                    // StartTurn dal Judge

                    this.log("START TURN Received");

                    // Generate a new guess based on previous guesses.
                    Sequence trySequence = this.iAm.extractGuess();
                    this.log("Send guess " + trySequence + " to the " + this.iAm.getName()
                            + " with number " + this.iAm.getSequence());

                    // TODO: Perché viene inviato il riferimento a iAm? Quando sarebbero recuperabili tramite una getSender()?
                    this.iAm.getReference().tell(new GuessMsg(trySequence, this.iAm), getSelf());

                })

                .match(GuessMsg.class, msg -> {
                    // GuessMsg dal player
                    this.log("Response to the GUESS MSG at all players");

                    // Calculate the response.
                    Sequence guess = msg.getSequence();
                    SequenceInfoGuess response = this.iAm.getSequence().tryGuess(guess);

                    others.notifyOtherPlayersAboutResponse(response, getSelf());

                    // Send to the sender the full response.
                    getSender().tell(new ReturnGuessMsg(response), getSelf());

                })
                .match(ReturnGuessMsg.class, msg -> {

                    // ReturnGuessMsg dal player che risponde in funzione del guess richiesto
                    // ReturnGuessMsg dal player (risposta di quanti caratteri giusti ho indovinato)
                    int rightNumbers = msg.getSequence().getRightNumbers();
                    int rightPlaceNumbers = msg.getSequence().getRightPlaceNumbers();

                    this.log("RETURN GUESS MSG with response:\nRight Numbers: "
                            +rightNumbers+"\nRight Place Number: "+rightPlaceNumbers+"\n");

                    // Save the guess and notify the view.
                    String enemy = getSender().path().name();
                    others.saveGuess(
                            enemy,
                            msg.getSequence());
                    view.inputSolution(iAm.getName(), enemy, msg.getSequence());

                    // Controllo se il giocatore ha vinto
                    if(this.iAm.isWon(rightPlaceNumbers)){

                        this.log(this.iAm.getName()+" win!!");
                        // Invio al Judge il messaggio di vittoria
                        this.judgeActor.tell(new PlayerWin(this.iAm), getSelf());

                    }else {

                        // Invio al Judge il msg di fine turno (vado al prossimo giocatore o al nuovo turno)
                        this.judgeActor.tell(new EndTurn(), getSelf());
                    }
                })

                .match(NumberAnswer.class, msg -> {
                    // NumberAnswer dal player che invia A TUTTI la risposta

                    this.log("NUMBERS ANSWER\nRight Numbers: "+msg.getRightNumbers()+"\nRight Place Number: "+msg.getRightPlaceNumbers()+"\n");

                    // TODO Memorizzare informazione sulla risposta ricevuta.
                    // In verità è inutile memorizzare la risposta se non si ha anche la sequenza ad essa collegata.
                    // Infatti la risposta del prof è stata totalmente inutile.

                }).match(EndGame.class, msg ->{
                    // Il judge ha dichiarato la fine della partita

                    // Stoppo la mia esecuzione
                    this.log("My execution is finished!");
                    this.iAm.stopPlayer(getContext());

                }).build();
    }
}
