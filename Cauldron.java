package MCO1;
import java.util.ArrayList;

public class Cauldron {
	private boolean isUsable;
	private InventoryItem concoctionBase;
	private ArrayList<InventoryItem> ingredients;
	private int cauldronNum;

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

	public void removeIngredient(int index, Inventory inventory) {
		if(ingredients.size() == 0) {
			System.out.println("There is nothing to remove from the cauldron.");
		}else {
				inventory.addInventory(ingredients.get(index),1);
				ingredients.remove(index);
		}
	}

	public void addBase(String base, Inventory inventory) {
		this.concoctionBase = new InventoryItem(InventoryItem.TYPE_BASE, base, 1);
		inventory.removeInventory(this.concoctionBase, 1);
	}

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

	public void cauldronFlush() {
		this.concoctionBase = null;
		this.ingredients.removeAll(ingredients);
	}

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
