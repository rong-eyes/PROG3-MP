package MCO1;
import java.util.ArrayList;

public class Cauldron {
	private boolean isUsable;
  //private int capacity; for max ingredients
	private String concoctionBase;
	private ArrayList<String> ingredients;
	private boolean isBrewSuccess;
	private int cauldronNum;
	private final int price;
	
	public Cauldron() {
		isUsable = true;
		isBrewSuccess = true; //evaluated to 'true' so that the player is free to brew
		price = 3000;
	}

	public boolean isUsable() {
		return isUsable;
	}

	public void setUsable(boolean isUsable) {
		this.isUsable = isUsable;
	}

	public String getConcoctionBase() {
		return concoctionBase;
	}

	public void setConcoctionBase(String concoctionBase) {
		this.concoctionBase = concoctionBase;
	}
	
	public ArrayList<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<String> ingredients) {
		this.ingredients = ingredients;
	}

	public boolean isBrewSuccess() {
		return isBrewSuccess;
	}

	public void setBrewSuccess(boolean isBrewSuccess) {
		this.isBrewSuccess = isBrewSuccess;
	}

	public int getCauldronNum() {
		return cauldronNum;
	}

	public void setCauldronNum(int cauldronNum) {
		this.cauldronNum = cauldronNum;
	}
	
	public int getPrice() {
		return price;
	}

	public void addIngredients(String ingredient) {
		if(ingredients.size() == 3) {
			System.out.println("Cauldron is full!");
		}else {
			boolean isDuplicate = false;
			for(int i = 0; i < ingredients.size() && isDuplicate != true; i++) {
				if(ingredient == ingredients.get(i)){
					isDuplicate = true;
				}
			}
			
			if(isDuplicate) {
				System.out.println("Ingredient already in the Cauldron!");
			}else {
				ingredients.add(ingredient);
				//LINE FOR TAKING FROM INVENTORY
			}
		}
	}
	
	public void removeIngredient(int index) {
		if(ingredients.size() == 0) {
			System.out.println("Nothing to Remove.");
		}else {
				ingredients.remove(index);
		//LINE TO ADD IT BACK TO INVENTORY
		}
	}
	
	
	
}
