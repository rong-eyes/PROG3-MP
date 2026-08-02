import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BrewController { 

	private BrewModel model;
	private BrewPanel view;
	private PotionProdigy main;
	private Player currentPlayer;
	private Cauldron cauldron;		

	public BrewController(BrewModel m, BrewPanel v, PotionProdigy main, Player p) {
		this.model = m;
		this.view = v;
		this.main = main;
		this.currentPlayer = p;
		this.cauldron = p.getInventory().getFreeCauldron();

		view.addListener(e -> addPicked());
		view.brewListener(e -> brew());

		view.arrowListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				back();
			}
		});

		refreshScreen();
	}

	/**
	* <p>
	* Recipe mode lists the recipes in the spellbook and disables the ones the player lacks ingredients for. 
	* Creative mode list the bases, then the fruits. The player can only add one base, and can add 1 to 3 fruits to the cauldron.
	* </p>
	*/
	public void refreshScreen() {
		view.clearChoices();

		if(model.isCreative()) {
			if(model.getStage() == BrewModel.STAGE_BASE) {
				view.setHeading("Pick a concoction base, then hit ADD");
				for(int i = 0; i < model.baseCount(); i++) {
					view.addChoice(model.baseLabel(i, currentPlayer), ItemArt.pathOf(model.baseName(i)),
							model.ownsBase(i, currentPlayer));
				}
			} else {
				view.setHeading("Throw in 1 to 3 fruits, then hit BREW");
				for(int i = 0; i < model.fruitCount(); i++) {
					view.addChoice(model.fruitLabel(i, currentPlayer), ItemArt.pathOf(model.fruitName(i)),
							model.canAddFruit(i, currentPlayer, cauldron));
				}
			}
		} else {
			view.setHeading("Pick a recipe from your spellbook, then hit BREW");
			ArrayList<Recipe> unlocked = model.unlockedRecipes(currentPlayer);
			for(int i = 0; i < unlocked.size(); i++) {
				view.addChoice(model.recipeLabel(unlocked.get(i)), ItemArt.potionPathOf(unlocked.get(i).getName()),
						currentPlayer.hasSufficientIngredients(unlocked.get(i)));
			}
		}

		view.refreshList();
		view.setAdded(model.addedSummary(cauldron));
		view.setCauldronCount(currentPlayer.getInventory().getUsableCauldrons());
		view.setGems(currentPlayer.getCrystals());
	}

	/**
	* The base is showed first, so that the player can choose what base they want,
	* then the list changes to fruits where the player can choose which fruits they want, 
	* then they can press which fruit they want to be put in the cauldron.
	* </p>
	*/
	public void addPicked() {
		int picked = view.getPickedChoice();

		if(picked == -1) {
			CustomPopUp.promptMessage(view, "Pick something off the list first.");
			return;
		}

		if(model.getStage() == BrewModel.STAGE_BASE) {
			if(cauldron.addBase(model.baseName(picked), currentPlayer.getInventory()))
				model.setStage(BrewModel.STAGE_FRUIT);
			else
				CustomPopUp.promptMessage(view, "You do not have any " + model.baseName(picked) + " left.");
		} else {
			if(cauldron.getIngredients().size() >= Cauldron.MAX_INGREDIENTS)
				CustomPopUp.promptMessage(view, "The cauldron is already full. Three fruits is all it can take.");
			else if(!cauldron.addIngredient(new Ingredient(model.fruitName(picked), 1), currentPlayer.getInventory()))
				CustomPopUp.promptMessage(view, "That fruit cannot go in. You are either out of it or it is already in the cauldron.");
		}

		refreshScreen();
	}

	/**
	* This starts the brewing process after the player clicked brew.
	* </p>
	*/
	public void brew() {
		if(model.isCreative())
			brewCreative();
		else
			brewFromRecipe();
	}

	/**
	* Brews the recipe the player selected from the spellbook.
	*/
	private void brewFromRecipe() {
		int picked = view.getPickedChoice();

		if(picked == -1) {
			CustomPopUp.promptMessage(view, "Pick a recipe from your spellbook first.");
			return;
		}

		Recipe chosen = model.unlockedRecipes(currentPlayer).get(picked);

		boolean pushThrough = CustomPopUp.promptYesNo(view, "Brew " + chosen.getName() + " for " + chosen.getPrice() + " crystals?");

		if(pushThrough) {
			int status = currentPlayer.brewRecipe(chosen, cauldron, model.getMarket());
			CustomPopUp.promptMessage(view, model.brewMessage(status, currentPlayer),
					model.brewImage(status, currentPlayer));
			main.HomeScreen(currentPlayer);
		}
	}

	/**
	* Brews the base and the fruits the player mixed in creative mode, then it determines if the brew is
	* successful or not.
	*/
	private void brewCreative() {
		if(cauldron == null || cauldron.getConcoctionBase() == null || cauldron.getIngredients().isEmpty()) {
			CustomPopUp.promptMessage(view, model.brewMessage(Player.BREW_INCOMPLETE, currentPlayer));
			return;
		}

		boolean pushThrough = CustomPopUp.promptYesNo(view, "Brew a concoction out of " + model.cauldronContents(cauldron) + "?");

		if(pushThrough) {
			int status = currentPlayer.brewCreative(cauldron, model.getAllRecipes(), model.getMarket());
			CustomPopUp.promptMessage(view, model.brewMessage(status, currentPlayer),
					model.brewImage(status, currentPlayer));
			main.HomeScreen(currentPlayer);
		}
	}

	/**
	* This allows the player to leave the brewing screen and return to the home screen without 
	* lessening their ingredients.
	* </p>
	*/
	public void back() {
		if(model.isCreative() && cauldron != null)
			cauldron.returnContents(currentPlayer.getInventory());

		this.main.HomeScreen(currentPlayer);
	}

	//Setters and getters
	public BrewModel getModel() {
		return model;
	}

	public void setModel(BrewModel model) {
		this.model = model;
	}

	public BrewPanel getView() {
		return view;
	}

	public void setView(BrewPanel view) {
		this.view = view;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Cauldron getCauldron() {
		return cauldron;
	}

	public void setCauldron(Cauldron cauldron) {
		this.cauldron = cauldron;
	}
}
