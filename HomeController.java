import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeController {
	private HomePanel view;
	private HomeModel model;
	private PotionProdigy main;
	private Player currentPlayer;
	private boolean newGame;
	
	public HomeController(HomePanel view, HomeModel model, PotionProdigy main, Player p, boolean newGame) {
		this.view = view;
		this.model= model;
		this.main = main;
		this.currentPlayer = p;
		this.newGame = newGame;
		
		
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
		
		while(this.promptWelcome() == 0) {
			
		}
	}
	
	public void checkInventory() {
		//insert code
	}
	
	public void visitMarket() {
		//insert code
	}
	
	public void cauldronClick() {
		int scenario = view.promptBrewOrBless();
		
		if(scenario == 1) { //Brew Concoction
			boolean isRecipe = view.promptBrewMode();
			if(isRecipe) {
				System.out.println("Recipe");
			}else
				System.out.println("Creative");
			
		}else if(scenario == 2) { //Bless Cauldron
			model.checkBrokenCauldrons(currentPlayer);
		}
	}
	
	public void checkSpellbook() {
		
	}
	
	public void collectLogin() {
		//insert code
	}

	public void back() {
		//saves the game when pressing arrow
		SaveManager.saveGame(getCurrentPlayer());
		this.main.TitleScreen();
	}
	
	public int promptWelcome() {
		return view.WelcomeMessage(this.currentPlayer.getPlayerName(), this.newGame);
	}
	
	//PUT OTHER CODES ABOVE GETTER SETTERS
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
