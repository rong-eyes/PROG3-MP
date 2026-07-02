package MCO1;
import java.util.ArrayList;

public class Cauldron {
	private boolean isUsable;
	private Base concoctionBase;
	private ArrayList<Ingredient> ingredients;
	private int cauldronNum;					//To select a cauldron in brew or bless				
	
	public Cauldron() {
		isUsable = true;
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
				if(ingredient.getName() == ingredients.get(i).getName()){
					isDuplicate = true;
				}
			}
			
			if(isDuplicate) {
				System.out.println("Ingredient already in the Cauldron!");
			}else {
				int index;
				ingredients.add(ingredient);
				inventory.removeInventory(ingredient,1);
				index = inventory.isInInventory(ingredient.getName(), inventory.getIngredients());
				System.out.println(ingredient.getName() + " Added! Remaining:" + inventory.getIngredients().get(index).getQuantity());
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
		concoctionBase.setName(base);
		concoctionBase.setQuantity(1);
		inventory.removeInventory(concoctionBase, 1);
	}
	
	public Recipe validBrew(ArrayList<Recipe> Recipe) {
		for(int i = 0; i < Recipe.size(); i++) {
			if(Recipe.get(i).getIngredients().size() == this.ingredients.size()) {
				if(Recipe.get(i).getIngredients() == this.ingredients && Recipe.get(i).getConcoctionBase() == this.concoctionBase) {
					return Recipe.get(i);
				}
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
		if(player.getCrystals() == 1000) {
			setUsable(true);
			System.out.println("Cauldron Blessed!");
		}else {
			System.out.println("You do not have enough crystals.");
		}
	}
}
