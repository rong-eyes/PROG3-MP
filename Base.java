
public class Base extends InventoryItem{
	
	public Base(String name, int price, int quantity) {
		super(name, price, quantity);
	}
	
	public Base(String name, int quantity) {
		super(name, Market.sellPriceOf(name), quantity);
	}
	
}
