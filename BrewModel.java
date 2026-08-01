import java.util.ArrayList;

public class BrewModel { 

	public static final int STAGE_BASE = 0;
	public static final int STAGE_FRUIT = 1;

	private static final String[] BASE_NAMES = {"SYRUP BASE", "BUBBLE BASE", "PERFUME BASE", "MILK BASE", "LOTION BASE"};
	private static final String[] FRUIT_NAMES = {"STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO", "PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT"};

	private boolean creative;
	private ArrayList<Recipe> allRecipes;
	private Market market;
	private int stage;

	/**
	* Creates the model for a single visit to the brewing screen.
	*
	* @param creative true if the player entered creative mode or false for recipe mode
	* @param allRecipes the list of all valid recipes, used to check what a creative combination produced
	* @param market the market, so that a successful brew is counted towards the market refresh
	*/
	public BrewModel(boolean creative, ArrayList<Recipe> allRecipes, Market market) {
		this.creative = creative;
		this.allRecipes = allRecipes;
		this.market = market;
		this.stage = STAGE_BASE;
	}

	/**
	* Returns the recipes that are recorded in the player's spellbook, this displays the recipes
	* the player has unlocked after a successfull brew
	*
	* @param p the player whose spellbook is being read
	* @return the unlocked recipes, sorted by concoction ID
	*/
	public ArrayList<Recipe> unlockedRecipes(Player p) {
		return p.getSpellbook().getUnlockedRecipes();
	}

	/**
	* The format for the recipe for display on the list.
	* 
	* @param r the recipe being formatted
	* @return the ID, name, price, base and ingredients of the recipe on a single line
	*/
	public String recipeLabel(Recipe r) {
		return String.format("[%03d] %s  -  %d  (%s + %s)", r.getConcoctionID(), r.getName(),
				r.getPrice(), r.getConcoctionBase().getName(), Spellbook.ingredientsToString(r));
	}

	/**
	* The format of a concoction base for display on the list, with their quantity remaining.
	*
	* @param index the position of the base on the list
	* @param p the player whose stock is being counted
	* @return the name of the base and the quantity the player owns
	*/
	public String baseLabel(int index, Player p) {
		return BASE_NAMES[index] + "  (x" + p.getInventory().quantityOfBase(BASE_NAMES[index]) + ")";
	}

	/**
	* The format of a fruit for display on the list witht heir quantity remaining.
	*
	* @param index the position of the fruit on the list
	* @param p the player whose stock is being counted
	* @return the name of the fruit and the quantity the player owns
	*/
	public String fruitLabel(int index, Player p) {
		return FRUIT_NAMES[index] + "  (x" + p.getInventory().quantityOfIngredient(FRUIT_NAMES[index]) + ")";
	}

	/**
	* Checks whether the player still owns a concoction base.
	*
	* @param index the position of the base on the list
	* @param p the player whose stock is being counted
	* @return true if the player owns at least one; false otherwise
	*/
	public boolean ownsBase(int index, Player p) {
		return p.getInventory().quantityOfBase(BASE_NAMES[index]) > 0;
	}

	/**
	* Checks whether the player still owns a fruit and that it is not inside the cauldron.
	*
	* @param index the position of the fruit on the list
	* @param p the player whose stock is being counted
	* @param cauldron the cauldron being used for the brew
	* @return true if the fruit can still be added; false otherwise
	*/
	public boolean canAddFruit(int index, Player p, Cauldron cauldron) {
		if(cauldron != null && cauldron.hasIngredient(FRUIT_NAMES[index]))
			return false;

		return p.getInventory().quantityOfIngredient(FRUIT_NAMES[index]) > 0;
	}

	/**
	* Formats the current contents of the cauldron for display in the ingredients box.
	*
	* @param cauldron the cauldron being used for the brew
	* @return the base and fruits inside the cauldron; a reminder to add a base if it is still empty
	*/
	public String addedSummary(Cauldron cauldron) {
		if(cauldron == null)
			return "no cauldron";

		if(cauldron.getConcoctionBase() == null && cauldron.getIngredients().isEmpty())
			return "empty";

		StringBuilder inside = new StringBuilder();

		if(cauldron.getConcoctionBase() != null)
			inside.append(cauldron.getConcoctionBase().getName()).append("<br>");

		for(int i = 0; i < cauldron.getIngredients().size(); i++) {
			inside.append(cauldron.getIngredients().get(i).getName()).append("<br>");
		}

		return inside.toString();
	}

	/**
	* Formats the contents of the cauldron on a single line, for use in the confirmation message.
	*
	* @param cauldron the cauldron being used for the brew
	* @return the base and the fruits written on a single line
	*/
	public String cauldronContents(Cauldron cauldron) {
		StringBuilder inside = new StringBuilder();

		if(cauldron.getConcoctionBase() != null)
			inside.append(cauldron.getConcoctionBase().getName());

		for(int i = 0; i < cauldron.getIngredients().size(); i++) {
			if(i == 0)
				inside.append(" + ");
			else
				inside.append(", ");

			inside.append(cauldron.getIngredients().get(i).getName());
		}

		return inside.toString();
	}

	/**
	* Builds the message displayed to the player after a brew.
	*
	* @param status the result returned by the player's brewing
	* @param p the player, whose earnings and remaining crystals are included in the message
	* @return the message to be shown in the pop up
	*/
	public String brewMessage(int status, Player p) {
		String message;

		if(status == Player.BREW_SUCCESS) {
			message = "You brewed " + p.getLastConcoction() + "! It was packed into bottles and sold for " 
			+ p.getLastEarnings() + " crystals. You now have " + p.getCrystals() + " crystals."; 
			if(p.isNewDiscovery())
				message = message + " The recipe has been written into your spellbook!";
		} else if(status == Player.BREW_ALCHEMY_FAILED) {
			message = "The alchemy failed! The junk is stuck in the cauldron, so it cannot be used again until it is blessed.";
		} else if(status == Player.BREW_NOT_ENOUGH_INGREDIENTS) {
			message = "You do not have everything that recipe asks for.";
		} else if(status == Player.BREW_NO_CAULDRON) {
			message = "You have no cauldron clean enough to brew in.";
		} else {
			message = "Pour in a base and throw in at least one fruit first.";
		}

		return message;
	}

	/**
	* Returns the number of bases the player can choose from
	*
	* @return the number of concoction bases in the game
	*/
	public int baseCount() {
		return BASE_NAMES.length;
	}

	/**
	* Returns the number of fruits the placer can choose from.
	*
	* @return the number of fruit ingredients in the game
	*/
	public int fruitCount() {
		return FRUIT_NAMES.length;
	}

	/**
	* Returns the name of one of the bases.
	*
	* @param index the position of the base on the list
	* @return the name of that base
	*/
	public String baseName(int index) {
		return BASE_NAMES[index];
	}

	/**
	* Returns the name of a fruit.
	*
	* @param index the position of the fruit on the list
	* @return the name of that fruit
	*/
	public String fruitName(int index) {
		return FRUIT_NAMES[index];
	}

	//Setters and getters
	public boolean isCreative() {
		return creative;
	}

	public void setCreative(boolean creative) {
		this.creative = creative;
	}

	public ArrayList<Recipe> getAllRecipes() {
		return allRecipes;
	}

	public void setAllRecipes(ArrayList<Recipe> allRecipes) {
		this.allRecipes = allRecipes;
	}

	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}
}
