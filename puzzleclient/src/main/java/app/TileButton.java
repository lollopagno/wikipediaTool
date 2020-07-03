package app;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class TileButton extends JButton {

	private final Tile tile;
	private Color color;

	public TileButton(final Tile tile) {
		super(new ImageIcon(tile.getImage()));

		this.tile = tile;
		setColor(Color.gray);
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
