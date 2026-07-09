package MCO1;
import java.util.ArrayList;

public class Inventory {
	private ArrayList<Ingredient> ingredients;
	private ArrayList<Base> bases;
	private ArrayList<Cauldron> cauldrons;
	private int usableCauldrons = 0;

	public Inventory() {
		ingredients = new ArrayList<>();
		bases = new ArrayList<>();
		cauldrons = new ArrayList<>();
	}

	public Inventory(ArrayList<Ingredient> ingredients, ArrayList<Base> bases, ArrayList<Cauldron> cauldrons) {
		this.ingredients = ingredients;
		this.bases = bases;
		this.cauldrons = cauldrons;
		for(int i = 0; i < cauldrons.size(); i++) {
			if(cauldrons.get(i).isUsable())
				usableCauldrons++;
		}
	}

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

	public int getUsableCauldrons() {
		return usableCauldrons;
	}

	public void setUsableCauldrons(int usableCauldrons) {
		this.usableCauldrons = usableCauldrons;
	}

	public int isInInventory(String name, ArrayList<?> items) { //returns index
		for(int i = 0; i < items.size(); i++) {
			String itemName = null;
			int quantity = 0;
			if(items.get(i) instanceof Ingredient ing) {
				itemName = ing.getName();
				quantity = ing.getQuantity();
			} else if(items.get(i) instanceof Base base) {
				itemName = base.getName();
				quantity = base.getQuantity();
			}
			if(itemName != null && name.equals(itemName) && quantity > 0)
				return i;
		}

		return -1;
	}

	private int indexByName(String name, ArrayList<?> items) {
		for(int i = 0; i < items.size(); i++) {
			String itemName = null;
			if(items.get(i) instanceof Ingredient ing) {
				itemName = ing.getName();
			} else if(items.get(i) instanceof Base base) {
				itemName = base.getName();
			}
			if(itemName != null && name.equals(itemName))
				return i;
		}

		return -1;
	}

	public void addInventory(Ingredient ingredient, int amount) { //for selling
		int index = this.indexByName(ingredient.getName(), this.ingredients);
		if(index != -1)
			this.ingredients.get(index).addQuantity(amount);
		else {
			ingredient.setQuantity(amount);
			this.ingredients.add(ingredient);
		}
	}

	public void addInventory(Base base, int amount) { //method overload
		int index = this.indexByName(base.getName(), this.bases);
		if(index != -1)
			this.bases.get(index).addQuantity(amount);
		else {
			base.setQuantity(amount);
			this.bases.add(base);
		}
	}

	public void removeInventory(Ingredient ingredient, int amount) {
		int index = this.indexByName(ingredient.getName(), this.ingredients);
		if(index != -1)
			this.ingredients.get(index).deductQuantity(amount);
	}

	public void removeInventory(Base base, int amount) { //method overload
		int index = this.indexByName(base.getName(), this.bases);
		if(index != -1)
			this.bases.get(index).deductQuantity(amount);
	}

	public void addCauldron() {
		Cauldron cauldron = new Cauldron();
		cauldrons.add(cauldron);
	}

	public void displayInventory() {
		int i;
		int usable = 0;

		for(i = 0; i < ingredients.size(); i++) {
			Ingredient tmp = ingredients.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}

		System.out.print("\n");

		for(i = 0; i < bases.size(); i++) {
			Base tmp = bases.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}

		System.out.print("\n");

		for(i = 0; i < cauldrons.size(); i++) {
			if(cauldrons.get(i).isUsable())
				usable++;
		}

		System.out.println("Total Cauldrons: " + cauldrons.size());
		System.out.println("Usable Cauldrons: " + usable);
		System.out.println("Unusable Cauldrons: " + (cauldrons.size() - usable));
	}

	public void displaySellables() {
		int i;

		for(i = 0; i < ingredients.size(); i++) {
			Ingredient tmp = ingredients.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}

		System.out.print("\n");

		for(i = 0; i < bases.size(); i++) {
			Base tmp = bases.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}
		System.out.print("\n");
	}
}
