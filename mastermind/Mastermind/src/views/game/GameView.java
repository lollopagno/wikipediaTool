package views.game;

import actors.JudgeActor;
import actors.messages.StartGameMsg;
import actors.messages.StopGameMsg;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import model.SequenceInfoGuess;
import views.player.PlayersPanel;
import views.player.PlayersView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class GameView extends JFrame implements ActionListener {
    private final ActorRef judgeRef;
    private final PlayersView players;
    private final int length, nPlayers, time;

    public GameView(int length, int nPlayers, int time) {
        this.setTitle("Game");
        this.setPreferredSize(new Dimension(600, 480));
        this.length = length;
        this.nPlayers = nPlayers;
        this.time = time;

        // Genera il pannello dei giocatori.
        this.players = new PlayersPanel();
        this.getContentPane().add((Component) this.players, BorderLayout.CENTER);

        // Genera il pannello dei comandi.
        JPanel commands = new JPanel();

        JButton addP = new JButton("Add P*");
        addP.addActionListener(this);
        commands.add(addP);

        JButton addS = new JButton("Add S*");
        addS.addActionListener(this);
        commands.add(addS);

        JButton start = new JButton("Start");
        start.addActionListener(this);
        commands.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(this);
        commands.add(stop);

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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: Questo non dove servire. Sono tutti bottoni di test.
        List<Integer> seq = new LinkedList<>();
        Random r = new Random();
        switch (e.getActionCommand()) {
            case "Add P*":
                for (int i = 0; i < length; i++) {
                    seq.add(r.nextInt(10));
                }
                this.playerReady("Player", seq);
                break;
            case "Add S*":
                for (int i = 0; i < length; i++) {
                    seq.add(r.nextInt(10));
                }
                SequenceInfoGuess info = new SequenceInfoGuess(seq, 2, 2);
                this.players.inputSolution("player_0", "player_1", info);
                break;
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
     * TODO: Remove this.
     *
     * @param player Player name.
     * @param info   Player infos.
     */
    private void playerReady(String player, List<Integer> info) {
        SwingUtilities.invokeLater(() -> this.players.addPlayer(player, info));
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