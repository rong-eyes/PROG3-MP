import java.util.ArrayList;

public class SpellbookModel {
	public static final int RECIPES_PER_PAGE = 10;

	private ArrayList<Recipe> unlockedRecipes;		//dont give full access to player
	private ArrayList<Recipe> shownRecipes;			//the recipes that matched the search bar
	private int currentPage;

	public SpellbookModel() {
		unlockedRecipes = new ArrayList<>();
		shownRecipes = new ArrayList<>();
		currentPage = 0;
	}

	/**
	* Copies the player's spellbook progress into the model so that the screen can display it.
	*
	* @param p the player whose spellbook is being opened
	*/
	public void loadFrom(Player p) {
		unlockedRecipes = p.getSpellbook().getUnlockedRecipes();
		shownRecipes = new ArrayList<>();
		currentPage = 0;

		for(int i = 0; i < unlockedRecipes.size(); i++) {
			shownRecipes.add(unlockedRecipes.get(i));
		}
	}

	/**
	* Filters the list down to the recipes that match what the player typed into the search bar.
	* <p>
	* Both the name and the concoction ID are checked, and upper or lower case makes no difference. An empty
	* search bar displays the entire spellbook again.
	* </p>
	*
	* @param query the text the player typed into the search bar
	*/
	public void search(String query) {
		shownRecipes = new ArrayList<>();
		currentPage = 0;

		String cleaned = "";
		if(query != null)
			cleaned = query.trim().toUpperCase();

		for(int i = 0; i < unlockedRecipes.size(); i++) {
			Recipe r = unlockedRecipes.get(i);
			String id = String.format("%03d", r.getConcoctionID());

			if(cleaned.isEmpty() || r.getName().toUpperCase().contains(cleaned) || id.contains(cleaned))
				shownRecipes.add(r);
		}
	}

	/**
	* Computes how many pages the recipes currently occupy.
	*
	* @return the number of pages; at least 1 even when the spellbook is empty
	*/
	public int pageCount() {
		if(shownRecipes.isEmpty())
			return 1;

		return (shownRecipes.size() + RECIPES_PER_PAGE - 1) / RECIPES_PER_PAGE;
	}

	/**
	* Moves to the next page of the spellbook, if there is one.
	*/
	public void nextPage() {
		if(currentPage < pageCount() - 1)
			currentPage++;
	}

	/**
	* Moves back to the previous page of the spellbook, if there is one.
	*/
	public void previousPage() {
		if(currentPage > 0)
			currentPage--;
	}

	/**
	* Returns the recipes that belong on the page currently being displayed.
	*
	* @return the recipes on the current page
	*/
	public ArrayList<Recipe> recipesOnPage() {
		ArrayList<Recipe> page = new ArrayList<>();
		int start = currentPage * RECIPES_PER_PAGE;
		int end = start + RECIPES_PER_PAGE;

		if(end > shownRecipes.size())
			end = shownRecipes.size();

		for(int i = start; i < end; i++) {
			page.add(shownRecipes.get(i));
		}

		return page;
	}

	/**
	* Formats one recipe for display in the list on the left page.
	*
	* @param r the recipe being formatted
	* @return the concoction ID and the name of the recipe
	*/
	public String recipeLine(Recipe r) {
		return String.format("[%03d]  %s", r.getConcoctionID(), r.getName());
	}

	/**
	* Formats the full details of a recipe for the right page of the book.
	*
	* @param r the recipe being formatted; null if the player has not selected one yet
	* @return the details to be displayed on the right page
	*/
	public String recipeDetail(Recipe r) {
		if(r == null)
			return "Pick a recipe from the list to read up on it.";

		return "<b>" + r.getName() + "</b><br><br>"
				+ "Concoction ID: " + String.format("%03d", r.getConcoctionID()) + "<br><br>"
				+ "Base: " + r.getConcoctionBase().getName() + "<br><br>"
				+ "Ingredients: " + Spellbook.ingredientsToString(r) + "<br><br>"
				+ "Sells for: " + r.getPrice() + " crystals";
	}

	/**
	* Formats the page indicator displayed at the bottom of the left page.
	*
	* @return the current page number over the total number of pages
	*/
	public String pageLabel() {
		return (currentPage + 1) + " / " + pageCount();
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

	//PUT OTHER CODES ABOVE GETTER SETTERS
	public ArrayList<Recipe> getUnlockedRecipes() {
		return unlockedRecipes;
	}

	public void setUnlockedRecipes(ArrayList<Recipe> unlockedRecipes) {
		this.unlockedRecipes = unlockedRecipes;
	}

	public ArrayList<Recipe> getShownRecipes() {
		return shownRecipes;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
