package MCO1;
import java.util.ArrayList;

public class Inventory {
	private ArrayList<InventoryItem> ingredients;
	private ArrayList<InventoryItem> bases;
	private ArrayList<Cauldron> cauldrons;
	private int usableCauldrons = 0;

	public Inventory() {
		ingredients = new ArrayList<>();
		bases = new ArrayList<>();
		cauldrons = new ArrayList<>();
	}

	public Inventory(ArrayList<InventoryItem> ingredients, ArrayList<InventoryItem> bases, ArrayList<Cauldron> cauldrons) {
		this.ingredients = ingredients;
		this.bases = bases;
		this.cauldrons = cauldrons;
		for(int i = 0; i < cauldrons.size(); i++) {
			if(cauldrons.get(i).isUsable())
				usableCauldrons++;
		}
	}

	public ArrayList<InventoryItem> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<InventoryItem> ingredients) {
		this.ingredients = ingredients;
	}

	public ArrayList<InventoryItem> getBases() {
		return bases;
	}

	public void setBases(ArrayList<InventoryItem> bases) {
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

	public int isInInventory(String name, ArrayList<InventoryItem> items) { //returns index
		for(int i = 0; i < items.size(); i++) {
			InventoryItem item = items.get(i);
			if(name.equals(item.getName()) && item.getQuantity() > 0)
				return i;
		}

		return -1;
	}

	private int indexByName(String name, ArrayList<InventoryItem> items) {
		for(int i = 0; i < items.size(); i++) {
			if(name.equals(items.get(i).getName()))
				return i;
		}

		return -1;
	}

	private ArrayList<InventoryItem> listFor(InventoryItem item) { //route by type tag
		return InventoryItem.TYPE_BASE.equals(item.getType()) ? bases : ingredients;
	}

	public void addInventory(InventoryItem item, int amount) { //fruit or base, picked by type
		ArrayList<InventoryItem> target = listFor(item);
		int index = this.indexByName(item.getName(), target);
		if(index != -1)
			target.get(index).addQuantity(amount);
		else {
			item.setQuantity(amount);
			target.add(item);
		}
	}

	public void removeInventory(InventoryItem item, int amount) { //fruit or base, picked by type
		ArrayList<InventoryItem> target = listFor(item);
		int index = this.indexByName(item.getName(), target);
		if(index != -1)
			target.get(index).deductQuantity(amount);
	}

	public void addCauldron() {
		Cauldron cauldron = new Cauldron();
		cauldrons.add(cauldron);
	}

	public void displayInventory() {
		int i;
		int usable = 0;

		for(i = 0; i < ingredients.size(); i++) {
			InventoryItem tmp = ingredients.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}

		System.out.print("\n");

		for(i = 0; i < bases.size(); i++) {
			InventoryItem tmp = bases.get(i);
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
			InventoryItem tmp = ingredients.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}

		System.out.print("\n");

		for(i = 0; i < bases.size(); i++) {
			InventoryItem tmp = bases.get(i);
			System.out.println(tmp.getName() + " = " + tmp.getQuantity());
		}

		System.out.print("\n");
	}
}
