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
		// TODO : FAR SI CHE FACCIA RELEASE SOLO DI QUELLO SELZIONATO IN PRECEDENZA
		//checkReleaseBox();


	}

	private void checkTakeBox(){
		System.out.println(idBox+ "è " + selectionManager.getSelected());
		if(!selectionManager.getSelected())
			System.out.println("viene presa" + idBox);
			requestClient.takeBox(username, idBox, System.out::println);
	}

	//evitare release multipla
	private void checkReleaseBox(){
		if(selectionManager.getSelected())
			requestClient.releaseBox(username, idBox, System.out::println);
	}

}
