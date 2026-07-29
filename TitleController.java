
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
				main.HomeScreen(p, true);
				break;
			case 1:
				boolean overwrite = view.promptOverWriteConfirm();
				if(overwrite) {
					Player overwrittenP = model.playerProfile(name);
					main.HomeScreen(overwrittenP, true);
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
				main.HomeScreen(p, false);
				break;
			case 1:
				boolean overwrite = view.promptNewGameConfirm();
				if(overwrite) {
					model.playerProfile(name);
	
					Player overwrittenP = model.playerProfile(name);
					main.HomeScreen(overwrittenP, true);
				}
				break;
			case 2:
				break;
		}
	}
}
