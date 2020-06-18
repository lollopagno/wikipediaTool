package views.game;

import actors.JudgeActor;
import actors.messages.StartGameMsg;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import views.players.PlayersPanel;
import views.players.PlayersView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameView extends JFrame implements ActionListener {

    private final ActorRef judgeRef;
    private final ActorSystem system;
    private final PlayersView players;
    private final int length, nPlayers;
    private final int time;

    public GameView(int length, int nPlayers, int time) {
        this.setTitle("Game");
        this.setPreferredSize(new Dimension(600, 800));
        this.length = length;
        this.nPlayers = nPlayers;
        this.time = time;

        // Genera il pannello dei giocatori.
        this.players = new PlayersPanel();
        this.getContentPane().add((Component) this.players, BorderLayout.CENTER);

        // Genera il pannello dei comandi.
        JPanel commands = new JPanel();
        commands.add(new ActionButton("Start", this));
        commands.add(new ActionButton("Stop", this));
        this.getContentPane().add(commands, BorderLayout.PAGE_START);

        this.pack();
        this.requestFocus();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(-1);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(-1);
            }
        });

        // Genero l'arbitro.
        this.system = ActorSystem.create("Mastermind");
        this.judgeRef = system.actorOf(Props.create(JudgeActor.class), "judge");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Start":
                this.startGame();
                break;
            case "Stop":
                this.stopGame();
                break;
        }
    }

    /**
     * Send the message of start game with this the view too at the judge actor ref.
     */
    private void startGame() {
        StartGameMsg msg = new StartGameMsg(this.length, this.nPlayers, this.time, this.players);
        this.judgeRef.tell(msg, ActorRef.noSender());
    }

    /**
     * Send the message of stop game to judge to stop all players too.
     */
    private void stopGame() {
        // Termino l'esecuzione dell'arbitro.
        this.system.stop(this.judgeRef);
    }
}