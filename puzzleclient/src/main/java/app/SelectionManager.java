package app;

public class SelectionManager {

	private boolean selectionActive = false;
	private Tile selectedTile;
	//private RequestClient requestClient;

	public void selectTile(String username, RequestClient requestClient,final Tile tile, final Listener listener) {
		
		if(selectionActive) {
			selectionActive = false;

			swap(selectedTile, tile);
			//requestClient.moveBox(username,selectedTile,tile,System.out::println);
			listener.onSwapPerformed();
		} else {
			selectionActive = true;
			selectedTile = tile;
		}
	}



	private void swap(final Tile t1, final Tile t2) {
		int pos = t1.getCurrentPosition();
		t1.setCurrentPosition(t2.getCurrentPosition());
		t2.setCurrentPosition(pos);
	}

	public void setSelected() {
		this.selectionActive = true;
	}

	@FunctionalInterface
	interface Listener{
		void onSwapPerformed();
	}

	public boolean getSelected(){
		return selectionActive;
	}

}
