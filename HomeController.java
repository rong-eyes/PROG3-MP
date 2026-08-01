import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeController {
	private HomePanel view;
	private HomeModel model;
	private PotionProdigy main;
	private Player currentPlayer;
	
	public HomeController(HomePanel view, HomeModel model, PotionProdigy main, Player p) {
		this.view = view;
		this.model= model;
		this.main = main;
		this.currentPlayer = p;
		
		view.cabinetListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkInventory();
			}
		});
		
		view.pouchListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				visitMarket();
			}
		});
		
		view.cauldronListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cauldronClick();
			}
		});
		
		view.spellbookListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkSpellbook();
			}
		});
		
		view.clockListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				collectLogin();
			}
		});
		
		view.arrowListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				back();
			}
		});

		view.setCrystals(currentPlayer.getCrystals());
	}
	/**
	* This opens the Inventory scree, where the items the players own are listed.
	*/
	public void checkInventory() {
		this.main.InventoryScreen(getCurrentPlayer());
	}

	/**
	* This opens the market screen, where the player can buy and sell ingredients.
	*/
	public void visitMarket() {
		this.main.MarketScreen(getCurrentPlayer());
	}
	
	/**
	* Handles the player clicking the cauldron, which leads to either brewing or blessing.
	* <p>
	* Creative mode is only allowed when the player has more than one usable cauldron, because ruining the
	* last one would leave the player with no way to brew at all. Blessing is only offered when there is a
	* cauldron that actually needs it.
	* </p>
	*/
	public void cauldronClick() {
		int scenario = view.promptBrewOrBless();

		if(scenario == 1) { //Brew Concoction
			if(!currentPlayer.hasUsableCauldron()) {
				CustomPopUp.promptMessage(view, "Every cauldron you own is full of junk. Have one blessed before brewing again.");
				return;
			}

			boolean isRecipe = view.promptBrewMode();

			if(!isRecipe && !currentPlayer.canBrewCreative()) {
				CustomPopUp.promptMessage(view, "You only have one cauldron left that can be used. Experimenting could ruin it, "
						+ "so stick to a spellbook recipe for now.");
				return;
			}

			this.main.BrewScreen(getCurrentPlayer(), !isRecipe);
		}else if(scenario == 2) { //Bless Cauldron
			blessCauldron();
		}
	}
	
	/**
	* Pays for the blessing of a cauldron that was ruined by a failed experiment.
	*/
	public void blessCauldron() {
		if(!model.checkBrokenCauldrons(currentPlayer)) {
			CustomPopUp.promptMessage(view, "None of your cauldrons need blessing right now.");
			return;
		}

		boolean pushThrough = CustomPopUp.promptYesNo(view, "Have a cauldron blessed for "
				+ Cauldron.BLESSING_COST + " crystals?");

		if(pushThrough) {
			boolean blessed = model.blessCauldron(currentPlayer);
			view.setCrystals(currentPlayer.getCrystals());
			CustomPopUp.promptMessage(view, model.blessMessage(blessed, currentPlayer));
		}
	}

	/**
	* opens the spellbook screen, this lists the resicpes the player has already unlocked
	*/
	public void checkSpellbook() {
		this.main.SpellbookScreen(getCurrentPlayer());
	}

	/**
	* This claims the free ingredient when the login bonus is clicked
	*/
	public void collectLogin() {
		String pick = currentPlayer.claimLoginBonus();
		CustomPopUp.promptMessage(view, model.loginMessage(pick));
	}

	public void back() {
		//saves the game when pressing arrow
		boolean saved = SaveManager.saveGame(getCurrentPlayer());
		if(saved)
			CustomPopUp.promptMessage(view, "Your progress has been saved to \"" + currentPlayer.getPlayerName() + ".txt\".");
		else
			CustomPopUp.promptMessage(view, "Your progress could not be saved. Check if the game folder is write protected.");

		this.main.TitleScreen();
	}
	
	// Setters and getters
	public HomePanel getView() {
		return view;
	}

	public void setView(HomePanel view) {
		this.view = view;
	}

	public PotionProdigy getMain() {
		return main;
	}

	public void setMain(PotionProdigy main) {
		this.main = main;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public HomeModel getModel() {
		return model;
	}

	public void setModel(HomeModel model) {
		this.model = model;
	}
	
}
