import java.util.ArrayList;

public class TitleModel { //the model for the startup.Derived from MCO1 Main and SaveManager
	private boolean isGameStarted;
	private static final String COMPENDIUM_PATH = "POTION COMPENDIUM.csv";
	private Player currentPlayer;
	private ArrayList<Recipe> allRecipes;
	
	//state determinants
	private final int success = 0;
	private final int overwrite = 1;	//save eexists
	private final int cancelled = 2; 	//also works as invalid name
	
	//RECIPE LOADER
	public TitleModel() {	//loads recipes upon instantiation
		this.allRecipes = RecipeLoader.loadRecipes(COMPENDIUM_PATH);
		this.setGameStarted(false);
	}

	public boolean isGameStarted() {
		return isGameStarted;
	}

	public void setGameStarted(boolean isGameStarted) {
		this.isGameStarted = isGameStarted;
	}
	
	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}
	
	public int createNewGame(String name) {
		if(name == null || name.trim().isEmpty())
			return cancelled;
		
		if(SaveManager.saveExists(name))
			//in console ver, it prompts an overwrite confirmation
			return overwrite;
		
		return success;
	}
	
	public int loadSave(String name) {
		if(name == null || name.trim().isEmpty())
			return cancelled;
		
		Player p = SaveManager.loadGame(name, allRecipes);
		
		if(p != null) {
			this.currentPlayer = p;
			this.setGameStarted(true);
			return success;
		}else { 		//if save file doesnt exist
			return overwrite;
		}
	}
	
	/**
	* Returns the list of valid recipes that was loaded when the game started.
	*
	* @return the list of every valid recipe in the game
	*/
	public ArrayList<Recipe> getAllRecipes() {
		return this.allRecipes;
	}

	public Player playerProfile(String name) {
		this.currentPlayer = new Player(name, allRecipes);
		this.setGameStarted(true);
		return this.currentPlayer;
	}
	
}
