
public class TitleController {
	private TitlePanel view;
	private TitleModel model;
	private PotionProdigy main;
	
	public TitleController(TitlePanel view, TitleModel model, PotionProdigy main) {
		this.view = view;
		this.model = model;
		this.main = main;
		
		view.addNewGameListener(e -> handleNewGame());
		view.addLoadGameListener(e -> handleLoadGame());
	}
	
	private void handleNewGame() {
		String name = view.promptPlayerName();
		
		int scenario = model.createNewGame(name);
		
		switch(scenario) {
			case 0:
				// SWITCH TO HOMESCREEN
				Player p = model.playerProfile(name);
				SaveManager.saveGame(p);
				while(this.promptWelcome(p, true) == 0) {
					
				}
				main.HomeScreen(p);
				break;
			case 1:
				boolean overwrite = view.promptOverWriteConfirm();
				if(overwrite) {
					Player overwrittenP = model.playerProfile(name);
					while(this.promptWelcome(overwrittenP, true) == 0) {
						
					}
					main.HomeScreen(overwrittenP);
				}
				break;
			case 2:
				break;
		}
		
	}
	
	private void handleLoadGame() {
		String name = view.promptPlayerName();
		
		int scenario = model.loadSave(name);
		
		switch(scenario) {
			case 0:
				Player p = model.getCurrentPlayer();
				
				while(this.promptWelcome(p, false) == 0) {
					
				}
				main.HomeScreen(p);
				
				break;
			case 1:
				boolean overwrite = view.promptNewGameConfirm();
				if(overwrite) {
					model.playerProfile(name);
					Player overwrittenP = model.playerProfile(name);
					while(this.promptWelcome(overwrittenP, true) == 0) {
						
					}
					main.HomeScreen(overwrittenP);
				}
				break;
			case 2:
				break;
		}
	}
	
	public int promptWelcome(Player p, boolean newGame) {
		return view.WelcomeMessage(p.getPlayerName(), newGame);
	}
}
