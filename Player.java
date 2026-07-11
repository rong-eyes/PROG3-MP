/*
 * Potion Prodigy (MCO1) - Player.java
 * A player: name, crystals, inventory, spellbook, and their actions.
 */
package MCO1;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Player {
	
	private final String playerName;
	private Inventory inventory;
	private int crystals;
	private Spellbook spellbook;
	private boolean loginBonusClaimed;

	/**
	* Constructs a player with the starter inventory, crystals, and recipes. This constructor is for new games.
	*
	* @param name player name
	* @param allRecipes the list of valid recipes
	*/
	public Player(String name, ArrayList<Recipe> allRecipes) {
		playerName = name;
		crystals = 5000;

		ArrayList<InventoryItem> fruits = new ArrayList<>();
		fruits.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, "STRAWBERRY", 3));
		fruits.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, "ORANGE", 2));
		fruits.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, "LEMON", 2));
		fruits.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, "BANANA", 3));
		fruits.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, "MANGO", 1));
		fruits.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, "PINEAPPLE", 0));
		fruits.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, "KIWI", 1));
		fruits.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, "BLUEBERRY", 3));
		fruits.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, "COCONUT", 0));

		ArrayList<InventoryItem> bases = new ArrayList<>();
		bases.add(new InventoryItem(InventoryItem.TYPE_BASE, "SYRUP BASE", 3));
		bases.add(new InventoryItem(InventoryItem.TYPE_BASE, "BUBBLE BASE", 3));
		bases.add(new InventoryItem(InventoryItem.TYPE_BASE, "PERFUME BASE", 1));
		bases.add(new InventoryItem(InventoryItem.TYPE_BASE, "MILK BASE", 2));
		bases.add(new InventoryItem(InventoryItem.TYPE_BASE, "LOTION BASE", 2));

		ArrayList<Cauldron> cauldrons = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			cauldrons.add(new Cauldron()); // a fresh cauldron is usable
		}

		inventory = new Inventory(fruits, bases, cauldrons);
		inventory.setUsableCauldrons(3);

		spellbook = new Spellbook();
		int[] defaultIds = {1, 2, 16, 17, 36, 37, 55, 56};
		for (int id : defaultIds) {
			Recipe recipe = RecipeLoader.findRecipeById(allRecipes, id);
			if (recipe != null) {
				spellbook.addRecipe(recipe);
			}
		}
	}

	/**
	* Constructs a player and loads the progress of a previous save onto the player. This is for load game.
	*
	* @param name player name
	* @param inventory the inventory with the items loaded onto from the saved file
	* @param crystals the amount of crystals the save file had
	* @param sb spellbook with the unlocked recipes of the save file loaded onto
	*/
	public Player(String name, Inventory inventory, int crystals, Spellbook sb) {
		playerName = name;
		this.inventory = inventory;
		this.crystals = crystals;
		spellbook = sb;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public int getCrystals() {
		return crystals;
	}

	public void setCrystals(int crystals) {
		this.crystals = crystals;
	}

	public Spellbook getSpellbook() {
		return spellbook;
	}

	public void setSpellbook(Spellbook spellbook) {
		this.spellbook = spellbook;
	}

	public String getPlayerName() {
		return playerName;
	}

	/**
	* The dispatcher for method for brewing concoctions. Tells the program to execute the correct brewing mode based on a boolean value.
	*
	* @param isCreative boolean value that indicates the brew mode; true for creative mode and false for recipe mode
	* @param cauldron the cauldron that the brew is being done on
	* @param s Scanner to be passed onto the brew methods for player interactivity
	* @param Recipes the list of valid Recipes
	* @param market the market, mostly here for the purpose of tracking successful brews for the market refresh
	*/
	public void BrewConcoction(boolean isCreative, Cauldron cauldron, Scanner s, ArrayList<Recipe> Recipes, Market market) {
		if (isCreative) {
			brewCreative(cauldron, s, Recipes, market);
		} else {
			brewRecipe(cauldron, s, market);
		}
	}

	/**
	* Conducts the process of brewing in Recipe Mode
	* <p>
	* It takes the ingredients of the recipe, as indicated in the spellbook recipes, from the player inventory. It allows the player to cancel the brew and by doing so, returns the
	* the ingredients and base to the player's inventory. This will not cause any alchemy failures since it takes recipes directly from the unlocked recipes.
	* </p>
	*
	* @param cauldron the cauldron that the brew is being done on
	* @param s Scanner to be passed onto the brew methods for player interactivity
	* @param market the market, mostly here for the purpose of tracking successful brews for the market refresh
	*/
	private void brewRecipe(Cauldron cauldron, Scanner s, Market market) {
		spellbook.printRecipes();
		if (spellbook.getUnlockedRecipes().isEmpty()) {
			System.out.println("Your spellbook has no recipes to brew yet.");
			return;
		}

		Recipe chosen = null;
		System.out.println("Enter the concoction ID to brew, or 0 to cancel:");
		while (chosen == null) {
			String line = readLine(s);
			if (line == null) {
				System.out.println("No input received. Returning to the main menu.");
				return;
			}
			int id = 0;
			boolean valid = true;
			try {
				id = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Enter a numeric concoction ID, or 0 to cancel:");
				valid = false;
			}
			if (valid) {
				if (id == 0) {
					System.out.println("Brew cancelled. Returning to the main menu.");
					return;
				}
				chosen = spellbook.getRecipe(id);
				if (chosen == null) {
					System.out.println("That concoction is not in your spellbook. Enter a valid ID, or 0 to cancel:");
				}
			}
		}

		if (!hasSufficientIngredients(chosen)) {
			System.out.println("You do not have enough ingredients to brew this concoction.");
			System.out.println("Brew cancelled. Returning to the main menu.");
			return;
		}

		System.out.println("Brew " + chosen.getName() + " for " + chosen.getPrice() + " crystals? (Y/N):");
		Boolean confirm = readYesNo(s);
		if (confirm == null) {
			System.out.println("No input received. Returning to the main menu.");
			return;
		}
		if (!confirm) {
			System.out.println("Brew cancelled. Returning to the main menu.");
			return;
		}

		inventory.removeInventory(chosen.getConcoctionBase(), 1);
		for (int i = 0; i < chosen.getIngredients().size(); i++) {
			inventory.removeInventory(chosen.getIngredients().get(i), 1);
		}

		setCrystals(getCrystals() + chosen.getPrice());
		market.recordBrew();
		System.out.println("Successfully brewed " + chosen.getName()
				+ "! It was packed into bottles and sold for " + chosen.getPrice()
				+ " crystals. You now have " + getCrystals() + " crystals.");
	}

	/**
	* Conducts the process of brewing in Creative Mode.
	* <p>
	* Lets the player choose the base and the ingredients to be brewed. Consequently, this has the chance of an alchemy failure and leads to an unusable cauldron. It doesn't allow
	* duplicate ingredients and allows the player to cancel the brew. This can cause alchemy failure-a cauldron to be unusable and require blessing-due to the invalid base-ingredient
	* combination.
	* </p>
	*
	* @param cauldron the cauldron that the brew is being done on
	* @param s Scanner to be passed onto the brew methods for player interactivity
	* @param Recipes the list of valid Recipes to cross-check if the final brew combination is valid
	* @param market the market, mostly here for the purpose of tracking successful brews for the market refresh
	*/
	private void brewCreative(Cauldron cauldron, Scanner s, ArrayList<Recipe> Recipes, Market market) {
		String[] baseNames = {"SYRUP BASE", "BUBBLE BASE", "PERFUME BASE", "MILK BASE", "LOTION BASE"};
		String[] fruitNames = {"STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO",
				"PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT"};

		// Base selection (must own the base; 0 cancels).
		String chosenBase = null;
		while (chosenBase == null) {
			System.out.println("Choose a base to brew with, or 0 to cancel:");
			System.out.println("1. SYRUP BASE  2. BUBBLE BASE  3. PERFUME BASE  4. MILK BASE  5. LOTION BASE");
			String line = readLine(s);
			if (line == null) {
				System.out.println("No input received. Returning to the main menu.");
				return;
			}
			int opt = 0;
			boolean valid = true;
			try {
				opt = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Enter 1-5, or 0 to cancel.");
				valid = false;
			}
			if (valid) {
				if (opt == 0) {
					System.out.println("Brew cancelled. Returning to the main menu.");
					return;
				}
				if (opt < 1 || opt > baseNames.length) {
					System.out.println("Invalid choice. Enter 1-5, or 0 to cancel.");
				} else {
					String candidate = baseNames[opt - 1];
					if (inventory.isInInventory(candidate, inventory.getBases()) != -1) {
						chosenBase = candidate;
						System.out.println(chosenBase + " selected as your concoction base.");
					} else {
						System.out.println("You don't own any " + candidate + ". Choose a base you own.");
					}
				}
			}
		}

		ArrayList<InventoryItem> chosenFruits = new ArrayList<>();
		boolean doneAdding = false;
		while (!doneAdding) {
			System.out.println("Fruits in the cauldron: " + fruitListString(chosenFruits)
					+ " (" + chosenFruits.size() + "/3)");
			System.out.println("Choose a fruit to add, or 0 to cancel the brew:");
			System.out.println("1. STRAWBERRY  2. ORANGE  3. LEMON  4. BANANA  5. MANGO");
			System.out.println("6. PINEAPPLE  7. KIWI  8. BLUEBERRY  9. COCONUT");
			String line = readLine(s);
			if (line == null) {
				System.out.println("No input received. Returning to the main menu.");
				return;
			}
			int opt = 0;
			boolean valid = true;
			try {
				opt = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Enter 1-9, or 0 to cancel.");
				valid = false;
			}
			if (valid) {
				if (opt == 0) {
					System.out.println("Brew cancelled. Returning to the main menu.");
					return;
				}
				if (opt < 1 || opt > fruitNames.length) {
					System.out.println("Invalid choice. Enter 1-9, or 0 to cancel.");
				} else {
					String fruit = fruitNames[opt - 1];
					if (containsName(chosenFruits, fruit)) {
						System.out.println(fruit + " is already in the cauldron; no duplicates allowed.");
					} else if (inventory.isInInventory(fruit, inventory.getIngredients()) == -1) {
						System.out.println("You don't own any " + fruit + ". Choose a fruit you own.");
					} else {
						chosenFruits.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, fruit, 1));
						System.out.println(fruit + " added to the cauldron.");

						if (chosenFruits.size() == 3) {
							System.out.println("The cauldron is full (3 fruits).");
							doneAdding = true;
						} else {
							System.out.println("Add another fruit? (Y/N):");
							Boolean more = readYesNo(s);
							if (more == null) {
								System.out.println("No input received. Returning to the main menu.");
								return;
							}
							if (!more) {
								doneAdding = true;
							}
						}
					}
				}
			}
		}

		System.out.println("Brew a concoction with " + chosenBase + " and "
				+ fruitListString(chosenFruits) + "? (Y/N):");
		Boolean confirm = readYesNo(s);
		if (confirm == null) {
			System.out.println("No input received. Returning to the main menu.");
			return;
		}
		if (!confirm) {
			System.out.println("Brew cancelled. Returning to the main menu.");
			return;
		}

		inventory.removeInventory(new InventoryItem(InventoryItem.TYPE_BASE, chosenBase, 1), 1);
		for (int i = 0; i < chosenFruits.size(); i++) {
			inventory.removeInventory(new InventoryItem(InventoryItem.TYPE_INGREDIENT, chosenFruits.get(i).getName(), 1), 1);
		}
		cauldron.setConcoctionBase(new InventoryItem(InventoryItem.TYPE_BASE, chosenBase, 1));
		cauldron.setIngredients(chosenFruits);

		Recipe result = cauldron.validBrew(Recipes);
		if (result != null) {
			market.recordBrew();
			setCrystals(getCrystals() + result.getPrice());
			boolean newlyDiscovered = spellbook.getRecipe(result.getConcoctionID()) == null;
			if (newlyDiscovered) {
				spellbook.addRecipe(result);
			}
			System.out.println("Success! You brewed " + result.getName()
					+ ", packed it into bottles, and sold it for " + result.getPrice()
					+ " crystals. You now have " + getCrystals() + " crystals.");
			if (newlyDiscovered) {
				System.out.println(result.getName() + " has been recorded in your spellbook!");
			} else {
				System.out.println(result.getName() + " is already in your spellbook.");
			}
			cauldron.cauldronFlush();
		} else {
			System.out.println("Oh no! The alchemy failed. The cauldron is now full of junk and "
					+ "unusable until it is blessed.");
			cauldron.setUsable(false);
		}
	}

	/**
	* Claims the login bonus of the player, if applicable
	*
	*/
	public void claimLoginBonus() {
		if (loginBonusClaimed) {
			System.out.println("You have already claimed your login bonus this session. "
					+ "Exit and re-enter the game to claim it again.");
			return;
		}
		String[] fruitNames = {"STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO",
				"PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT"};
		Random rng = new Random();
		String pick = fruitNames[rng.nextInt(fruitNames.length)];
		inventory.addInventory(new InventoryItem(InventoryItem.TYPE_INGREDIENT, pick, 1), 1);
		loginBonusClaimed = true;
		System.out.println("Login bonus claimed! You received 1 " + pick + ".");
	}

	/**
	* Checks if the player has the items needed for the brew. (Helper function for Recipe Mode)
	*
	* @param recipe the reference recipe for the items needed
	* @return true if the player has the items; false otherwise
	*/
	private boolean hasSufficientIngredients(Recipe recipe) {
		if (quantityOwned(recipe.getConcoctionBase().getName(), inventory.getBases()) < 1) {
			return false;
		}
		ArrayList<InventoryItem> needed = recipe.getIngredients();
		for (int i = 0; i < needed.size(); i++) {
			String name = needed.get(i).getName();
			int required = 0;
			for (int j = 0; j < needed.size(); j++) {
				if (needed.get(j).getName().equals(name)) {
					required++;
				}
			}
			if (quantityOwned(name, inventory.getIngredients()) < required) {
				return false;
			}
		}
		return true;
	}

	/**
	* Counts how much of the item being asked that the player has.
	*
	* @param name name of the item
	* @param items the list of items the player has
	* @return the quantity of the items; 0 if the player posesses none
	*/
	private int quantityOwned(String name, ArrayList<InventoryItem> items) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getName().equals(name)) {
				return items.get(i).getQuantity();
			}
		}
		return 0;
	}

	/**
	* Checks if the player has the fruit (ingredient)
	*
	* @param fruits the list of ingredients that the player has
	* @param name the ingredient being checked for
	* @return true if the player has it; false otherwise
	*/
	private boolean containsName(ArrayList<InventoryItem> fruits, String name) {
		for (int i = 0; i < fruits.size(); i++) {
			if (fruits.get(i).getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	* Turns the list of ingredients that the player has a into a string with commas separating the them.
	*
	* @param fruits list of ingredients that the player has
	* @return the string of comma separated values; returns "none" if the list is empty.
	*/
	private String fruitListString(ArrayList<InventoryItem> fruits) {
		if (fruits.isEmpty()) {
			return "(none)";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fruits.size(); i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(fruits.get(i).getName());
		}
		return sb.toString();
	}

	/**
	* Reads the next line from the scanner, removing any whitespaces
	*
	* @param s the Scanner to read from
	* @return the trimmed line
	*/
	private String readLine(Scanner s) {
		if (!s.hasNextLine()) {
			return null;
		}
		return s.nextLine().trim();
	}

	/**
	* Handles the reading of user inputted values of 'Y' or 'N' by converting them into boolean values; ignores the case of the characters for ease.
	*
	* @param s the Scanner being read from
	* @return true if the user input is 'Y' and false if 'N'. 'null' if no input.
	*/
	private Boolean readYesNo(Scanner s) {
		while (true) {
			String line = readLine(s);
			if (line == null) {
				return null;
			}
			if (line.equalsIgnoreCase("Y")) {
				return true;
			}
			if (line.equalsIgnoreCase("N")) {
				return false;
			}
			System.out.println("Please enter Y or N.");
		}
	}
}
