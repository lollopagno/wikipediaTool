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
	//private final Integer idBox2;
	private final String username;
	private final SelectionManager selectionManager;

	public TileButton(final Tile tile, RequestClient requestClient, Integer idBox, String username, SelectionManager selectionManager) {
		super(new ImageIcon(tile.getImage()));

		this.requestClient = requestClient;
		this.idBox = idBox;
		this.username = username;
		this.selectionManager = selectionManager;

		// TODO : TOGLIERE IF OPPURE COME FARE FERMARE IL SECONDO CLICK?
		//System.out.println(this.selectionManager.getSelected());
		//if(!this.selectionManager.getSelected()) {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setBorder(BorderFactory.createLineBorder(Color.red));

					// Prendo il possesso di quella casella
					checkTakeBox();
				}
			});
			checkReleaseBox();
	}

	// TODO: metodo aggiunto per prendere in possesso una casella (non attendibile)
	// Prima bisogna verificare se la casella è già stata presa, se è libera fare la PUT
	private void checkTakeBox(){
		//selectionManager.setSelected();
		requestClient.takeBox(username, idBox, System.out::println);
	}

	private void checkReleaseBox(){
		if(!selectionManager.getSelected())
			requestClient.releaseBox(username, idBox, System.out::println);
	}

	//private void checkMoveBox(){requestClient.moveBox(username, idBox,  ,System.out::println);}


}
