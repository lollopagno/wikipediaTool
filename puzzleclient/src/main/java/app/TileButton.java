package app;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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

				setBorder(BorderFactory.createLineBorder(Color.red));

				// Prendo il possesso di quella casella
				checkTakeBox();
			}
		});


	}

	// TODO: metodo aggiunto per prendere in possesso una casella (non attendibile)
	// Prima bisogna verificare se la casella è già stata presa, se è libera fare la PUT
	private void checkTakeBox(){
		requestClient.takeBox(username, idBox, System.out::println);
	}

}
