import java.util.ArrayList;

public class InventoryModel { //Model for the Inventory Screen; replaces the old displayInventory()

	/**
	* Arranges everything the player owns into lines of text for the inventory screen.
	* <p>
	* The fruits are listed first, followed by the concoction bases, then the cauldrons separated into the
	* usable ones and the ones that need blessing. Section titles are stored as a line with a blank quantity
	* so that the screen knows to display them in a larger font.
	* </p>
	*
	* @param p the player whose inventory is being listed
	* @return the lines to be displayed, each one containing a name and a quantity
	*/
	public ArrayList<String[]> buildInventoryRows(Player p) {
		ArrayList<String[]> rows = new ArrayList<>();
		Inventory inv = p.getInventory();

		rows.add(new String[] {"FRUIT INGREDIENTS", ""});
		for(int i = 0; i < inv.getIngredients().size(); i++) {
			InventoryItem fruit = inv.getIngredients().get(i);
			rows.add(new String[] {fruit.getName(), "x " + fruit.getQuantity()});
		}

		rows.add(new String[] {"", ""});
		rows.add(new String[] {"CONCOCTION BASES", ""});
		for(int i = 0; i < inv.getBases().size(); i++) {
			InventoryItem base = inv.getBases().get(i);
			rows.add(new String[] {base.getName(), "x " + base.getQuantity()});
		}

		rows.add(new String[] {"", ""});
		rows.add(new String[] {"CAULDRONS", ""});
		rows.add(new String[] {"USABLE", "x " + inv.getUsableCauldrons()});
		rows.add(new String[] {"NEEDS BLESSING", "x " + inv.getUnusableCauldrons()});
		rows.add(new String[] {"TOTAL", "x " + inv.getCauldrons().size()});

		return rows;
	}

	/**
	* Checks whether a line is a section title rather than an actual item.
	*
	* @param row the line being checked
	* @return true if the line is a section title; false if it is an item or a blank line
	*/
	public boolean isHeader(String[] row) {
		return !row[0].isEmpty() && row[1].isEmpty();
	}

	/**
	* Summarises the player's cauldrons for the counter at the side of the screen.
	*
	* @param p the player whose cauldrons are being counted
	* @return the number of usable cauldrons over the total number of cauldrons
	*/
	public String cauldronSummary(Player p) {
		return p.getInventory().getUsableCauldrons() + " / " + p.getInventory().getCauldrons().size();
	}
}
