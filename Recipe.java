package MCO1;
import java.util.ArrayList;

public class Recipe {
	private final int concoctionID;
	private final String name;					//name of the potion
	private final InventoryItem concoctionBase;		    //base of the potion
	private final int price;
	private final ArrayList<InventoryItem> ingredients;//ArrayList of ingredients; can be one, can be 3

	/**
	* Constructs the object for the valid Recipe
	*
	* @param id the unique identifier for each valid recipe
	* @param name the name of the concoction
	* @param concoctionBase the name of the base of the concoction
	* @param price the selling price of the concoction (the amount of crystals the player will receive when concocted)
	* @param ingredients the list of the ingredient combination that the recipe has
	*/
	public Recipe(int id, String name, String concoctionBase, int price, ArrayList<InventoryItem> ingredients) {
		concoctionID = id;
		this.name = name;
		this.concoctionBase = new InventoryItem(InventoryItem.TYPE_BASE, concoctionBase, 1);
		this.price = price;
		this.ingredients = ingredients;
	}

	public int getConcoctionID() {
		return concoctionID;
	}

	public String getName() {
		return name;
	}

	public InventoryItem getConcoctionBase() {
		return concoctionBase;
	}

	public int getPrice() {
		return price;
	}

	public ArrayList<InventoryItem> getIngredients() {
		return ingredients;
	}
}
