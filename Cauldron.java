package MCO1;
import java.util.ArrayList;

public class Cauldron {
	private boolean isUsable;
	private Base concoctionBase;
	private ArrayList<Ingredient> ingredients;
	private int cauldronNum;					//To select a cauldron in brew or bless				
	
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
	
	public void addIngredients(Ingredient ingredient, Inventory inventory) {
		if(ingredients.size() == 3) {
			System.out.println("Cauldron is full!");
		}else {
			boolean isDuplicate = false;
			for(int i = 0; i < ingredients.size() && isDuplicate != true; i++) {
				if(ingredient.getName().equals(ingredients.get(i).getName())){
					isDuplicate = true;
				}
			}
			
			if(isDuplicate) {
				System.out.println(ingredient.getName() + "already in the Cauldron!");
			}else {
				int index;
				ingredients.add(new Ingredient(ingredient.getName(), 1));
				inventory.removeInventory(ingredient,1);
				index = inventory.isInInventory(ingredient.getName(), inventory.getIngredients());
				int remaining = (index == -1) ? 0 : inventory.getIngredients().get(index).getQuantity();
				System.out.println(ingredient.getName() + " added! Remaining: " + remaining);
			}
		}
	}
	
	public void removeIngredient(int index, Inventory inventory) {
		if(ingredients.size() == 0) {
			System.out.println("Nothing to Remove.");
		}else {
				inventory.addInventory(ingredients.get(index),1);
				ingredients.remove(index);
		}
	}
	
	public void addBase(String base, Inventory inventory) { //cannot remove base since its the start, just accept it <3
		this.concoctionBase = new Base(base, 1);
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
	
	
	//don't give this option to the player if there are no cauldrons to be blessed
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
