import java.util.ArrayList;

public class Spellbook {
	private ArrayList<Recipe> unlockedRecipes;

	/**
	* Constructs the spellbook by instantiating an {@code ArrayList<Recipe>} for the unlocked Recipes to be stored in
	*/
	public Spellbook() {
		unlockedRecipes = new ArrayList<>();
	}

	public ArrayList<Recipe> getUnlockedRecipes() {
		return unlockedRecipes;
	}

	public void setUnlockedRecipes(ArrayList<Recipe> unlockedRecipes) {
		this.unlockedRecipes = unlockedRecipes;
	}

	/**
	* Sorts the unlocked recipes by ID in ascending order
	*/
	public void sortRecipes() {
		int i, j;
		for (i = 1; i < unlockedRecipes.size(); i++) {
			Recipe tmp = unlockedRecipes.get(i);
			j = i;
			while ((j > 0) && (unlockedRecipes.get(j - 1).getConcoctionID() > tmp.getConcoctionID())) {
				unlockedRecipes.set(j, unlockedRecipes.get(j - 1));
				j--;
			}
			unlockedRecipes.set(j, tmp);
		}
	}

	/**
	* Gets a recipe from the unlocked recipe list (for brewing in recipe mode)
	*
	* @param id the unique identifier for the Recipe
	* @return the recipe with the corresponding ID; null if not found
	*/
	public Recipe getRecipe(int id) {
		int lo = 0;
		int hi = unlockedRecipes.size() - 1;

		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;

			if (id == unlockedRecipes.get(mid).getConcoctionID()) {
				return unlockedRecipes.get(mid);
			} else if (id > unlockedRecipes.get(mid).getConcoctionID()) {
				lo = mid + 1;
			} else {
				hi = mid - 1;
			}
		}

		return null; // recipe not found
	}

	/**
	* Checks whether a recipe has already been recorded in the spellbook.
	*
	* @param id the concoction ID being looked for
	* @return true if the recipe is already unlocked; false otherwise
	*/
	public boolean hasRecipe(int id) {
		return getRecipe(id) != null;
	}

	/**
	* Formats the ingredients of a recipe on a single line, which is how the spellbook displays them.
	*
	* @param recipe the recipe whose ingredients are being listed
	* @return the ingredient names separated by commas
	*/
	public static String ingredientsToString(Recipe recipe) {
		StringBuilder ingredients = new StringBuilder();

		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			if (i > 0) {
				ingredients.append(", ");
			}
			ingredients.append(recipe.getIngredients().get(i).getName());
		}

		return ingredients.toString();
	}

	/**
	* Adds a recipe to the spellbook (or list of unlocked recipes) whenever a player unlocks a new recipe
	*
	* @param recipe the recipe unlocked by the player
	*/
	public void addRecipe(Recipe recipe) {
		unlockedRecipes.add(recipe);
		sortRecipes();
	}
}
