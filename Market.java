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

	private String[] items = {"STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO", "PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT",
	                          "SYRUP BASE", "BUBBLE BASE", "PERFUME BASE", "MILK BASE", "LOTION BASE", "CAULDRON"};
	private int[] buyPrices = {125, 80, 50, 75, 90, 240, 200, 120, 180, 50, 80, 250, 60, 150, 3000};
	private int[] sellPrices = {25, 40, 25, 50, 30, 120, 80, 20, 90, 10, 20, 50, 15, 25, 0};
	private Random randomizer;
	private int[][] itemSlots;
	private int maxQty;
	private int brewsSinceVisit;

	public Market() {
		this.randomizer = new Random();
		this.maxQty = 5;
		this.itemSlots = new int[2][NUM_SLOTS];
		this.brewsSinceVisit = 0;
		initializeItems();
	}

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

	public void recordBrew() {
		brewsSinceVisit++;
	}

	public void marketMain(Player player, Scanner s) {
		if (brewsSinceVisit >= REFRESH_THRESHOLD) {
			initializeItems();
			System.out.println("The market has restocked with fresh items!");
		}
		brewsSinceVisit = 0; 

		int opt = 0;
		while (opt != 3) {
			displayMarket();
			System.out.println("What would you like to do?");
			System.out.println("1. BUY   2. SELL   3. EXIT MARKET");
			opt = readIntLine(s);
			if (opt == NO_INPUT) {
				System.out.println("Leaving the market.");
				break;
			}
			switch (opt) {
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
		System.out.println("=============== THE MARKET ===============");
		for (int i = 0; i < NUM_SLOTS; i++) {
			int type = itemSlots[0][i];
			if (type == EMPTY_SLOT) {
				System.out.println((i + 1) + ". [ SOLD OUT ]");
			} else {
				System.out.println((i + 1) + ". " + items[type] + " x" + itemSlots[1][i]
						+ "   (Buy: " + buyPrices[type] + " crystals each)");
			}
		}
		System.out.println("==========================================");
	}

	private void buyItems(Player player, Scanner s) {
		System.out.println("Enter slot number(s) to buy, comma-separated (e.g. 1,3,4), or 0 to go back:");
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

		System.out.println("How many " + name + " would you like to buy? (available: " + stock
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
			player.getInventory().addInventory(new Ingredient(name, qty), qty);
		} else {
			player.getInventory().addInventory(new Base(name, qty), qty);
		}
		player.setCrystals(player.getCrystals() - cost);
		itemSlots[0][slotIndex] = EMPTY_SLOT; // the slot goes blank after a purchase
		System.out.println("Bought " + qty + " " + name + " for " + cost + " crystals. You now have "
				+ player.getCrystals() + " crystals.");
	}

	private void sellItems(Player player, Scanner s) {
		Inventory inv = player.getInventory();
		ArrayList<Object> sellables = new ArrayList<>();
		for (Ingredient ing : inv.getIngredients()) {
			if (ing.getQuantity() > 0)
				sellables.add(ing);
		}
		for (Base b : inv.getBases()) {
			if (b.getQuantity() > 0)
				sellables.add(b);
		}

		if (sellables.isEmpty()) {
			System.out.println("You have nothing to sell.");
			return;
		}

		System.out.println("------------- YOUR SELLABLE ITEMS -------------");
		for (int i = 0; i < sellables.size(); i++) {
			String nm = nameOf(sellables.get(i));
			int qty = quantityOf(sellables.get(i));
			int price = sellPrices[catalogIndexOf(nm)];
			System.out.println((i + 1) + ". " + nm + " x" + qty + "   (Sell: " + price + " crystals each)");
		}
		System.out.println("-----------------------------------------------");
		System.out.println("Enter item number(s) to sell, comma-separated (e.g. 1,2), or 0 to go back:");
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

	private void sellOneItem(Object item, Player player, Scanner s) {
		String name = nameOf(item);
		int owned = quantityOf(item);
		int catalog = catalogIndexOf(name);
		if (catalog < 0) {
			System.out.println(name + " cannot be sold here.");
			return;
		}
		int price = sellPrices[catalog];

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
		if (item instanceof Ingredient) {
			player.getInventory().removeInventory(new Ingredient(name, qty), qty);
		} else {
			player.getInventory().removeInventory(new Base(name, qty), qty);
		}
		player.setCrystals(player.getCrystals() + gain);
		System.out.println("Sold " + qty + " " + name + " for " + gain + " crystals. You now have "
				+ player.getCrystals() + " crystals.");
	}

	private String nameOf(Object item) {
		if (item instanceof Ingredient ing)
			return ing.getName();
		if (item instanceof Base b)
			return b.getName();
		return "";
	}

	private int quantityOf(Object item) {
		if (item instanceof Ingredient ing)
			return ing.getQuantity();
		if (item instanceof Base b)
			return b.getQuantity();
		return 0;
	}

	private int catalogIndexOf(String name) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(name))
				return i;
		}
		return -1;
	}

	private ArrayList<Integer> parseNumbers(String line) {
		ArrayList<Integer> nums = new ArrayList<>();
		String[] parts = line.split(",");
		for (String part : parts) {
			String token = part.trim();
			if (token.isEmpty())
				continue;
			try {
				nums.add(Integer.parseInt(token));
			} catch (NumberFormatException e) {
				System.out.println("'" + token + "' is not a valid number and was skipped.");
			}
		}
		return nums;
	}

	private String readLineOrNull(Scanner s) {
		if (!s.hasNextLine())
			return null;
		return s.nextLine().trim();
	}

	private int readIntLine(Scanner s) {
		while (true) {
			String line = readLineOrNull(s);
			if (line == null)
				return NO_INPUT;
			if (line.isEmpty()) {
				System.out.println("Please enter a number.");
				continue;
			}
			try {
				return Integer.parseInt(line);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
			}
		}
	}
}
