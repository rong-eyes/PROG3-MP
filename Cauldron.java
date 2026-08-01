import java.util.ArrayList;

public class Cauldron {
	public static final int MAX_INGREDIENTS = 3;
	public static final int BLESSING_COST = 1000;
	
	private boolean isUsable;
	private Base concoctionBase;
	private ArrayList<Ingredient> ingredients;
	private int cauldronNum;

	/**
	* Creates a cauldron and initializes it to be immediately usable with an empty ingredients list
	*
	*/
	public Cauldron() {
		isUsable = true;
		ingredients = new ArrayList<>();
	}

	public boolean isUsable() {
		return isUsable;
	}

	public void setUsable(boolean isUsable) {
		this.isUsable = isUsable;
	}

	public Base getConcoctionBase() {
		return concoctionBase;
	}

	public void setConcoctionBase(Base concoctionBase) {
		this.concoctionBase = concoctionBase;
	}

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public int getCauldronNum() {
		return cauldronNum;
	}

	public void setCauldronNum(int cauldronNum) {
		this.cauldronNum = cauldronNum;
	}

	/**
	* Adds a fruit to the cauldron while the player is still preparing a concoction.
	* <p>
	* The fruit is removed from the player's inventory as soon as it is placed in the cauldron. Nothing is
	* added if the cauldron is already full or if the same fruit is inside it, because a recipe cannot
	* repeat an ingredient.
	* </p>
	*
	* @param ingredient the fruit being added
	* @param inventory the player's inventory
	* @return true if the fruit was added; false if the cauldron is full or the fruit is a duplicate
	*/
	public boolean addIngredient(Ingredient ingredient, Inventory inventory) {
		if(ingredients.size() == MAX_INGREDIENTS)
			return false;

		if(hasIngredient(ingredient.getName()))
			return false;

		if(inventory.isInInventoryIngredient(ingredient.getName(), inventory.getIngredients()) == -1)
			return false;

		ingredients.add(new Ingredient(ingredient.getName(), 1));
		inventory.removeInventory(ingredient, 1);

		return true;
	}

	/**
	* Checks whether a fruit is already inside the cauldron.
	*
	* @param name the name of the fruit being looked for
	* @return true if the fruit is already in the cauldron; false otherwise
	*/
	public boolean hasIngredient(String name) {
		for(int i = 0; i < ingredients.size(); i++) {
			if(ingredients.get(i).getName().equals(name))
				return true;
		}

		return false;
	}
	
	/**
	* Removes a fruit from the cauldron and returns it to the player's inventory.
	*
	* @param index the index of the ingredient to be removed
	* @param inventory the player's inventory
	* @return true if an ingredient was removed; false if the index does not refer to one
	*/
	public boolean removeIngredient(int index, Inventory inventory) {
		if(index < 0 || index >= ingredients.size())
			return false;

		inventory.addInventory(ingredients.get(index), 1);
		ingredients.remove(index);

		return true;
	}

	/**
	* Adds a concoction base to the cauldron and removes it from the player's inventory.
	* If a base was added earlier, that one is returned to the inventory first so that it is not wasted.
	*
	* @param base the name of the base being added
	* @param inventory the player's inventory
	* @return true if the base was added; false if the player no longer owns that base
	*/
	public boolean addBase(String base, Inventory inventory) {
		if(inventory.isInInventoryBase(base, inventory.getBases()) == -1)
			return false;

		if(this.concoctionBase != null)
			inventory.addInventory(this.concoctionBase, 1);

		this.concoctionBase = new Base(base, 1);
		inventory.removeInventory(this.concoctionBase, 1);

		return true;
	}

	/**
	* Returns everything currently in the cauldron to the player's inventory and empties the cauldron.
	* This is called when the player cancels a brew instead of completing it.
	*
	* @param inventory the player's inventory
	*/
	public void returnContents(Inventory inventory) {
		if(this.concoctionBase != null)
			inventory.addInventory(new Base(this.concoctionBase.getName(), 1), 1);

		for(int i = 0; i < ingredients.size(); i++) {
			inventory.addInventory(new Ingredient(ingredients.get(i).getName(), 1), 1);
		}

		cauldronFlush();
	}

	/**
	* Checks if the brew is a valid recipe
	* <p>
	* Checks the player's concocted brew and cross-checks with the list of valid recipes if the combination of base and ingredients are valid.
	* </p>
	*
	* @param recipes the list of valid recipes
	* @return the Recipe(object) of the valid brew, if valid; null otherwise
	*/
	public Recipe validBrew(ArrayList<Recipe> recipes) {
		for(int i = 0; i < recipes.size(); i++) {
			Recipe candidate = recipes.get(i);
			if(candidate.getIngredients().size() == this.ingredients.size()
					&& this.concoctionBase != null
					&& candidate.getConcoctionBase().getName().equals(this.concoctionBase.getName())) {
				boolean allMatch = true;
				for(int j = 0; j < candidate.getIngredients().size() && allMatch; j++) {
					String needed = candidate.getIngredients().get(j).getName();
					boolean found = false;
					for(int k = 0; k < this.ingredients.size() && !found; k++) {
						if(this.ingredients.get(k).getName().equals(needed)) {
							found = true;
						}
					}
					if(!found) {
						allMatch = false;
					}
				}
				if(allMatch) {
					return candidate;
				}
			}
		}

		return null;
	}
	
	/**
	* Clears the base and ingredients of the cauldron.
	*
	*/
	public void cauldronFlush() {
		this.concoctionBase = null;
		this.ingredients.removeAll(ingredients);
	}
	
	/**
	* Makes the cauldron usable again.
	* <p>
	* A cauldron becomes unusable when a brew is invalid. Blessing it costs the player 1000 crystals and makes
	* it usable again. Nothing happens if the player cannot afford the blessing.
	* </p>
	*
	* @param player the player paying for the blessing
	* @return true if the cauldron was blessed; false if the player does not have enough crystals
	*/
	public boolean blessCauldron(Player player) {
		if(player.getCrystals() < BLESSING_COST)
			return false;

		player.setCrystals(player.getCrystals() - BLESSING_COST);
		cauldronFlush();
		setUsable(true);

		return true;
	}
}
