import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SpellbookController {
	private SpellbookModel model;
	private SpellbookPanel view;
	private PotionProdigy main;
	private Player currentPlayer;

	public SpellbookController(SpellbookModel m, SpellbookPanel v, PotionProdigy main, Player p) {
		this.model = m;
		this.view = v;
		this.main = main;
		this.currentPlayer = p;

		model.loadFrom(p);

		for(int i = 0; i < view.getRowsPerPage(); i++) {
			final int row = i;
			view.rowListener(i, new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					openRecipe(row);
				}
			});
		}

		view.leftArrowListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				previousPage();
			}
		});

		view.rightArrowListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				nextPage();
			}
		});

		view.searchListener(e -> runSearch());

		view.arrowListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				back();
			}
		});

		view.setDetail(model.recipeDetail(null));
		writePage();
	}

	/**
	* Displays the current page of recipes on the left page and clears any unused lines.
	*/
	public void writePage() {
		ArrayList<Recipe> page = model.recipesOnPage();

		for(int i = 0; i < view.getRowsPerPage(); i++) {
			if(i < page.size()) {
				Recipe shown = page.get(i);
				view.setRow(i, model.recipeLine(shown), ItemArt.potionPathOf(shown.getName()));
			} else {
				view.clearRow(i);
			}
		}

		view.setPageNumber(model.pageLabel());
	}

	/**
	* Displays the details of the recipe the player clicked on the right page of the book.
	*
	* @param row the position of the line the player clicked
	*/
	public void openRecipe(int row) {
		ArrayList<Recipe> page = model.recipesOnPage();

		if(row < page.size())
			view.setDetail(model.recipeDetail(page.get(row)));
	}

	/**
	* Filters the spellbook using the text the player typed into the search bar.
	*/
	public void runSearch() {
		model.search(view.getSearchText());
		view.setDetail(model.recipeDetail(null));
		writePage();
	}

	/**
	* Moves to the next page of the spellbook.
	*/
	public void nextPage() {
		model.nextPage();
		writePage();
	}

	/**
	* Moves back to the previous page of the spellbook.
	*/
	public void previousPage() {
		model.previousPage();
		writePage();
	}

	/**
	* Closes the spellbook and returns the player to the home screen.
	*/
	public void back() {
		this.main.HomeScreen(currentPlayer);
	}

	//GETTER SETTERS
	public SpellbookModel getModel() {
		return model;
	}

	public void setModel(SpellbookModel model) {
		this.model = model;
	}

	public SpellbookPanel getView() {
		return view;
	}

	public void setView(SpellbookPanel view) {
		this.view = view;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
}
