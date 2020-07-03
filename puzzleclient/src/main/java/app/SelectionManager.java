package app;

import java.awt.*;

import static java.awt.Color.yellow;

public class SelectionManager {

	private boolean selectionActive = false;
	private Tile selectedTile;

	public void selectTile(String username, RequestClient requestClient, final TileButton btn, final Listener listener) {

		// Check button is not yellow
		if (!btn.getColor().equals(yellow)) {

			// Check button is red
			if (btn.getColor().equals(Color.red)) {
				requestClient.releaseBox(username, btn);
			} else {
				// Action for gray button
				requestClient.takeBox(username, btn, took -> {
					if(!took) return;

					if (selectionActive) {
						selectionActive = false;

						requestClient.moveBox(username, selectedTile.getOriginalPosition(), btn.getTile().getOriginalPosition(), moved -> {
							if (moved) {
								swap(selectedTile, btn.getTile());
								listener.onSwapPerformed();
							}
						});
					} else {
						selectionActive = true;
						selectedTile = btn.getTile();
					}
				});
			}
		}
	}

	private void swap(final Tile t1, final Tile t2) {
		int pos = t1.getCurrentPosition();
		t1.setCurrentPosition(t2.getCurrentPosition());
		t2.setCurrentPosition(pos);
	}

	@FunctionalInterface
	interface Listener {
		void onSwapPerformed();
	}

	public boolean getSelected() {
		return selectionActive;
	}

}