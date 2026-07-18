package MCO1;


public abstract class InventoryItem {

	protected static final String TYPE_INGREDIENT = "INGREDIENT";
	protected static final String TYPE_BASE = "BASE";

	protected String name;
	protected int price;
	protected int quantity;

	/**
	* Constructor for an Inventory item in the player profile; sets its type (Base or Ingredient), name, price (player selling price), and quantity
	*
	* @param type the item type: (Base or Ingredient)
	* @param name the name of the item
	* @param price the price that the player sells it at
	* @param quantity the amount of the item the player possesses
	*/
	public InventoryItem(String type, String name, int price, int quantity) {
		this.type = type;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	/**
	* Constructor for an Inventory item in the Market; sets its type (Base or Ingredient), name, price (Market selling price), and quantity
	*
	* @param type the item type: (Base or Ingredient)
	* @param name the name of the item
	* @param quantity the amount of the item that will be sold
	*/
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

	/**
	* Deducts the inventory item's quantity by a specified amount
	*
	* @param qty the specified amount to be deducted
	*/
	public void deductQuantity(int qty) {
		this.quantity -= qty;
	}

	/**
	* Increases the inventory item's quantity by a specified amount
	*
	* @param qty the specified amount to be added
	*/
	public void addQuantity(int qty) {
		this.quantity += qty;
	}
}
