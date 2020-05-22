package views;

import actors.JudgeActor;
import actors.messages.StartGameMsg;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GameView extends JFrame implements MyView, ActionListener {
    private final Set<PlayerView> players;
    private final ActorRef judgeRef;
    private final JScrollPane panel;
    private final int length, nplayers;

    public GameView(int length, int nplayers) {
        this.setTitle("Game");
        this.setPreferredSize(new Dimension(800, 600));

        this.length = length;
        this.nplayers = nplayers;
        this.players = new HashSet<>();
        this.panel = new JScrollPane();
        this.panel.setPreferredSize(this.getPreferredSize());
        ActorSystem system = ActorSystem.create("Mastermind");
        this.judgeRef = system.actorOf(Props.create(JudgeActor.class), "judge");

        this.panel.add(new PlayerView("player_0"));

        JPanel commands = new JPanel();
        JButton addP = new JButton("Add*");
        addP.addActionListener(this);
        JButton start = new JButton("Start");
        start.addActionListener(this);
        commands.add(addP);
        commands.add(start);
        this.getContentPane().add(commands, BorderLayout.PAGE_START);
        this.getContentPane().add(panel, BorderLayout.CENTER);
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
    }

    @Override
    public void playerReady(String player, ArrayList<Integer> info) {
        SwingUtilities.invokeLater(() -> {
            PlayerView view = new PlayerView(player);
            view.setSequence(info);
            this.players.add(view);
            this.panel.add(view);
        });
    }

    @Override
    public void solutionUpdated(String from, String to, ArrayList<Integer> sequence) {
        SwingUtilities.invokeLater(() -> {
            Optional<PlayerView> view = this.getPlayerViewByName(from);
            view.ifPresent(playerView -> playerView.setOperation("Solution updated"));
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
        SwingUtilities.invokeLater(() -> System.out.println(player));
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
                // Invia il messaggio al judge di iniziare il gioco.
                StartGameMsg msg = new StartGameMsg(this.length, this.nplayers);
                this.judgeRef.tell(msg, ActorRef.noSender());
                break;
        }
    }

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
}
