import java.util.ArrayList;

public class MarketModel { //Model for the Market screens; wraps the Market so the screens never touch it directly

	private Market market;
	private int selectedSlot;

	/**
	* Creates the model using the market of the current session.
	*
	* @param market the market being used for this session
	*/
	public MarketModel(Market market) {
		this.market = market;
		this.selectedSlot = -1;
	}

	/**
	* Enters the market and refreshes its stock if the refresh condition has been met.
	*
	* @return true if the market was restocked with new items; false if the previous stock remains
	*/
	public boolean visitMarket() {
		return market.refreshIfNeeded();
	}

	/**
	* Returns the number of slots the market displays.
	*
	* @return the number of market slots
	*/
	public int getSlotCount() {
		return market.getSlotCount();
	}

	/**
	* Checks whether a market slot has already been bought.
	*
	* @param slot the position of the slot being checked
	* @return true if the slot no longer has stock; false otherwise
	*/
	public boolean isSlotEmpty(int slot) {
		return market.isSlotEmpty(slot);
	}

	/**
	* Returns the name of the item being sold in a market slot.
	*
	* @param slot the position of the slot being checked
	* @return the name of the item in that slot
	*/
	public String slotName(int slot) {
		return market.getSlotName(slot);
	}

	/**
	* Formats the remaining quantity of a market slot for display on the shelf.
	*
	* @param slot the position of the slot being checked
	* @return the remaining quantity as text, such as "x 3"
	*/
	public String slotStock(int slot) {
		return "x " + market.getSlotQuantity(slot);
	}

	/**
	* Returns the cost of one piece of the item in a market slot.
	*
	* @param slot the position of the slot being checked
	* @return the cost of a single piece
	*/
	public int slotPrice(int slot) {
		return market.getSlotPrice(slot);
	}

	/**
	* Returns how many pieces a purchase from a market slot covers.
	* <p>
	* The market does not sell part of a slot, so buying from one takes its entire stock. A slot holding
	* a cauldron is the exception, because cauldrons are always sold one at a time.
	* </p>
	*
	* @param slot the position of the slot being bought from
	* @return the quantity held by that slot
	*/
	public int lotSize(int slot) {
		if(market.isSlotEmpty(slot))
			return 0;

		if(market.isSlotCauldron(slot))
			return 1;

		return market.getSlotQuantity(slot);
	}

	/**
	* Checks whether the player has enough crystals to buy the entire stock of a market slot.
	*
	* @param slot the position of the slot being bought from
	* @param p the player making the purchase
	* @return true if the player can afford the whole slot; false otherwise
	*/
	public boolean canAffordLot(int slot, Player p) {
		return costOf(slot, lotSize(slot)) <= p.getCrystals();
	}

	/**
	* Checks whether a market slot is selling a cauldron, which is always bought one at a time.
	*
	* @param slot the position of the slot being checked
	* @return true if the slot holds a cauldron; false otherwise
	*/
	public boolean isSlotCauldron(int slot) {
		return market.isSlotCauldron(slot);
	}

	/**
	* Computes the total cost of buying a given quantity from a market slot.
	*
	* @param slot the position of the slot being bought from
	* @param amount the quantity being bought
	* @return the total cost in crystals
	*/
	public int costOf(int slot, int amount) {
		return market.costOf(slot, amount);
	}

	/**
	* Carries out a purchase from a market slot.
	*
	* @param slot the position of the slot being bought from
	* @param amount the quantity being bought
	* @param p the player making the purchase
	* @return the result of the transaction
	*/
	public int buy(int slot, int amount, Player p) {
		return market.buySlot(slot, amount, p);
	}

	/**
	* Carries out the sale of an item to the market.
	*
	* @param item the item being sold
	* @param amount the quantity being sold
	* @param p the player making the sale
	* @return the result of the transaction
	*/
	public int sell(InventoryItem item, int amount, Player p) {
		return market.sellItem(item, amount, p);
	}

	/**
	* Returns every item in the player's inventory that the market accepts.
	*
	* @param p the player making the sale
	* @return the list of items the player still has stock of
	*/
	public ArrayList<InventoryItem> sellables(Player p) {
		return p.getInventory().getSellables();
	}

	/**
	* Builds the message displayed to the player after a purchase.
	*
	* @param status the result returned by the market
	* @param name the name of the item involved
	* @param amount the quantity involved
	* @param p the player, whose remaining crystals are included in the message
	* @return the message to be shown in the pop up
	*/
	public String buyMessage(int status, String name, int amount, Player p) {
		String message;

		if(status == Market.TRANSACTION_OK && amount == 1)
			message = "Bought the last " + name + " on the stall. You now have " + p.getCrystals() + " crystals.";
		else if(status == Market.TRANSACTION_OK)
			message = "Bought all " + amount + " " + name + " and cleared the stall out. You now have "
					+ p.getCrystals() + " crystals.";
		else if(status == Market.NOT_ENOUGH_CRYSTALS)
			message = "You do not have enough crystals for that. You only have " + p.getCrystals() + ".";
		else if(status == Market.NOT_ENOUGH_STOCK)
			message = "The merchant does not have that many " + name + " left.";
		else if(status == Market.NOTHING_IN_SLOT)
			message = "That stall has already been bought out.";
		else
			message = "Pick how many " + name + " you want first.";

		return message;
	}

	/**
	* Builds the message displayed to the player after a sale.
	*
	* @param sold the total quantity of items that were sold
	* @param earned the crystals the player received for them
	* @param p the player, whose remaining crystals are included in the message
	* @return the message to be shown in the pop up
	*/
	public String sellMessage(int sold, int earned, Player p) {
		if(sold == 0)
			return "Nothing was sold. Tick the items you want the merchant to take.";

		return "Sold " + sold + " item(s) for " + earned + " crystals. You now have "
				+ p.getCrystals() + " crystals.";
	}

	//PUT OTHER CODES ABOVE GETTER SETTERS
	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

	public int getSelectedSlot() {
		return selectedSlot;
	}

	public void setSelectedSlot(int selectedSlot) {
		this.selectedSlot = selectedSlot;
	}
}
