import java.util.ArrayList;

public class SpellbookModel {
	private ArrayList<Recipe> unlockedRecipes;		//dont give full access to player
	
	public SpellbookModel() {
		unlockedRecipes = new ArrayList<>();
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
	
	public ArrayList<Recipe> getUnlockedRecipes() {
		return unlockedRecipes;
	}

	public void setUnlockedRecipes(ArrayList<Recipe> unlockedRecipes) {
		this.unlockedRecipes = unlockedRecipes;
	}
	
	public boolean prevPage(int totalRecipe, int currentPage) {
		return (currentPage > 0);
	}
	
	public boolean nextPage(int totalRecipe, int currentPage, int perPage) {
		int totalPages = (totalRecipe + perPage - 1) /perPage;
		return (currentPage < totalPages - 1);
	}
}
