import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class InventoryController {
	private InventoryModel model;
	private InventoryPanel view;
	private PotionProdigy main;
	private Player currentPlayer;

	public InventoryController(InventoryModel m, InventoryPanel v, PotionProdigy main, Player p) {
		this.model = m;
		this.view = v;
		this.main = main;
		this.currentPlayer = p;

		view.arrowListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				back();
			}
		});

		refreshScreen();
	}

	/**
	* Displays the player's current items, cauldrons and crystals on the screen.
	*/
	public void refreshScreen() {
		ArrayList<String[]> rows = model.buildInventoryRows(currentPlayer);

		view.clearRows();
		for(int i = 0; i < rows.size(); i++) {
			String[] row = rows.get(i);
			view.addRow(row[0], row[1], model.isHeader(row));
		}

		view.setCauldronCount(model.cauldronSummary(currentPlayer));
		view.setCrystalCount("" + currentPlayer.getCrystals());
	}

	/**
	* Returns the player to the home screen.
	*/
	public void back() {
		this.main.HomeScreen(currentPlayer);
	}

	//PUT OTHER CODES ABOVE GETTER SETTERS
	public InventoryModel getModel() {
		return model;
	}

	public void setModel(InventoryModel model) {
		this.model = model;
	}

	public InventoryPanel getView() {
		return view;
	}

	public void setView(InventoryPanel view) {
		this.view = view;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
}
