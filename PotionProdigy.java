import java.awt.CardLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PotionProdigy { //this should be the entry point of the projecct

	private Player currentPlayer;
	private ArrayList<Recipe> allRecipes;	//the list of valid recipes, kept here so every screen can use it
	private Market market;					//one market shared by the whole session
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

	/**
	* Displays the title screen, where the player starts a new game or loads an existing save.
	* <p>
	* Returning here means the player has left their save, so the market is discarded. The next game then
	* begins with a newly stocked market, which is the behaviour required when exiting and re-entering
	* the game.
	* </p>
	*/
	public void TitleScreen() {
		TitleModel titleModel = new TitleModel();
		TitlePanel titleScreen = new TitlePanel();
		new TitleController(titleScreen, titleModel, this);

		this.allRecipes = titleModel.getAllRecipes();
		this.market = null;

		showScreen(titleScreen, "TITLE");
	}

	/**
	* Displays the home screen, which serves as the main menu that every other screen is reached from.
	*
	* @param player the player whose save is currently being played
	*/
	public void HomeScreen(Player player) {
		this.setCurrentPlayer(player);

		if(this.market == null)
			this.market = new Market();

		HomePanel homeScreen = new HomePanel();
		HomeModel homeModel = new HomeModel();
		new HomeController(homeScreen, homeModel, this, player);

		showScreen(homeScreen, "HOME");
	}

	/**
	* Displays the spellbook screen, which lists all the recipes the player has unlocked.
	*
	* @param player the player whose spellbook is being opened
	*/
	public void SpellbookScreen(Player player) {
		SpellbookPanel sbScreen = new SpellbookPanel();
		SpellbookModel sbModel = new SpellbookModel();

		new SpellbookController(sbModel, sbScreen, this, player);

		showScreen(sbScreen, "SPELLBOOK");
	}

	/**
	* Displays the inventory screen, which lists the player's fruits, bases, cauldrons and crystals.
	*
	* @param player the player whose inventory is being checked
	*/
	public void InventoryScreen(Player player) {
		InventoryPanel invScreen = new InventoryPanel();
		InventoryModel invModel = new InventoryModel();

		new InventoryController(invModel, invScreen, this, player);

		showScreen(invScreen, "INVENTORY");
	}

	/**
	* Displays the market screen, which shows the items currently in stock.
	*
	* @param player the player visiting the market
	*/
	public void MarketScreen(Player player) {
		MarketPanel marketScreen = new MarketPanel();
		MarketModel marketModel = new MarketModel(getMarket());

		MarketController marketController = new MarketController(marketModel, marketScreen, this, player);

		showScreen(marketScreen, "MARKET");
		marketController.enterMarket(); //done after the screen is up so the merchant's greeting lands on top of it
	}

	/**
	* Displays the brewing screen in whichever mode the player selected.
	*
	* @param player the player doing the brewing
	* @param isCreative true for creative mode; false for recipe mode
	*/
	public void BrewScreen(Player player, boolean isCreative) {
		BrewPanel brewScreen = new BrewPanel(isCreative);
		BrewModel brewModel = new BrewModel(isCreative, getAllRecipes(), getMarket());

		new BrewController(brewModel, brewScreen, this, player);

		showScreen(brewScreen, "BREW");
	}

	/**
	* Replaces the screen currently being displayed with a new one.
	* <p>
	* The previous screen is removed instead of being kept behind the new one, so that the card layout does
	* not accumulate copies of the same screen as the player moves back and forth.
	* </p>
	*
	* @param screen the screen to be displayed
	* @param name the name the card layout stores the screen under
	*/
	public void showScreen(JPanel screen, String name) {
		main.removeAll();
		main.add(screen, name);
		screens.show(main, name);
		main.revalidate();
		main.repaint();
	}

	//PUT OTHER CODES ABOVE GETTER SETTERS
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

	/**
	 * @return the list of every valid recipe in the compendium
	 */
	public ArrayList<Recipe> getAllRecipes() {
		return allRecipes;
	}

	/**
	 * @return the market for this session
	 */
	public Market getMarket() {
		return market;
	}
}
