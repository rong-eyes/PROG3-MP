package MCO1;


public class InventoryItem {

	public static final String TYPE_INGREDIENT = "INGREDIENT";
	public static final String TYPE_BASE = "BASE";

	private String type;
	private String name;
	private int price;
	private int quantity;

	public InventoryItem(String type, String name, int price, int quantity) {
		this.type = type;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public InventoryItem(String type, String name, int quantity) {
		this(type, name, Market.sellPriceOf(name), quantity);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
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
