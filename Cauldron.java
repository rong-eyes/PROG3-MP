package MCO1;
import java.util.ArrayList;

public class Cauldron {
	private boolean isUsable;
	private InventoryItem concoctionBase;
	private ArrayList<InventoryItem> ingredients;
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

	public InventoryItem getConcoctionBase() {
		return concoctionBase;
	}

	public void setConcoctionBase(InventoryItem concoctionBase) {
		this.concoctionBase = concoctionBase;
	}

	public ArrayList<InventoryItem> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<InventoryItem> ingredients) {
		this.ingredients = ingredients;
	}

	public int getCauldronNum() {
		return cauldronNum;
	}

	public void setCauldronNum(int cauldronNum) {
		this.cauldronNum = cauldronNum;
	}

	/**
	* Adds ingredient/s to the cauldron when brewing
	* <p>
	* Takes the desired ingredient from the player's inventory (if any). Doesn't add the ingredient if
	* the cauldron is full or if theingredient is already in the cauldron.
	* </p>
	*
	* @param ingredient the desired ingredient that the player wants to add to the cauldron
	* @param inventory the player's inventory
	*/
	public void addIngredients(InventoryItem ingredient, Inventory inventory) {
		if(ingredients.size() == 3) {
			System.out.println("The cauldron is already full (3 ingredients).");
		}else {
			boolean isDuplicate = false;
			for(int i = 0; i < ingredients.size() && isDuplicate != true; i++) {
				if(ingredient.getName().equals(ingredients.get(i).getName())){
					isDuplicate = true;
				}
			}

			if(isDuplicate) {
				System.out.println(ingredient.getName() + " is already in the cauldron; no duplicates allowed.");
			}else {
				int index;
				ingredients.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, ingredient.getName(), 1));
				inventory.removeInventory(ingredient,1);
				index = inventory.isInInventory(ingredient.getName(), inventory.getIngredients());
				int remaining = (index == -1) ? 0 : inventory.getIngredients().get(index).getQuantity();
				System.out.println(ingredient.getName() + " added! Remaining: " + remaining);
			}
		}
	}

	/**
	* Removes ingredient/s to the cauldron when brewing
	* <p>
	* Removes an ingredient from the cauldron based on the user-inputted index and adds it back 
	* to the player's inventory. The program will inform the player if there is nothign to remove.
	* </p>
	*
	* @param index the index of the ingredient to be removed
	* @param inventory the player's inventory
	*/
	public void removeIngredient(int index, Inventory inventory) {
		if(ingredients.size() == 0) {
			System.out.println("There is nothing to remove from the cauldron.");
		}else {
				inventory.addInventory(ingredients.get(index),1);
				ingredients.remove(index);
		}
	}

	/**
	* Adds base to the cauldron when brewing
	* <p>
	* Removes an ingredient from the cauldron based on the user-inputted index and adds it back 
	* to the player's inventory. The program will inform the player if there is nothign to remove.
	* </p>
	*
	* @param index the index of the ingredient to be removed
	* @param inventory the player's inventory
	*/
	public void addBase(String base, Inventory inventory) {
		this.concoctionBase = new InventoryItem(InventoryItem.TYPE_BASE, base, 1);
		inventory.removeInventory(this.concoctionBase, 1);
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
			if(candidate.getIngredients().size() != this.ingredients.size()) {
				continue;
			}
			if(this.concoctionBase == null
					|| !candidate.getConcoctionBase().getName().equals(this.concoctionBase.getName())) {
				continue;
			}
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
	* A cauldron is made unusable if a brew is invalid. Takes 1000 crystals from the player to make the cauldron usable again. If the player doesn't have enough crystals,
	* they will be alerted.
	* </p>
	*/

	public void blessCauldron(Player player) {
		if(player.getCrystals() >= 1000) {
			player.setCrystals(player.getCrystals() - 1000);
			cauldronFlush();
			setUsable(true);
			System.out.println("Your cauldron has been blessed and is usable again! 1000 crystals spent. "
					+ "You now have " + player.getCrystals() + " crystals.");
		}else {
			System.out.println("You don't have enough crystals to bless your cauldron "
					+ "(it costs 1000, you have " + player.getCrystals() + ").");
		}
	}
}
