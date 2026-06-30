package MCO1;
import java.util.ArrayList;

public class Recipe {
	private final int concoctionID;
	private final String name;					//name of the potion
	private final String concoctionBase;		//base of the potion
	private final int price;
	private final ArrayList<Ingredient> ingredients;//ArrayList of ingredients; can be one, can be 3
	
	public Recipe(int id, String name, String concoctionBase, int price, ArrayList<Ingredient> ingredients) {
		concoctionID = id;
		this.name = name;
		this.concoctionBase = concoctionBase;
		this.price = price;
		this.ingredients = ingredients;
	}

	public int getConcoctionID() {
		return concoctionID;
	}

	public String getName() {
		return name;
	}


	public String getConcoctionBase() {
		return concoctionBase;
	}


	public int getPrice() {
		return price;
	}

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}
}
