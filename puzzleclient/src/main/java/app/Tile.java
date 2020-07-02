package app;

import com.google.gson.annotations.SerializedName;

import java.awt.Image;

public class Tile implements Comparable<Tile>{
	private Image image;
	private int originalPosition;
	private int currentPosition;
    private SelectionManager selectionManager;

    public Tile(final Image image, final int originalPosition, final int currentPosition) {
        this.image = image;
        this.originalPosition = originalPosition;
        this.currentPosition = currentPosition;
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

    public boolean getSelected(){
        return this.selectionManager.getSelected();
    }

	@Override
	public int compareTo(Tile other) {
		return this.currentPosition < other.currentPosition ? -1 
				: (this.currentPosition == other.currentPosition ? 0 : 1);
	}

}
