/*
 * Potion Prodigy (MCO2) - Player.java
 * A player: name, crystals, inventory, spellbook, and their actions.
 */
import java.util.ArrayList;
import java.util.Random;

public class Player {

	//BREW RESULTS; the brewing screen converts these into pop up messages
	public static final int BREW_SUCCESS = 0;
	public static final int BREW_ALCHEMY_FAILED = 1;
	public static final int BREW_NOT_ENOUGH_INGREDIENTS = 2;
	public static final int BREW_NO_CAULDRON = 3;
	public static final int BREW_INCOMPLETE = 4;

	private static final String[] FRUIT_NAMES = {"STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO",
			"PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT"};

	private final String playerName;
	private Inventory inventory;
	private int crystals;
	private Spellbook spellbook;
	private boolean loginBonusClaimed;

	//details of the most recent brew, stored here so the screen can report the result
	private String lastConcoction;
	private int lastEarnings;
	private boolean newDiscovery;

	/**
	* Constructs a player with the starter inventory, crystals, and recipes. This constructor is for new games.
	*
	* @param name player name
	* @param allRecipes the list of valid recipes
	*/
	public Player(String name, ArrayList<Recipe> allRecipes) {
		playerName = name;
		crystals = 5000;

		ArrayList<Ingredient> fruits = new ArrayList<>();
		fruits.add(new Ingredient("STRAWBERRY", 3));
		fruits.add(new Ingredient("ORANGE", 2));
		fruits.add(new Ingredient("LEMON", 2));
		fruits.add(new Ingredient("BANANA", 3));
		fruits.add(new Ingredient("MANGO", 1));
		fruits.add(new Ingredient("PINEAPPLE", 0));
		fruits.add(new Ingredient("KIWI", 1));
		fruits.add(new Ingredient("BLUEBERRY", 3));
		fruits.add(new Ingredient("COCONUT", 0));

		ArrayList<Base> bases = new ArrayList<>();
		bases.add(new Base("SYRUP BASE", 3));
		bases.add(new Base("BUBBLE BASE", 3));
		bases.add(new Base("PERFUME BASE", 1));
		bases.add(new Base("MILK BASE", 2));
		bases.add(new Base("LOTION BASE", 2));

		ArrayList<Cauldron> cauldrons = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			cauldrons.add(new Cauldron()); // a fresh cauldron is usable
		}

		inventory = new Inventory(fruits, bases, cauldrons);

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

	/**
	* Brews a concoction that is already recorded in the player's spellbook.
	* <p>
	* Recipe mode cannot fail, because the combination came from the spellbook itself, so the cauldron is
	* left usable afterwards. The ingredients are taken from the inventory, and the finished concoction is
	* packed into bottles and sold immediately for crystals.
	* </p>
	*
	* @param chosen the recipe the player selected from the spellbook
	* @param cauldron the cauldron being used for the brew
	* @param market the market, so that the brew is counted towards the market refresh
	* @return BREW_SUCCESS if the concoction was brewed and sold; otherwise the reason it was rejected
	*/
	public int brewRecipe(Recipe chosen, Cauldron cauldron, Market market) {
		if (cauldron == null || !cauldron.isUsable()) {
			return BREW_NO_CAULDRON;
		}

		if (chosen == null) {
			return BREW_INCOMPLETE;
		}

		if (!hasSufficientIngredients(chosen)) {
			return BREW_NOT_ENOUGH_INGREDIENTS;
		}

		inventory.removeInventory(chosen.getConcoctionBase(), 1);
		for (int i = 0; i < chosen.getIngredients().size(); i++) {
			inventory.removeInventory(chosen.getIngredients().get(i), 1);
		}

		setCrystals(getCrystals() + chosen.getPrice());
		market.recordBrew();

		this.lastConcoction = chosen.getName();
		this.lastEarnings = chosen.getPrice();
		this.newDiscovery = false;

		return BREW_SUCCESS;
	}

