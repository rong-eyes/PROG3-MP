package MCO1;

public class Ingredient {
	private InventoryItem item;

	public Ingredient(String name, int quantity) {
		item = new InventoryItem(name, quantity);
	}

	public String getName() {
		return item.getName();
	}

	public void setName(String name) {
		item.setName(name);
	}

	public int getQuantity() {
		return item.getQuantity();
	}

	public void setQuantity(int qty) {
		item.setQuantity(qty);
	}

	public void deductQuantity(int qty) {
		item.deductQuantity(qty);
	}

	public void addQuantity(int qty) {
		item.addQuantity(qty);
	}
}
