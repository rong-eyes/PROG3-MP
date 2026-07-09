package MCO1;

public class InventoryItem {
	private String name;
	private int quantity;

	public InventoryItem(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int qty) {
		quantity = qty;
	}

	public void deductQuantity(int qty) {
		this.quantity -= qty;
	}

	public void addQuantity(int qty) {
		this.quantity += qty;
	}
}
