package app;

import java.awt.*;

import static java.awt.Color.yellow;

public class SelectionManager {
	private boolean selectionActive = false;
	private Tile selectedTile;

	/**
	 * Manage the selection of a tile.
	 *
	 * @param username      Username of the selector.
	 * @param requestClient The request client.
	 * @param btn           The button clicked.
	 * @param listener      The listener of change.
	 */
	public void selectTile(String username, RequestClient requestClient, final TileButton btn, final Listener listener) {
		// Check if button is not taken by another player.
		if (!btn.getColor().equals(yellow)) {
			// Check if button is not already taken by the player.
			if (btn.getColor().equals(Color.red)) {
				requestClient.releaseBox(username, btn);
			} else {
				// Now declare the tile as taken by the player.
				requestClient.takeBox(username, btn, took -> {
					if (!took) return;

					// If I have two tile taken, I swap them.
					if ((selectionActive) && (!selectedTile.getButton().getColor().equals(yellow))) {
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

	/**
	 * Swap two tiles.
	 *
	 * @param t1 First tile.
	 * @param t2 Second tile.
	 */
	private void swap(final Tile t1, final Tile t2) {
		int pos = t1.getCurrentPosition();
		t1.setCurrentPosition(t2.getCurrentPosition());
		t2.setCurrentPosition(pos);
	}

	@FunctionalInterface
	interface Listener {
		void onSwapPerformed();
	}
}