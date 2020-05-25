package views.game;

import actors.JudgeActor;
import actors.messages.StartGameMsg;
import actors.messages.StopGameMsg;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import model.Sequence;
import model.SequenceImpl;
import model.SequenceInfoGuess;
import views.players.PlayersPanel;
import views.players.PlayersView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;

public class GameView extends JFrame implements ActionListener {
    private final ActorRef judgeRef;
    private final PlayersView players;
    private final int length, nPlayers;
    private int time;

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
        ActorSystem system = ActorSystem.create("Mastermind");
        this.judgeRef = system.actorOf(Props.create(JudgeActor.class), "judge");

        // TODO: Remove those after actors implementation.
        this.generatePlayers();
        this.generateSequences();
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
     * Add a new player to the panel.
     * TODO: This will be removed after actor implementation.
     *
     * @param player Player name.
     * @param info   Player infos.
     */
    private void playerReady(String player, Sequence info) {
        SwingUtilities.invokeLater(() -> this.players.addPlayer(player, info));
    }

    private void generatePlayers() {
        // TODO: Remove this after actor implementation.
        List<Integer> seq = new LinkedList<>();
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            seq.add(r.nextInt(10));
        }
        for (int i = 0; i < nPlayers; i++) {
            Sequence sequence = new SequenceImpl(seq);
            this.playerReady("player_" + i, sequence);
        }
    }

    private void generateSequences() {
        Executors.newSingleThreadExecutor().execute(() -> {
            while (this.time-- > 0) {
                List<Integer> seq = new LinkedList<>();
                Random r = new Random();
                for (int i = 0; i < length; i++) {
                    seq.add(r.nextInt(10));
                }
                Sequence sequence = new SequenceImpl(seq);
                SequenceInfoGuess info = new SequenceInfoGuess(sequence, 2, 2);
                int from = r.nextInt(nPlayers);
                int to = r.nextInt(nPlayers);
                SwingUtilities.invokeLater(() -> this.players.inputSolution("player_" + from, "player_" + to, info));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
        StopGameMsg msg = new StopGameMsg();
        this.judgeRef.tell(msg, ActorRef.noSender());
    }
}