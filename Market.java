package MCO1;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Market {

	private static final int NUM_SLOTS = 8;
	private static final int LAST_FRUIT_INDEX = 8;
	private static final int CAULDRON_INDEX = 14;
	private static final int EMPTY_SLOT = -1;
	private static final int REFRESH_THRESHOLD = 3;
	private static final int NO_INPUT = Integer.MIN_VALUE;

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

	/*
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
	* Record the number of successful brew. This is used to restock the shop in your next visit
	*/
	public void recordBrew() {
		brewsSinceVisit++;
	}

	/**
	* This opens the shop, and checks if the stock needs refreshing. This also lets the
	* player to buy, sell or leave the shop.
	* @param player name of the player
	* @param s object that reads user input from scanner
	*/
	public void marketMain(Player player, Scanner s) {
		if (brewsSinceVisit >= REFRESH_THRESHOLD) {
			initializeItems();
			System.out.println("The market has restocked with fresh items!");
		}
		brewsSinceVisit = 0; 

		int option = 0;
		while (option != 3) {
			displayMarket();
			System.out.println("What would you like to do?");
			System.out.println("1. BUY   2. SELL   3. EXIT MARKET");
			option = readIntLine(s);
			if (option == NO_INPUT) {
				System.out.println("Leaving the market.");
				break;
			}
			switch (option) {
				case 1:
					buyItems(player, s);
					break;
				case 2:
					sellItems(player, s);
					break;
				case 3:
					System.out.println("You leave the market.");
					break;
				default:
					System.out.println("Invalid choice. Please enter 1, 2, or 3.");
					break;
			}
		}
	}

	private void displayMarket() {
		System.out.println();
		System.out.println("========================= THE MARKET =========================");
		System.out.printf("%-4s%-18s%-15s%s%n", "", "Item", "Quantity", "Price");
		for (int i = 0; i < NUM_SLOTS; i++) {
			int type = itemSlots[0][i];
			if (type == EMPTY_SLOT) {
				System.out.printf("%-4s%-18s%n", (i + 1) + ".", "[ SOLD OUT ]");
			} else {
				String slotNum = (i + 1) + ".";
				String qtyStr = "x " + itemSlots[1][i];
				String priceStr = buyPrices[type] + " crystals each"; 

				System.out.printf("%-4s%-18s%-15s%s%n", slotNum, items[type], qtyStr, priceStr);
			}
		}
		System.out.println("=============================================================");
	}

	/**
	* Handles the buying transaction from the player to the market. If he item you want to buy is not empty,
	* it will ask the player for the quantity. However if the item you want to buy is empty, it will say
	* Slot x is sold out.
	* @param player the player who wants to buy the items
	* @param s input reader
	*/
	private void buyItems(Player player, Scanner s) {
		System.out.println("Enter the slot number(s) of what items you want to buy, or 0 to go back \n(e.g. 1,2 = buy the FIRST and SECOND item in the list) ");
		String line = readLineOrNull(s);
		if (line == null || line.isEmpty() || line.equals("0")) {
			System.out.println("Returning to the market menu.");
			return;
		}
		ArrayList<Integer> picks = parseNumbers(line);
		if (picks.isEmpty()) {
			System.out.println("No valid slot numbers were entered.");
			return;
		}
		for (int n : picks) {
			int i = n - 1;
			if (i < 0 || i >= NUM_SLOTS) {
				System.out.println("Slot " + n + " does not exist; choose 1-" + NUM_SLOTS + ".");
			} else if (itemSlots[0][i] == EMPTY_SLOT) {
				System.out.println("Slot " + n + " is sold out; nothing to buy there.");
			} else {
				buyOneSlot(i, player, s);
			}
		}
	}

	/**
	* Handles the purchasing of item in the market. This method ensures wheter the item is a cauldron or an item. 
	* This also validates all conditions such as input, stock and money of the player, and updates the player's 
	* inventory, and clears the market slot.
	* @param slotIndex the marketslot being purchased
	* @param player name of the player where their inventory will be used/added
 	* @param s input reader 
	*/
	private void buyOneSlot(int slotIndex, Player player, Scanner s) {
		int type = itemSlots[0][slotIndex];
		int stock = itemSlots[1][slotIndex];
		String name = items[type];
		int price = buyPrices[type];

		if (type == CAULDRON_INDEX) {
			if (player.getCrystals() >= price) {
				player.getInventory().addCauldron();
				player.setCrystals(player.getCrystals() - price);
				itemSlots[0][slotIndex] = EMPTY_SLOT;
				System.out.println("Bought 1 " + name + " for " + price + " crystals. You now have "
						+ player.getCrystals() + " crystals.");
			} else {
				System.out.println("You do not have enough crystals to buy a " + name
						+ " (need " + price + ", have " + player.getCrystals() + ").");
			}
			return;
		}

		System.out.println("How many " + name + " would you like to buy? (available: x" + stock
				+ ", " + price + " crystals each)");
		int qty = readIntLine(s);
		if (qty == NO_INPUT) {
			System.out.println("No input received. Purchase of " + name + " cancelled.");
			return;
		}
		if (qty <= 0) {
			System.out.println("Purchase of " + name + " cancelled.");
			return;
		}
		if (qty > stock) {
			System.out.println("Only " + stock + " " + name + " in stock; purchase cancelled.");
			return;
		}
		int cost = qty * price;
		if (cost > player.getCrystals()) {
			System.out.println("You do not have enough crystals to buy " + qty + " " + name
					+ " (need " + cost + ", have " + player.getCrystals() + ").");
			return;
		}

		if (type <= LAST_FRUIT_INDEX) {
			player.getInventory().addInventory(new InventoryItem(InventoryItem.TYPE_INGREDIENT, name, sellPrices[type], qty), qty);
		} else {
			player.getInventory().addInventory(new InventoryItem(InventoryItem.TYPE_BASE, name, sellPrices[type], qty), qty);
		}
		player.setCrystals(player.getCrystals() - cost);
		itemSlots[0][slotIndex] = EMPTY_SLOT; // the slot goes blank after a purchase
		System.out.println("Bought " + qty + " " + name + " for " + cost + " crystals. You now have "
				+ player.getCrystals() + " crystals.");
	}

	/**
	* Handles the seeling of items from a player's inventory to the market for crystals.
	* However, cauldrons cannot be sold, that is why it's not part of your sellable items
	* @param player name of the player where their inventory will be used/sold
 	* @param s input reader 
	*/
	private void sellItems(Player player, Scanner s) {
		Inventory inv = player.getInventory();
		ArrayList<InventoryItem> sellables = new ArrayList<>();
		for (InventoryItem ing : inv.getIngredients()) {
			if (ing.getQuantity() > 0)
				sellables.add(ing);
		}
		for (InventoryItem b : inv.getBases()) {
			if (b.getQuantity() > 0)
				sellables.add(b);
		}

		if (sellables.isEmpty()) {
			System.out.println("You have nothing to sell.");
			return;
		}

		System.out.println("----------------------- YOUR SELLABLE ITEMS -----------------------");
		System.out.printf("%-4s%-28s%-20s%s%n", "", "Item", "Quantity", "Sell");
		for (int i = 0; i < sellables.size(); i++) {
			InventoryItem item = sellables.get(i);
    		String numStr = (i + 1) + ".";
   			String qtyStr = "x" + item.getQuantity();
    		String priceStr = item.getPrice() + " crystals each";
			
			System.out.printf("%-4s%-28s%-20s%s%n", numStr, item.getName(), qtyStr, priceStr);	
		}
		System.out.println("-------------------------------------------------------------------");
		System.out.println("Enter the slot number(s) of what items you want to sell, or 0 to go back\n" + //
						"(e.g. 1,2 = sell the FIRST and SECOND item in the list)");
		String line = readLineOrNull(s);
		if (line == null || line.isEmpty() || line.equals("0")) {
			System.out.println("Returning to the market menu.");
			return;
		}
		ArrayList<Integer> picks = parseNumbers(line);
		if (picks.isEmpty()) {
			System.out.println("No valid item numbers were entered.");
			return;
		}
		for (int n : picks) {
			int i = n - 1;
			if (i < 0 || i >= sellables.size()) {
				System.out.println("Item " + n + " does not exist; choose 1-" + sellables.size() + ".");
			} else {
				sellOneItem(sellables.get(i), player, s);
			}
		}
	}

	/**
	* Handles the selling of a single selected inveotry item back to market. This checks if the player owns the item, 
	* how much they want to sell, validates if the player is selling the right amount they own, and lastly this also removes
	* the sold units from inventory.
	* @param item the item you want to sell
	* @param player name of the player where their inventory will be used/sold
 	* @param s input reader 
	*/
	private void sellOneItem(InventoryItem item, Player player, Scanner s) {
		String name = item.getName();
		int owned = item.getQuantity();
		int price = item.getPrice();

		if (owned <= 0) {
			System.out.println("You no longer have any " + name + " to sell.");
			return;
		}

		System.out.println("How many " + name + " would you like to sell? (you have: " + owned
				+ ", " + price + " crystals each)");
		int qty = readIntLine(s);
		if (qty == NO_INPUT) {
			System.out.println("No input received. Sale of " + name + " cancelled.");
			return;
		}
		if (qty <= 0) {
			System.out.println("Sale of " + name + " cancelled.");
			return;
		}
		if (qty > owned) {
			System.out.println("You only have " + owned + " " + name + "; sale cancelled.");
			return;
		}

		int gain = qty * price;
		player.getInventory().removeInventory(item, qty);
		player.setCrystals(player.getCrystals() + gain);
		System.out.println("Sold " + qty + " " + name + " for " + gain + " crystals. You now have "
				+ player.getCrystals() + " crystals.");
	}

	/**
	* Acts like a search function for the items in the Market. 
	* @param name the name of the item you want to find
	* @return if the item is found, it will return it's index, but if not, it will return -1
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
	* @param name the name of the item you are selling
	* @return the index of how much you can sell your item
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
	* Helper method that takes the comma separated values and take the valid whole numbers ito an ArrayList<Integers>
	* @param line the comma separated values (e.g. "1,2,3")
	* @return ordered parsed numbers
	*/
	private ArrayList<Integer> parseNumbers(String line) {
		ArrayList<Integer> nums = new ArrayList<>();
		String[] parts = line.split(",");
		for (String part : parts) {
			String token = part.trim();
			if (!token.isEmpty()) {
				try {
					nums.add(Integer.parseInt(token));
				} catch (NumberFormatException e) {
					System.out.println("'" + token + "' is not a valid number and was skipped.");
				}
			}
		}
		return nums;
	}

	/**
	* Clears up any useless spaces on the ends of a text
	* @param s input reader 
	* @return the string you entered without the extra space int he front and back
	*/
	private String readLineOrNull(Scanner s) {
		if (!s.hasNextLine())
			return null;
		return s.nextLine().trim();
	}

	/**
	* This is an input validator, where it asks the player to input a number when the user kept on putting letters or 
	* not a valid integer. This stops the program from going forward.
	* @param s input reader  
	* @return the valid integer the player typed, but if not, it will return "Invalid input. Please enter a number."
	*/
	private int readIntLine(Scanner s) {
		while (true) {
			String line = readLineOrNull(s);
			if (line == null)
				return NO_INPUT;
			if (line.isEmpty()) {
				System.out.println("Please enter a number.");
			} else {
				try {
					return Integer.parseInt(line);
				} catch (NumberFormatException e) {
					System.out.println("Invalid input. Please enter a number.");
				}
			}
		}
	}
}

