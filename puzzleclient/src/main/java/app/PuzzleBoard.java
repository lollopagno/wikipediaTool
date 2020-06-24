package app;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SuppressWarnings("serial")
public class PuzzleBoard extends JFrame {
	final int rows, columns;
    private final ScheduledExecutorService job = Executors.newSingleThreadScheduledExecutor();
    private List<Tile> tiles = new ArrayList<>();
	
	private SelectionManager selectionManager = new SelectionManager();
	
    public PuzzleBoard(final int rows, final int columns, final String imagePath) {
    	this.rows = rows;
		this.columns = columns;
    	
    	setTitle("Puzzle");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JPanel board = new JPanel();
        board.setBorder(BorderFactory.createLineBorder(Color.gray));
        board.setLayout(new GridLayout(rows, columns, 0, 0));
        getContentPane().add(board, BorderLayout.CENTER);
        
        createTiles(imagePath, board);

        // Action close view puzzle
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                // TODO: qui richiamare l'API Delete
            }

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
            }
        });
    }

    
    private void createTiles(final String imagePath, final JPanel board) {
        // TODO: Il caricamento dell'immagine dev'essere fatto.
		final BufferedImage image;
        
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not load image", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final int imageWidth = image.getWidth(null);
        final int imageHeight = image.getHeight(null);

        
        /*
        Questa è la vecchia parte delle positions.
        Adesso le dobbiamo scaricare dal server.
        final List<Integer> randomPositions = new ArrayList<>();
        IntStream.range(0, rows*columns).forEach(item -> { randomPositions.add(item); }); 
        Collections.shuffle(randomPositions);*/
        Call<List<Tile>> boxes = RemoteServices.getInstance().getPuzzleService().getMappings();
        boxes.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Tile>> call, Response<List<Tile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                     int position = 0;

                    // TODO: Scaricare dal server l'attuale disposizione delle tessere.
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            final Image imagePortion = createImage(new FilteredImageSource(image.getSource(),
                                    new CropImageFilter(j * imageWidth / columns,
                                            i * imageHeight / rows,
                                            (imageWidth / columns),
                                            imageHeight / rows)));

                            // TODO: Sincronizzare tutti i pezzi delle immagini con la loro attuale posizione, non quella originale.
                            tiles.add(new Tile(imagePortion, position, response.body().get(position).getCurrentPosition()));
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
    		final TileButton btn = new TileButton(tile);
            board.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            btn.addActionListener(actionListener -> {
            	selectionManager.selectTile(tile, () -> {
            		paintPuzzle(board);
                	checkSolution();
            	});
            });
    	});
        /** check if another players has selected a card*/
        this.job.execute(()-> selectedCard(tiles));
    	
    	pack();
        setLocationRelativeTo(null);
    }

    private void checkSolution() {
        // TODO: Questa parte deve essere effettuata dal server immagino.
    	if(tiles.stream().allMatch(Tile::isInRightPlace)) {
    		JOptionPane.showMessageDialog(this, "Puzzle Completed!", "", JOptionPane.INFORMATION_MESSAGE);
    	}
    }

    private void selectedCard(List<Tile> tiles){
        /*tiles.forEach(tile->{
            if(tile.getSelected()){
                setBorder(BorderFactory.createLineBorder(Color.red));
            }
        });*/
    }

    private void log(String msg) {
        synchronized (System.out) {
            System.out.println(msg);
        }
    }
}
