//import java.util.ArrayList;
//import java.util.Scanner;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PotionProdigy { //this should be the entry point of the projecct
	
	private Player currentPlayer;
	JPanel main;
	private CardLayout screens;

	public PotionProdigy() {
		
		JFrame PotionProdigy = new JFrame();
		PotionProdigy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//resizingPotionProdigy.setSize(1280,960); //replace by frame.pack()
		PotionProdigy.setResizable(false); //default 4:3 screen ratio; OMORI aspect ratio
		PotionProdigy.setTitle("Potion Prodigy");
		
		this.main = new JPanel();
		this.screens = new CardLayout();	//for easyscreen switching
		
		main.setLayout(screens);
		PotionProdigy.add(main);
		
		TitleScreen();
		
		PotionProdigy.pack();
		PotionProdigy.setLocationRelativeTo(null); //opens the application at the center of the user's screen
		PotionProdigy.setVisible(true);//should be at the end
	}
	
	public void TitleScreen() {
		TitleModel titleModel = new TitleModel();
		TitlePanel titleScreen = new TitlePanel();
		new TitleController(titleScreen, titleModel, this);
		
		main.add(titleScreen, "TITLE");
		screens.show(main, "TITLE");
	}
	
	public void HomeScreen(Player player) {
		this.setCurrentPlayer(player);

		HomePanel homeScreen = new HomePanel();
		HomeModel homeModel = new HomeModel();
		new HomeController(homeScreen, homeModel, this, player);
		
		main.add(homeScreen, "HOME");
		screens.show(main, "HOME");
	}
	
	public void SpellbookScreen(Player player) {
		//add screens here
		SpellbookPanel sbScreen = new SpellbookPanel(player.getSpellbook());
		SpellbookModel sbModel = new SpellbookModel();
		
		new SpellbookController(sbModel, sbScreen, this, player);
		
		main.add(sbScreen, "SPELLBOOK");
		screens.show(main, "SPELLBOOK");
	}
	
	public void InventoryScreen(/* Player player*/) {
		//add screens here
	}
	
	public void MarketScreen(/* Player player*/) {
		//add screens here
	}
	
	public void BrewScreen(/* Player player*/) {
		//add screens here
	}

	/**
	 * @return the currentPlayer
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * @param currentPlayer the currentPlayer to set
	 */
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	
}
