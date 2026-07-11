package MCO1;
import java.util.ArrayList;

public class Inventory {
	private ArrayList<InventoryItem> ingredients;
	private ArrayList<InventoryItem> bases;
	private ArrayList<Cauldron> cauldrons;
	private int usableCauldrons = 0;

	/**
	* Creates an empty Inventory, with no data for ingredients, bases, and cauldrons.
	*/
	public Inventory() {
		ingredients = new ArrayList<>();
		bases = new ArrayList<>();
		cauldrons = new ArrayList<>();
	}

	/**
	* Creates inventory with data for the ingredients, bases and cauldrons. This is used for starting a new game 
	*	and opening a Load Save inventory for old players.
	* @param ingredients the stack for fruit ingredients 
	* @param bases the stack for concoction base
	* @param cauldrons holds the number of used and unused cauldrons
	*/
	public Inventory(ArrayList<InventoryItem> ingredients, ArrayList<InventoryItem> bases, ArrayList<Cauldron> cauldrons) {
		this.ingredients = ingredients;
		this.bases = bases;
		this.cauldrons = cauldrons;
		for(int i = 0; i < cauldrons.size(); i++) {
			if(cauldrons.get(i).isUsable())
				usableCauldrons++;
		}
	}

	public ArrayList<InventoryItem> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<InventoryItem> ingredients) {
		this.ingredients = ingredients;
	}

	public ArrayList<InventoryItem> getBases() {
		return bases;
	}

	public void setBases(ArrayList<InventoryItem> bases) {
		this.bases = bases;
	}

	public ArrayList<Cauldron> getCauldrons() {
		return cauldrons;
	}

	public void setCauldrons(ArrayList<Cauldron> cauldrons) {
		this.cauldrons = cauldrons;
	}

	public int getUsableCauldrons() {
		return usableCauldrons;
	}

	public void setUsableCauldrons(int usableCauldrons) {
		this.usableCauldrons = usableCauldrons;
	}

	/**
	* Looks for the item, and checks if their amount is greater than 0.
	* If it's less than 0, it will return -1, if not, it will return 
	* the index number where the item is located.
	* @param name the item name that needs to be looked for
	* @param items list for items to use for searching
	* @return the index of the item if it's greater than 0, but returns -1 if less than 
	* 	           or equal to zero
	*/
	public int isInInventory(String name, ArrayList<InventoryItem> items) { 
		for(int i = 0; i < items.size(); i++) {
			InventoryItem item = items.get(i);
			if(name.equals(item.getName()) && item.getQuantity() > 0)
				return i;
		}

		return -1;
	}

	/**
	* Checks if the items is in the list, regardless of it's quantity. 
	* This is a helper function used to compare if your the name you are looking for is there.
	* @param name item's name that you are looking for
	* @param items array of items to use for searching
	* @return the index number if the item is found, but if the item is not part of the
	*            list, it will return -1 
	*/
	private int indexByName(String name, ArrayList<InventoryItem> items) {
		for(int i = 0; i < items.size(); i++) {
			if(name.equals(items.get(i).getName()))
				return i;
		}
		return -1;
	}

	/**
	* Looks for which type the item exists. Specifically if it's for bases or ingredients.
	* @param item the item where you want to know which type it belongs to, if it's base
	*		or ingredients
	* @return the inventory's list for your base or ingredient
	*/
	private ArrayList<InventoryItem> listFor(InventoryItem item) { 
		if (item.getType().equals(InventoryItem.TYPE_BASE)){
			return bases;
		} else {
			return ingredients;
		}
	}

	/**
	* Adds new stock to the player's inventory, by placing it in it's base or ingredients type
	* and searches for a space for you to put the item. It doesnt creates a new entry if the item exist
	* before, but if it's a new item, it adds it in the list.
	* @param item name of the item you want to add
	* @param amount the quantity of your item that you want to add
	*/
	public void addInventory(InventoryItem item, int amount) { 
		ArrayList<InventoryItem> target = listFor(item);
		int index = this.indexByName(item.getName(), target);
		if(index != -1)
			target.get(index).addQuantity(amount);
		else {
			item.setQuantity(amount);
			target.add(item);
		}
	}

	/**
	* Removes items out of the players's inventory, by using it for brewing a drink or selling a product.
	* @param item the item you will be deducting from the player's inventory
	* @param amount the quantity of how much items you will deduct from the player's inventory
	*/
	public void removeInventory(InventoryItem item, int amount) {
		ArrayList<InventoryItem> target = listFor(item);
		int index = this.indexByName(item.getName(), target);
		if(index != -1)
			target.get(index).deductQuantity(amount);
	}

	/**
	* Add a new usable cauldron to the player's inventory.
	*/
	public void addCauldron() {
		Cauldron cauldron = new Cauldron();
		cauldrons.add(cauldron);
	}

	public void displayInventory() {
		int i;
		int usable = 0;

		for(i = 0; i < ingredients.size(); i++) {
			InventoryItem tmp = ingredients.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}

		System.out.print("\n");

		for(i = 0; i < bases.size(); i++) {
			InventoryItem tmp = bases.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}

		System.out.print("\n");

		for(i = 0; i < cauldrons.size(); i++) {
			if(cauldrons.get(i).isUsable())
				usable++;
		}

		System.out.println("Total Cauldrons: " + cauldrons.size());
		System.out.println("Usable Cauldrons: " + usable);
		System.out.println("Unusable Cauldrons: " + (cauldrons.size() - usable));
	}

	public void displaySellables() {
		int i;

		for(i = 0; i < ingredients.size(); i++) {
			InventoryItem tmp = ingredients.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}

		System.out.print("\n");

		for(i = 0; i < bases.size(); i++) {
			InventoryItem tmp = bases.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}

		System.out.print("\n");
	}
}
