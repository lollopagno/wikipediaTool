package app;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class TileButton extends JButton {

	private final RequestClient requestClient;
	private final String username;
	private final Tile tile;
	private Color color;

	public TileButton(final Tile tile, RequestClient requestClient, String username) {
		super(new ImageIcon(tile.getImage()));

		this.tile = tile;
		this.requestClient = requestClient;
		this.username = username;
		setColor(Color.gray);
	}

	// Implement API PUT release
	public void releaseBox() {
		requestClient.releaseBox(username, this);
	}

	// Implement API PUT takeBox
	public void takeBox() {
		requestClient.takeBox(username, this);
	}

	public Tile getTile(){return this.tile;}


	// Metodi get/set: colore del bottone
	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

	private void log(String msg) {
		synchronized (System.out) {
			System.out.println("[Info] " + msg);
		}
	}
}
