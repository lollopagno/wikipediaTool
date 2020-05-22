package views.game;

import actors.JudgeActor;
import actors.messages.StartGameMsg;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import views.MyView;
import views.player.PlayerView;
import views.components.PlayerSolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GameView extends JFrame implements MyView, ActionListener, KeyListener {
    private final Set<PlayerView> players;
    private final ActorRef judgeRef;
    private final JPanel panel;
    private final int length, nPlayers, time;

    public GameView(int length, int nPlayers, int time) {
        this.setTitle("Game");
        this.setPreferredSize(new Dimension(600, 480));
        this.length = length;
        this.nPlayers = nPlayers;
        this.time = time;
        this.players = new HashSet<>();
        this.panel = new JPanel();
        this.panel.setPreferredSize(new Dimension(580, 600));

        // Genera il pannello dei giocatori.
        this.panel.add(new PlayerView("player_0"));
        JScrollPane pane = new JScrollPane(this.panel);
        this.getContentPane().add(pane, BorderLayout.CENTER);

        // Genera il pannello dei comandi.
        JPanel commands = new JPanel();
        JButton addP = new JButton("Add*");
        addP.addActionListener(this);
        JButton start = new JButton("Start");
        start.addActionListener(this);
        commands.add(addP);
        commands.add(start);
        this.getContentPane().add(commands, BorderLayout.PAGE_START);

        this.pack();
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
        this.addKeyListener(this);

        // Genero l'arbitro.
        ActorSystem system = ActorSystem.create("Mastermind");
        this.judgeRef = system.actorOf(Props.create(JudgeActor.class), "judge");
        this.requestFocus();
    }

    @Override
    public void playerReady(String player, ArrayList<Integer> info) {
        SwingUtilities.invokeLater(() -> {
            PlayerView view = new PlayerView(player);
            view.setSequence(info);
            this.players.add(view);
            this.panel.add(view, BorderLayout.CENTER);
            this.panel.updateUI();
        });
    }

    @Override
    public void solutionUpdated(String from, PlayerSolution solution) {
        SwingUtilities.invokeLater(() -> {
            Optional<PlayerView> view = this.getPlayerViewByName(from);
            view.ifPresent(playerView -> playerView.inputSolution(solution));
        });
    }

    @Override
    public void solutionFound(String from, String to) {
        SwingUtilities.invokeLater(() -> {
            Optional<PlayerView> view = this.getPlayerViewByName(from);
            view.ifPresent(player -> player.setOperation("Solution found!"));
        });
    }

    @Override
    public void playerWin(String player) {
        SwingUtilities.invokeLater(() -> System.out.println(player + "win"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        switch (e.getActionCommand()){
            case "Add*":
                // TODO: Questo non dove servire.
                this.playerReady("Player", new ArrayList<>());
                break;
            case "Start":
                this.startGame();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_S:
                this.startGame();
                break;
            case KeyEvent.VK_A:
                // TODO: Questo non deve esserci.
                this.playerReady("Player", new ArrayList<>());
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * Filtra il set dei players.
     * @param name Nome del giocatore da scegliere
     * @return Player scelto.
     */
    private Optional<PlayerView> getPlayerViewByName(String name) {
        return this.players.stream()
                .filter(f -> f.getName().equals(name))
                .findFirst();
    }

    /**
     * Invia il messaggio di inizio gioco all'arbitro.
     */
    private void startGame(){
        StartGameMsg msg = new StartGameMsg(this.length, this.nPlayers, this.time);
        this.judgeRef.tell(msg, ActorRef.noSender());
    }
}
