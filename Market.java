import java.util.Random;

public class Market {

	public static final int TRANSACTION_OK = 0;
	public static final int NOT_ENOUGH_CRYSTALS = 1;
	public static final int NOT_ENOUGH_STOCK = 2;
	public static final int NOTHING_IN_SLOT = 3;
	public static final int NOT_ENOUGH_ITEMS = 4;
	public static final int INVALID_AMOUNT = 5;

	private static final int NUM_SLOTS = 8;
	private static final int LAST_FRUIT_INDEX = 8;
	private static final int CAULDRON_INDEX = 14;
	private static final int EMPTY_SLOT = -1;
	private static final int REFRESH_THRESHOLD = 3;

	private static final String[] items = {"STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO", "PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT",
	                                        "SYRUP BASE", "BUBBLE BASE", "PERFUME BASE", "MILK BASE", "LOTION BASE", "CAULDRON"};
	private static final int[] buyPrices = {125, 80, 50, 75, 90, 240, 200, 120, 180, 50, 80, 250, 60, 150, 3000};
	private static final int[] sellPrices = {25, 40, 25, 50, 30, 120, 80, 20, 90, 10, 20, 50, 15, 25, 0};
	private Random randomizer;
	private int[][] itemSlots;
	private int maxQty;
	private int brewsSinceVisit;

	/**
	* The constructor for Market; initializes what items are to be sold to the player (at random).
	*
	*/
	public Market() {
		this.randomizer = new Random();
		this.maxQty = 5;
		this.itemSlots = new int[2][NUM_SLOTS];
		this.brewsSinceVisit = 0;
		initializeItems();
	}

	/**
	* Initializes the items to be sold at the market. Ensures that if a cauldron is being sold, it is only one and not more.
	*
	*/
	private void initializeItems() {
		boolean cauldronPlaced = false;
		for (int i = 0; i < NUM_SLOTS; i++) {
			int pick;
			if (cauldronPlaced)
				pick = randomizer.nextInt(CAULDRON_INDEX);
			else
				pick = randomizer.nextInt(CAULDRON_INDEX + 1);

			itemSlots[0][i] = pick;
			if (pick == CAULDRON_INDEX) {
				itemSlots[1][i] = 1;
				cauldronPlaced = true;
			} else {
				itemSlots[1][i] = randomizer.nextInt(maxQty) + 1;
			}
		}
	}

	/**
	* Records a successful brew. The count is used to decide whether the market restocks on the next visit.
	*
	*/
	public void recordBrew() {
		brewsSinceVisit++;
	}

	/**
	* Restocks the market if the player has brewed enough concoctions since the last visit.
	* <p>
	* This is called every time the player enters the market. The brew count is reset on every visit whether
	* or not the market was restocked, so brews beyond the required number do not carry over.
	* </p>
	*
	* @return true if the market was restocked with new items; false if the previous stock remains
	*/
	public boolean refreshIfNeeded() {
		boolean refreshed = false;

		if (brewsSinceVisit >= REFRESH_THRESHOLD) {
			initializeItems();
			refreshed = true;
		}
		brewsSinceVisit = 0;

		return refreshed;
	}

	/**
	* Returns how many slots the market displays.
	*
	* @return the number of market slots
	*/
	public int getSlotCount() {
		return NUM_SLOTS;
	}

	/**
	* Checks whether a slot has already been bought.
	*
	* @param slot the position of the slot being checked
	* @return true if the slot no longer has stock; false otherwise
	*/
	public boolean isSlotEmpty(int slot) {
		if (slot < 0 || slot >= NUM_SLOTS)
			return true;

		return itemSlots[0][slot] == EMPTY_SLOT;
	}

	/**
	* Checks whether the item in a slot is a cauldron, since cauldrons are bought one at a time.
	*
	* @param slot the position of the slot being checked
	* @return true if the slot holds a cauldron; false otherwise
	*/
	public boolean isSlotCauldron(int slot) {
		if (isSlotEmpty(slot))
			return false;

		return itemSlots[0][slot] == CAULDRON_INDEX;
	}

	/**
	* Returns the name of the item being sold in a slot.
	*
	* @param slot the position of the slot being checked
	* @return the name of the item; an empty string if the slot is empty
	*/
	public String getSlotName(int slot) {
		if (isSlotEmpty(slot))
			return "";

		return items[itemSlots[0][slot]];
	}

	/**
	* Returns how many pieces of the item remain in a slot.
	*
	* @param slot the position of the slot being checked
	* @return the quantity remaining in that slot; 0 if the slot is empty
	*/
	public int getSlotQuantity(int slot) {
		if (isSlotEmpty(slot))
			return 0;

		return itemSlots[1][slot];
	}

