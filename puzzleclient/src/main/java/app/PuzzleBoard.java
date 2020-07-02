package app;

import app.remoteservices.RemoteServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SuppressWarnings("serial")
public class PuzzleBoard extends JFrame{

    private SelectionManager selectionManager = new SelectionManager();
    private final ScheduledExecutorService job = Executors.newSingleThreadScheduledExecutor();

    private final RequestClient requestClient;
    private String username;

    private List<Tile> tiles = new ArrayList<>();

    final int rows, columns;
	
    public PuzzleBoard(final int rows, final int columns, final String imagePath, String username) {

        // Dimension Puzzle
    	this.rows = rows;
		this.columns = columns;

		// Object Request Client
        this.requestClient = new RequestClient(this.rows, this.columns);
        this.username = username;

    	setTitle("Puzzle");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createLineBorder(Color.gray));
        board.setLayout(new GridLayout(rows, columns, 0, 0));
        getContentPane().add(board, BorderLayout.CENTER);
        
        createTiles(imagePath, board);

        // Action close view puzzle
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                requestClient.deleteUser(username, msg ->{
                    log(msg+": "+username);
                });
                log("Closed window puzzle!");
       }
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
            }
        });
    }

    
    private void createTiles(final String imagePath, final JPanel board) {

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

        /* <-- Questa è la vecchia parte delle positions -->

        Adesso le dobbiamo scaricare dal server.
        final List<Integer> randomPositions = new ArrayList<>();
        IntStream.range(0, rows*columns).forEach(item -> { randomPositions.add(item); }); 
        Collections.shuffle(randomPositions);

            <-- end version prof -->
        */

        // API GET for extract position boxes image
        Call<List<Tile>> boxes = RemoteServices.getInstance().getPuzzleService().getMappings();
        boxes.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<List<Tile>> call, Response<List<Tile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int position = 0;

                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            final Image imagePortion = createImage(new FilteredImageSource(image.getSource(),
                                    new CropImageFilter(j * imageWidth / columns,
                                            i * imageHeight / rows,
                                            (imageWidth / columns),
                                            imageHeight / rows)));

                            // TODO getOriginalPosition or getCurrentPosition ??
                            tiles.add(new Tile(imagePortion, position, response.body().get(position).getOriginalPosition()));;
                            position++;
                        }
                    }
                    paintPuzzle(board);
                }
            }

            @Override
            public void onFailure(Call<List<Tile>> call, Throwable t) {
                log(t.getMessage());
            }
        });
	}
    
    private void paintPuzzle(final JPanel board) {
    	board.removeAll();
    	
    	Collections.sort(tiles);



    	tiles.forEach(tile -> {
    		final TileButton btn = new TileButton(tile, this.requestClient, tile.getOriginalPosition(), this.username, this.selectionManager);
            board.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            /* check if another players has selected a card*/
            this.job.execute(()-> selectedCard(tiles,btn));

            btn.addActionListener(actionListener -> {
            	selectionManager.selectTile(this.username,this.requestClient,tile, () -> {
            		paintPuzzle(board);
                	checkSolution();
            	});
            });
    	});
    	
    	pack();
        setLocationRelativeTo(null);
    }

    private void checkSolution() {
        // TODO: Questa parte deve essere effettuata dal server immagino.
    	if(tiles.stream().allMatch(Tile::isInRightPlace)) {
    		JOptionPane.showMessageDialog(this, "Puzzle Completed!", "", JOptionPane.INFORMATION_MESSAGE);
    	}
    }

    private void selectedCard(List<Tile> tiles, TileButton btn){
        tiles.forEach(tile->{
            if(tile.getSelected()){
                System.out.println("yellow");
                btn.setBorder(BorderFactory.createLineBorder(Color.yellow));
            }
        });
    }

    private void log(String msg) {
        synchronized (System.out) {
            System.out.println("[Info] "+msg);
        }
    }
}
