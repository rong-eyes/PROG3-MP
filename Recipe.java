package MCO1;
import java.util.ArrayList;

public class Recipe {
	private int concoctionID;
	private String name;
	private String concoctionBase;
	private int price;
	private ArrayList<String> ingredients;
	
	public Recipe(int id, String name, String concoctionBase, int price, ArrayList<String> ingredients) {
		concoctionID = id;
		this.name = name;
		this.concoctionBase = concoctionBase;
		this.price = price;
		this.ingredients = ingredients;
	}

	public int getConcoctionID() {
		return concoctionID;
	}

	public void setConcoctionID(int concoctionID) {
		this.concoctionID = concoctionID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConcoctionBase() {
		return concoctionBase;
	}

	public void setConcoctionBase(String concoctionBase) {
		this.concoctionBase = concoctionBase;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public ArrayList<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<String> ingredients) {
		this.ingredients = ingredients;
	}
	
	
}
