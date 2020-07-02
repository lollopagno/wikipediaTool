package app;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class TileButton extends JButton{

	private final RequestClient requestClient;
	private final Integer idBox;
	private final String username;

	public TileButton(final Tile tile, RequestClient requestClient, Integer idBox, String username) {
		super(new ImageIcon(tile.getImage()));

		this.requestClient = requestClient;
		this.idBox = idBox;
		this.username = username;

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {

					//TODO if API checkTake
					//if (/*API getState*/ true){

						// Deseleziono la casella presa
						/* API release */
						//checkReleaseBox();

					//}else{
						setBorder(BorderFactory.createLineBorder(Color.red));

						// Prendo il possesso di quella casella
						takeBox();
					//}

				}
			});
	}

	private void takeBox(){ requestClient.takeBox(username, idBox, System.out::println); }

	//evitare release multipla
	private void checkReleaseBox(){  requestClient.releaseBox(username, idBox, System.out::println);}

}
