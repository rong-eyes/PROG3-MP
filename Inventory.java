import java.util.ArrayList;

public class Inventory {
	private ArrayList<Ingredient> ingredients;
	private ArrayList<Base> bases;
	private ArrayList<Cauldron> cauldrons;

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
	public Inventory(ArrayList<Ingredient> ingredients, ArrayList<Base> bases, ArrayList<Cauldron> cauldrons) {
		this.ingredients = ingredients;
		this.bases = bases;
		this.cauldrons = cauldrons;
	}

	//INGREDIENT
	/**
	* Looks for the item, and checks if their amount is greater than 0.
	* If it's less than 0, it will return -1, if not, it will return
	* the index number where the item is located.
	* @param name the item name that needs to be looked for
	* @param items list for items to use for searching
	* @return the index of the item if it's greater than 0, but returns -1 if less than
	* 	           or equal to zero
	*/
	public int isInInventoryIngredient(String name, ArrayList<Ingredient> items) {
		for(int i = 0; i < items.size(); i++) {
			Ingredient item = items.get(i);
			if(name.equals(item.getName()) && item.getQuantity() > 0)
				return i;
		}

		return -1;
	}

	//BASE
	/**
	* Looks for the item, and checks if their amount is greater than 0.
	* If it's less than 0, it will return -1, if not, it will return
	* the index number where the item is located.
	* @param name the item name that needs to be looked for
	* @param items list for items to use for searching
	* @return the index of the item if it's greater than 0, but returns -1 if less than
	* 	           or equal to zero
	*/
	public int isInInventoryBase(String name, ArrayList<Base> items) {
		for(int i = 0; i < items.size(); i++) {
			Base item = items.get(i);
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
	private int indexByNameIngredient(String name, ArrayList<Ingredient> items) {
		for(int i = 0; i < items.size(); i++) {
			if(name.equals(items.get(i).getName()))
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
	private int indexByNameBase(String name, ArrayList<Base> items) {
		for(int i = 0; i < items.size(); i++) {
			if(name.equals(items.get(i).getName()))
				return i;
		}
		return -1;
	}


	//INGREDIENTs
	/**
	* Adds new stock to the player's inventory, by placing it in it's base or ingredients type
	* and searches for a space for you to put the item. It doesnt creates a new entry if the item exist
	* before, but if it's a new item, it adds it in the list.
	* @param item name of the item you want to add
	* @param amount the quantity of your item that you want to add
	*/
	public void addInventory(Ingredient item, int amount) {
		int index = this.indexByNameIngredient(item.getName(), this.ingredients);
		if(index != -1)
			this.ingredients.get(index).addQuantity(amount);
		else {
			item.setQuantity(amount);
			this.ingredients.add(item);
		}
	}

	/**
	* Removes items out of the players's inventory, by using it for brewing a drink or selling a product.
	* @param item the item you will be deducting from the player's inventory
	* @param amount the quantity of how much items you will deduct from the player's inventory
	*/
	public void removeInventory(Ingredient item, int amount) {
		int index = this.indexByNameIngredient(item.getName(), this.ingredients);
		if(index != -1)
			this.ingredients.get(index).deductQuantity(amount);
	}

	//BASE
	/**
	* Adds new stock to the player's inventory, by placing it in it's base or ingredients type
	* and searches for a space for you to put the item. It doesnt creates a new entry if the item exist
	* before, but if it's a new item, it adds it in the list.
	* @param item name of the item you want to add
	* @param amount the quantity of your item that you want to add
	*/
	public void addInventory(Base item, int amount) {
		int index = this.indexByNameBase(item.getName(), this.bases);
		if(index != -1)
			this.bases.get(index).addQuantity(amount);
		else {
			item.setQuantity(amount);
			this.bases.add(item);
		}
	}

	/**
	* Removes items out of the players's inventory, by using it for brewing a drink or selling a product.
	* @param item the item you will be deducting from the player's inventory
	* @param amount the quantity of how much items you will deduct from the player's inventory
	*/
	public void removeInventory(Base item, int amount) {
		int index = this.indexByNameBase(item.getName(), this.bases);
		if(index != -1)
			this.bases.get(index).deductQuantity(amount);
	}

	/**
	* Add a new usable cauldron to the player's inventory.
	*/
	public void addCauldron() {
		Cauldron cauldron = new Cauldron();
		cauldrons.add(cauldron);
	}

	/**
	* Counts how many pieces of a fruit ingredient the player currently owns.
	*
	* @param name the name of the fruit being counted
	* @return the quantity owned; 0 if the player has none
	*/
	public int quantityOfIngredient(String name) {
		int index = this.indexByNameIngredient(name, this.ingredients);
		if(index == -1)
			return 0;

		return this.ingredients.get(index).getQuantity();
	}

	/**
	* Counts how many pieces of a concoction base the player currently owns.
	*
	* @param name the name of the base being counted
	* @return the quantity owned; 0 if the player has none
	*/
	public int quantityOfBase(String name) {
		int index = this.indexByNameBase(name, this.bases);
		if(index == -1)
			return 0;

		return this.bases.get(index).getQuantity();
	}

	/**
	* Counts the cauldrons that can still be used for brewing.
	*
	* @return the number of usable cauldrons
	*/
	public int getUsableCauldrons() {
		int usable = 0;

		for(int i = 0; i < cauldrons.size(); i++) {
			if(cauldrons.get(i).isUsable())
				usable++;
		}

		return usable;
	}

	/**
	* Counts the cauldrons that contain junk from a failed experiment and are waiting to be blessed.
	*
	* @return the number of unusable cauldrons
	*/
	public int getUnusableCauldrons() {
		return cauldrons.size() - getUsableCauldrons();
	}

	/**
	* Returns the first cauldron that can still be used for brewing.
	*
	* @return a usable Cauldron; null if every cauldron the player owns is unusable
	*/
	public Cauldron getFreeCauldron() {
		for(int i = 0; i < cauldrons.size(); i++) {
			if(cauldrons.get(i).isUsable())
				return cauldrons.get(i);
		}

		return null;
	}

	/**
	* Returns the first cauldron that is waiting to be blessed.
	*
	* @return an unusable Cauldron; null if no cauldron needs blessing
	*/
	public Cauldron getBrokenCauldron() {
		for(int i = 0; i < cauldrons.size(); i++) {
			if(!cauldrons.get(i).isUsable())
				return cauldrons.get(i);
		}

		return null;
	}

	/**
	* Collects every fruit and base the player still has stock of, which are the items the market accepts.
	* Cauldrons are excluded because they cannot be sold.
	*
	* @return the list of items with a quantity greater than 0
	*/
	public ArrayList<InventoryItem> getSellables() {
		ArrayList<InventoryItem> sellables = new ArrayList<>();

		for(int i = 0; i < ingredients.size(); i++) {
			if(ingredients.get(i).getQuantity() > 0)
				sellables.add(ingredients.get(i));
		}

		for(int i = 0; i < bases.size(); i++) {
			if(bases.get(i).getQuantity() > 0)
				sellables.add(bases.get(i));
		}

		return sellables;
	}

	// setters and getters
	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public ArrayList<Base> getBases() {
		return bases;
	}

	public void setBases(ArrayList<Base> bases) {
		this.bases = bases;
	}

	public ArrayList<Cauldron> getCauldrons() {
		return cauldrons;
	}

	public void setCauldrons(ArrayList<Cauldron> cauldrons) {
		this.cauldrons = cauldrons;
	}
}