	/**
	* Returns how much the market charges for one piece of the item in a slot.
	*
	* @param slot the position of the slot being checked
	* @return the cost of a single piece; 0 if the slot is empty
	*/
	public int getSlotPrice(int slot) {
		if (isSlotEmpty(slot))
			return 0;

		return buyPrices[itemSlots[0][slot]];
	}

	/**
	* Handles the purchase of items from one market slot.
	* <p>
	* The player's crystals and the remaining stock are both checked before the items are handed over. Once
	* the purchase is completed, the slot is emptied even if the player did not take everything, and it stays
	* empty until the next market refresh. Cauldrons are always bought one at a time.
	* </p>
	*
	* @param slot the position of the slot being bought from
	* @param amount the quantity being bought
	* @param player the player making the purchase
	* @return TRANSACTION_OK if the purchase was completed; otherwise the reason it was rejected
	*/
	public int buySlot(int slot, int amount, Player player) {
		if (isSlotEmpty(slot))
			return NOTHING_IN_SLOT;

		int type = itemSlots[0][slot];
		String name = items[type];
		int price = buyPrices[type];

		if (type == CAULDRON_INDEX)
			amount = 1;

		if (amount <= 0)
			return INVALID_AMOUNT;

		if (type != CAULDRON_INDEX && amount > itemSlots[1][slot])
			return NOT_ENOUGH_STOCK;

		int cost = amount * price;
		if (cost > player.getCrystals())
			return NOT_ENOUGH_CRYSTALS;

		if (type == CAULDRON_INDEX)
			player.getInventory().addCauldron();
		else if (type <= LAST_FRUIT_INDEX)
			player.getInventory().addInventory(new Ingredient(name, sellPrices[type], amount), amount);
		else
			player.getInventory().addInventory(new Base(name, sellPrices[type], amount), amount);

		player.setCrystals(player.getCrystals() - cost);
		itemSlots[0][slot] = EMPTY_SLOT; //the slot becomes empty after a purchase

		return TRANSACTION_OK;
	}

	/**
	* Handles the sale of an item from the player's inventory back to the market.
	* <p>
	* Cauldrons never reach this method because they are not sellable items. The crystals given to the player
	* follow the resale price in Appendix C, which every inventory item already stores.
	* </p>
	*
	* @param item the inventory item being sold
	* @param amount the quantity being sold
	* @param player the player making the sale
	* @return TRANSACTION_OK if the sale was completed; otherwise the reason it was rejected
	*/
	public int sellItem(InventoryItem item, int amount, Player player) {
		if (item == null)
			return NOTHING_IN_SLOT;

		if (amount <= 0)
			return INVALID_AMOUNT;

		if (amount > item.getQuantity())
			return NOT_ENOUGH_ITEMS;

		int gain = amount * item.getPrice();

		if (item instanceof Base)
			player.getInventory().removeInventory((Base) item, amount);
		else
			player.getInventory().removeInventory((Ingredient) item, amount);

		player.setCrystals(player.getCrystals() + gain);

		return TRANSACTION_OK;
	}

	/**
	* Computes the cost of a purchase so that the screen can display the total before it is confirmed.
	*
	* @param slot the position of the slot being bought from
	* @param amount the quantity being bought
	* @return the total cost in crystals
	*/
	public int costOf(int slot, int amount) {
		return getSlotPrice(slot) * amount;
	}

	/**
	* Acts like a search function for the items in the Market.
	*
	* @param name the name of the item you want to find
	* @return if the item is found, it will return its index, but if not, it will return -1
	*/
	private static int catalogIndexOf(String name) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(name))
				return i;
		}
		return -1;
	}

	/**
	* Helper method that acts as the single source of truth for item resale prices.
	* This is done by giving the item name, and it will return how many crystals the
	* player gets when selling it to the market.
	*
	* @param name the name of the item you are selling
	* @return the crystals the player receives for one piece; 0 if the item is not in the catalog
	*/
	public static int sellPriceOf(String name) {
		int index = catalogIndexOf(name);
		if (index < 0) {
			return 0;
		} else {
			return sellPrices[index];
		}
	}

	/**
	* Helper method that acts as the single source of truth for how much the market charges for an item.
	*
	* @param name the name of the item being bought
	* @return the cost of one piece; 0 if the item is not in the catalog
	*/
	public static int buyPriceOf(String name) {
		int index = catalogIndexOf(name);
		if (index < 0) {
			return 0;
		} else {
			return buyPrices[index];
		}
	}

	/**
	* Returns how many concoctions have been brewed since the player last visited the market.
	*
	* @return the number of brews since the last visit
	*/
	public int getBrewsSinceVisit() {
		return brewsSinceVisit;
	}
}
