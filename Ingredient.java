
public class Ingredient extends InventoryItem {
	
	public Ingredient(String name, int price, int quantity) {
		super(name, price, quantity);
	}
	
	public Ingredient(String name, int quantity) {
		super(name, Market.sellPriceOf(name), quantity);
	}
}
