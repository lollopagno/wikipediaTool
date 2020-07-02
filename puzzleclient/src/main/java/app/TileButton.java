package app;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.text.StyledEditorKit;

@SuppressWarnings("serial")
public class TileButton extends JButton{

	private final RequestClient requestClient;
	private final String username;
	private Tile tile;
	private String stateBox;

	public TileButton(final Tile tile, RequestClient requestClient, String username) {
		super(new ImageIcon(tile.getImage()));

		this.tile = tile;
		this.requestClient = requestClient;
		this.username = username;

		// Action button puzzle
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				// Check border box is yellow
				// TODO da verificare l'uguaglianza
				if (! getBorder().equals(BorderFactory.createLineBorder(Color.yellow))) {

					// Check state box
					// Caso raro in cui selezione e deseleziono la mia casella
				    if (checkStateBox()) {

						// Deseleziono la casella presa
						releaseBox();

					} else {
						setBorder(BorderFactory.createLineBorder(Color.red));

						// Prendo il possesso di quella casella
						takeBox();
					}
				}
			}
		});
	}

	// Implement API PUT takeBox
	private void takeBox(){

		requestClient.takeBox(username, this.tile.getOriginalPosition(), msg ->{
			log("Take box id: "+this.tile.getOriginalPosition());
			log(msg+"");
		});
	}

	// Implement API GET getState
	private boolean checkStateBox(){

		requestClient.checkStateBox(this.username, this.tile.getOriginalPosition(), msg -> {
			log("Check state box id: "+tile.getOriginalPosition());
			setStateBox(msg);
		});
		return getStateBox();
	}

	// Implement API PUT release
	private void releaseBox(){

		requestClient.releaseBox(username, this.tile.getOriginalPosition(), msg ->{
			log("Release, position box id: "+this.tile.getOriginalPosition());
			log(msg+"");
		});
	}

	private void setStateBox(String result){
		this.stateBox = result;
	}

	private Boolean getStateBox(){
		return Boolean.parseBoolean(this.stateBox);
	}

	private void log(String msg){
		synchronized (System.out){
			System.out.println(msg);
		}
	}
}
