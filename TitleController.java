import javax.swing.JPanel;

public class TitleController {
	private TitlePanel view;
	private TitleModel model;
	private JPanel mainPanel;
	private HomePanel home;
	
	public TitleController(TitlePanel view, TitleModel model, JPanel main, HomePanel home) {
		this.view = view;
		this.model = new TitleModel();
		this.mainPanel = main;
		this.home = home;
		
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
				home.setPlayer(p);
				switchScreen("HOME");
				break;
			case 1:
				boolean overwrite = view.promptOverWriteConfirm();
				if(overwrite) {
					model.playerProfile(name);

					Player p = model.playerProfile(name);
					home.setPlayer(p);
					switchScreen("HOME");
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
				home.setPlayer(model.getCurrentPlayer());
				switchScreen("HOME");
				break;
			case 1:
				boolean overwrite = view.promptNewGameConfirm();
				if(overwrite) {
					model.playerProfile(name);
	
					Player p = model.playerProfile(name);
					home.setPlayer(p);
					switchScreen("HOME");
				}
				break;
			case 2:
				break;
		}
	}
}