	/**
	* Brews the combination the player mixed inside the cauldron in creative mode.
	* <p>
	* The base and the fruits are already inside the cauldron at this point, so this method only determines
	* what the combination produced. A combination listed in the compendium is packed into bottles and sold,
	* and a combination discovered for the first time is recorded in the spellbook. A combination that is not
	* in the compendium leaves junk in the cauldron, which stays unusable until it is blessed.
	* </p>
	*
	* @param cauldron the cauldron holding the combination
	* @param recipes the list of valid recipes the combination is checked against
	* @param market the market, so that a successful brew is counted towards the market refresh
	* @return BREW_SUCCESS if the combination was valid, BREW_ALCHEMY_FAILED if it was not, or the reason it was rejected
	*/
	public int brewCreative(Cauldron cauldron, ArrayList<Recipe> recipes, Market market) {
		if (cauldron == null || !cauldron.isUsable()) {
			return BREW_NO_CAULDRON;
		}

		if (cauldron.getConcoctionBase() == null || cauldron.getIngredients().isEmpty()) {
			return BREW_INCOMPLETE;
		}

		Recipe result = cauldron.validBrew(recipes);

		if (result == null) {
			this.lastConcoction = null;
			this.lastEarnings = 0;
			this.newDiscovery = false;
			cauldron.setUsable(false); //the junk remains in the cauldron until it is blessed
			return BREW_ALCHEMY_FAILED;
		}

		market.recordBrew();
		setCrystals(getCrystals() + result.getPrice());

		this.newDiscovery = spellbook.getRecipe(result.getConcoctionID()) == null;
		if (this.newDiscovery) {
			spellbook.addRecipe(result);
		}

		this.lastConcoction = result.getName();
		this.lastEarnings = result.getPrice();
		cauldron.cauldronFlush();

		return BREW_SUCCESS;
	}

	/**
	* Claims the player's login bonus, if it has not been claimed yet.
	* <p>
	* The bonus is one random fruit and can only be claimed once per session, so the player must exit and
	* re-enter the game before another one can be claimed.
	* </p>
	*
	* @return the name of the fruit the player received; null if the bonus was already claimed this session
	*/
	public String claimLoginBonus() {
		if (loginBonusClaimed) {
			return null;
		}

		Random rng = new Random();
		String pick = FRUIT_NAMES[rng.nextInt(FRUIT_NAMES.length)];
		inventory.addInventory(new Ingredient(pick, 1), 1);
		loginBonusClaimed = true;

		return pick;
	}

	/**
	* Checks if the player has the items needed for the brew. (Helper function for Recipe Mode)
	*
	* @param recipe the reference recipe for the items needed
	* @return true if the player has the items; false otherwise
	*/
	public boolean hasSufficientIngredients(Recipe recipe) {
		if (inventory.quantityOfBase(recipe.getConcoctionBase().getName()) < 1) {
			return false;
		}

		ArrayList<Ingredient> needed = recipe.getIngredients();
		for (int i = 0; i < needed.size(); i++) {
			String name = needed.get(i).getName();
			int required = 0;
			for (int j = 0; j < needed.size(); j++) {
				if (needed.get(j).getName().equals(name)) {
					required++;
				}
			}
			if (inventory.quantityOfIngredient(name) < required) {
				return false;
			}
		}

		return true;
	}

	/**
	* Checks whether the player still has a cauldron that can be used for brewing.
	*
	* @return true if at least one cauldron is usable; false otherwise
	*/
	public boolean hasUsableCauldron() {
		return inventory.getUsableCauldrons() > 0;
	}

	/**
	* Checks whether the player is allowed to enter creative mode.
	* <p>
	* Creative mode can ruin a cauldron, so it is blocked when only one usable cauldron is left. This
	* prevents the player from being left with no way to brew at all.
	* </p>
	*
	* @return true if creative mode can be entered; false if it would risk the last usable cauldron
	*/
	public boolean canBrewCreative() {
		return inventory.getUsableCauldrons() > 1;
	}

	//PUT OTHER CODES ABOVE GETTER SETTERS
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

	public boolean isLoginBonusClaimed() {
		return loginBonusClaimed;
	}

	public String getLastConcoction() {
		return lastConcoction;
	}

	public int getLastEarnings() {
		return lastEarnings;
	}

	public boolean isNewDiscovery() {
		return newDiscovery;
	}
}
