import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SpellbookController implements RecipeListener{
	private SpellbookModel model;
	private SpellbookPanel view;
	private PotionProdigy main;
	private Player currentPlayer;
	
	public SpellbookController(SpellbookModel m, SpellbookPanel v, PotionProdigy main, Player p) {
		this.model = m;
		this.view = v;
		this.main = main;
		this.currentPlayer = p;
			model.setUnlockedRecipes(currentPlayer.getSpellbook().getUnlockedRecipes());
	
		view.arrowListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				back();
			}
		});
		
		view.setRecipeListener(this); //works because it implements recipe listenter

		view.leftArrListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(model.prevPage(view.getSb().getUnlockedRecipes().size(), view.getCurrentPage())) {
					view.setCurrentPage(view.getCurrentPage() - 1);
					view.displayPage();
				}
						
			}
		});
		
		view.rightArrListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(model.nextPage(view.getSb().getUnlockedRecipes().size(), view.getCurrentPage(), view.getRECIPES_PER_PAGE())) {
					view.setCurrentPage(view.getCurrentPage() + 1);
					view.displayPage();
				}
						
			}
		});
		
		
		final int[] result = new int[1];
		view.textListener(e -> {
			try {
				result[0] = Integer.parseInt(view.getSearchBar().getText().trim());
				view.getSearchBar().setText("");
				
				Recipe temp = model.getRecipe(result[0]);
				if(temp != null) {
					view.RecipeDisplay(temp);
				}
					
			} catch (NumberFormatException ex) {
				view.getSearchBar().setText("");
			}
			
		});
	}
	
	@Override
	public void RecipeClicked(Recipe r) {
		view.RecipeDisplay(r);
	}

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
