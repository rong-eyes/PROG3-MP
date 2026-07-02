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
			if(!cauldrons.get(i).isUsable())
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

	public int isInInventory(String ingredient, ArrayList<? extends InventoryItem> items) { //returns index
		for(int i = 0; i < items.size(); i++) {
			if(ingredient.equals(items.get(i).getName()) && items.get(i).getQuantity() > 0)
				return i;
		}
		
		return -1;
	}
	
	//the ff. functions are not strings because ingredient and base are a subclass of Inventory Item
	
	public void addInventory(Ingredient ingredient, int amount) { //for selling
		int index = this.isInInventory(ingredient.getName(), this.ingredients);
		if(index != -1)
			this.ingredients.get(index).addQuantity(amount);
		else {
			ingredient.setQuantity(amount); 
			this.ingredients.add(ingredient);
		}
	}
	
	public void addInventory(Base base, int amount) { //method overload
		int index = this.isInInventory(base.getName(), this.bases);
		if(index != -1)
			this.bases.get(index).addQuantity(amount);
		else {
			base.setQuantity(amount); 
			this.bases.add(base);
		}
	}
	
	public void removeInventory(Ingredient ingredient, int amount) {
		for(int i = 0; i < this.ingredients.size(); i++) {
			if(this.ingredients.get(i).getName().equals(ingredient.getName())) {
				this.ingredients.get(i).deductQuantity(amount);
				if(this.ingredients.get(i).getQuantity() == 0)
					ingredients.remove(i);	
			}
		}
	}
	
	public void removeInventory(Base base, int amount) { //method overload
		for(int i = 0; i < this.bases.size(); i++) {
			if(this.bases.get(i).getName().equals(base.getName())) {
				this.bases.get(i).deductQuantity(base.getQuantity());
				if(this.bases.get(i).getQuantity() == 0)
					bases.remove(i);
			}
		}
	}
	
	public void addCauldron() {
		Cauldron cauldron = new Cauldron();
		cauldrons.add(cauldron);
	}
	
	public void displayInventory() {
		InventoryItem tmp;
		int i;
		int usable = 0;
		
		for(i = 0; i < ingredients.size(); i++) {
			tmp = ingredients.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}
		
		System.out.print("\n");
		
		for(i = 0; i < bases.size(); i++) {
			tmp = bases.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}
		
		System.out.print("\n");
		
		for(i = 0; i < cauldrons.size(); i++) {
			if(cauldrons.get(i).isUsable())
				usable++;
		}
		
		System.out.println("Total Cauldrons: " + cauldrons.size());
		System.out.println("Usable Cauldrons: " + usable);
	}
	
	public void displaySellables() {
		InventoryItem tmp;
		int i;
		
		for(i = 0; i < ingredients.size(); i++) {
			tmp = ingredients.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}
		
		System.out.print("\n");
		
		for(i = 0; i < bases.size(); i++) {
			tmp = bases.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}
		
		System.out.print("\n");
	}
}
