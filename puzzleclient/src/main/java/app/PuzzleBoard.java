package app;

import app.remoteservices.Box;
import app.remoteservices.RemoteServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public class PuzzleBoard extends JFrame {
    private final SelectionManager selectionManager = new SelectionManager();
    private final ScheduledExecutorService updateColor = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService updatePuzzle = Executors.newSingleThreadScheduledExecutor();

    private final RequestClient requestClient;
    private final String username;

    private final List<Tile> tiles = new ArrayList<>();
    final int rows, columns;
    final JPanel board = new JPanel();
    final String imagePath = "src/main/java/app/bletchley-park-mansion.jpg";

    public PuzzleBoard(String username) {
        // Dimension Puzzle
        this.rows = 3;
        this.columns = 5;

        // Object Request Client
        this.requestClient = RequestClient.instance();
        this.username = username;

        setTitle("Puzzle "+username);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.board.setBorder(BorderFactory.createLineBorder(Color.gray));
        this.board.setLayout(new GridLayout(rows, columns, 0, 0));
        getContentPane().add(this.board, BorderLayout.CENTER);

        createTiles(imagePath);

        // Action close view puzzle
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                // Release box
                requestClient.mappingBox(t-> t.forEach(tile -> {
                    String taker = tile.getTaker();

                    if(taker.equals(username)) {
                        tiles.stream()
                                .filter(f -> f.getOriginalPosition() == tile.getOriginalPosition()).findFirst()
                                .ifPresent(p -> SwingUtilities.invokeLater(() -> {
                                            requestClient.releaseBox(username, p.getButton());
                                }));
                    }
                }));

                // Delete user from user list server
                requestClient.deleteUser(username);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
            }
        });
    }

    private void createTiles(final String imagePath) {
        final BufferedImage image;

        // Load image
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not load image", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Params image
        final int imageWidth = image.getWidth(null);
        final int imageHeight = image.getHeight(null);

        // API GET for extract position boxes image
        Call<Set<Box>> boxes = RemoteServices.getInstance().getPuzzleService().getMappings();
        boxes.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Set<Box>> call, Response<Set<Box>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int position = 0;
                    List<Box> lists = new LinkedList<>(response.body());

                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            final Image imagePortion = createImage(new FilteredImageSource(image.getSource(),
                                    new CropImageFilter(j * imageWidth / columns,
                                            i * imageHeight / rows,
                                            (imageWidth / columns),
                                            imageHeight / rows)));

                            int currPos = position;
                            Optional<Box> remoteTile = lists.stream().filter(f -> f.getOriginalPosition() == currPos).findFirst();
                            tiles.add(new Tile(imagePortion, position, remoteTile.get().getCurrentPosition(), username));
                            position++;
                        }
                    }
                    paintPuzzle();
                    updateColor.scheduleAtFixedRate(() -> addColor(username), 0, 1500, TimeUnit.MILLISECONDS);
                    updatePuzzle.scheduleAtFixedRate(() -> SwingUtilities.invokeLater(() -> repaintPuzzle()), 0, 1500, TimeUnit.MILLISECONDS);
                }
            }

            @Override
            public void onFailure(Call<Set<Box>> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

     /**
     * Paint the puzzle made by the tile buttons.
     */
    private void paintPuzzle() {
        this.board.removeAll();

        Collections.sort(this.tiles);

        this.tiles.forEach(tile -> {

            final TileButton btn = new TileButton(tile);
            tile.setButton(btn);

            this.board.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(btn.getColor()));
            btn.setColor(btn.getColor());

            // Action Button puzzle
            btn.addActionListener(actionListener -> {
                // Move boxes
                selectionManager.selectTile(this.username, this.requestClient, btn, () -> {
                    paintPuzzle();
                    checkSolution();
                });
            });
        });
        pack();
    }

    /**
     * Repaint puzzle by moves box made by other client
     */
    private void repaintPuzzle(){

        this.board.removeAll();
        Collections.sort(this.tiles);

        this.tiles.forEach(tile -> {

            TileButton btn = tile.getButton();
            this.board.add(btn);

            btn.setBorder(BorderFactory.createLineBorder(btn.getColor()));
            btn.setColor(btn.getColor());
        });
        pack();
    }

    /**
     * Update color border box
     * @param username name user
     */
    private void addColor(String username) {
        log("Update color box every 1,5s");

        this.requestClient.mappingBox(t-> t.forEach(tile -> {

            String taker = tile.getTaker();

            this.tiles.stream()
                    .filter(f -> f.getOriginalPosition() == tile.getOriginalPosition()).findFirst()
                    .ifPresent(p -> {

                        p.setCurrentPosition(tile.getCurrentPosition());

                        // If color button is gray and user take a box --> color box yellow
                        if (!taker.equals("") && !taker.equals(username)) {
                            SwingUtilities.invokeLater(() -> {
                                p.getButton().setColor(Color.yellow);
                                p.getButton().setBorder(BorderFactory.createLineBorder(Color.yellow));
                            });

                        // If color button is yellow and user release a box --> color box gray
                        } else if (taker.equals("") && p.getButton().getColor() == Color.yellow) {
                            SwingUtilities.invokeLater(() -> {
                                p.getButton().setColor(Color.gray);
                                p.getButton().setBorder(BorderFactory.createLineBorder(Color.gray));
                            });
                        }
                    });
            checkSolution();
        }));
    }

    /**
     * Check for the solution.
     */

    /* TODO TERMINATE--------*/
    /*private void terminate() {

        if (this.tiles.stream().allMatch(Tile::isInRightPlace)) {
            updateColor.shutdown();
            updatePuzzle.shutdown();
            JOptionPane.showMessageDialog(this, "Puzzle Completed!", "", JOptionPane.INFORMATION_MESSAGE);

        }
    }*/
    private void checkSolution() {

        if (this.tiles.stream().allMatch(Tile::isInRightPlace)) {
            updateColor.shutdown();
            updatePuzzle.shutdown();
            JOptionPane.showMessageDialog(this, "Puzzle Completed!", "", JOptionPane.INFORMATION_MESSAGE);
            this.requestClient.gameReset();
        }
    }

    /**
     * Print a string in the default log.
     * @param msg Message to log.
     */
    private void log(String msg) {
        synchronized (System.out) {
            System.out.println("[Info] " + msg);
        }
    }
}