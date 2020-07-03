package app;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class TileButton extends JButton {

	private final RequestClient requestClient;
	private final String username;
	private Tile tile;
	private String stateBox;
	private Color color;

	public TileButton(final Tile tile, RequestClient requestClient, String username) {
		super(new ImageIcon(tile.getImage()));

		this.tile = tile;
		this.requestClient = requestClient;
		this.username = username;
		setColor(Color.gray);
	}

	// Action for button red
	public void actionButtonRed() {
		// Check state box

		// ATTENZIONE NON ELIMINARE IL CONTROLLO DEL IF
		// --> Questo controllo alla fine può essere eliminato <--
		//if (checkStateBox()) {

		setBorder(BorderFactory.createLineBorder(Color.gray));
		setColor(Color.gray);

		// Deseleziono la casella presa
		releaseBox();
		//}
	}

	// Action for button gray
	public void actionButtonGray() {
		setBorder(BorderFactory.createLineBorder(Color.red));
		setColor(Color.red);

		// Prendo il possesso di quella casella
		takeBox();
	}

	// Implement API PUT takeBox
	private void takeBox() {
		requestClient.takeBox(username, this.tile.getOriginalPosition(), msg -> {
			log("Take box id: " + this.tile.getOriginalPosition());
			log(msg + "");
		});
	}

	// Implement API GET getState
	private boolean checkStateBox() {
		requestClient.checkStateBox(this.username, this.tile.getOriginalPosition(), msg -> {
			log("Check state box id: " + tile.getOriginalPosition());
			setStateBox(msg);
		});
		return getStateBox();
	}

	// Implement API PUT release
	private void releaseBox() {
		requestClient.releaseBox(username, this.tile.getOriginalPosition(), msg -> {
			log("Release, position box id: " + this.tile.getOriginalPosition());
			log(msg + "");
		});
	}

	// Metodi get/set: colore del bottone
	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

	// Metodi get/set: stato (true, false) del box del risultato dell'API GET getState
	private void setStateBox(String result) {
		this.stateBox = result;
	}

	private Boolean getStateBox() {
		return Boolean.parseBoolean(this.stateBox);
	}

	private void log(String msg) {
		synchronized (System.out) {
			System.out.println("[Info] " + msg);
		}
	}
}
