package app;

import java.awt.Image;

public class Tile implements Comparable<Tile>{
	private final Image image;
	private int originalPosition;
	private int currentPosition;
	private String taker;
	private TileButton tileButton;

    public Tile(final Image image, final int originalPosition, final int currentPosition, final String taker) {
        this.image = image;
        this.originalPosition = originalPosition;
        this.currentPosition = currentPosition;
        this.taker = taker;
    }
    
    public Image getImage() {
    	return image;
    }
    
    public boolean isInRightPlace() {
    	return currentPosition == originalPosition;
    }

    public int getCurrentPosition() {
    	return currentPosition;
    }
    
    public void setCurrentPosition(final int newPosition) {
    	currentPosition = newPosition;
    }

    public int getOriginalPosition(){ return this.originalPosition;}

    public String getTaker(){ return this.taker;}

    public void setTaker(String user){ this.taker = user;}

    public void setButton(TileButton button){ this.tileButton = button;}

    public TileButton getButton(){ return this.tileButton;}

	@Override
	public int compareTo(Tile other) {
		return Integer.compare(this.currentPosition, other.currentPosition);
	}
}
