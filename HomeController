import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeController {
	private HomePanel view;
	private HomeModel model;
	private PotionProdigy main;
	private Player currentPlayer;
	
	public HomeController(HomePanel view, HomeModel model, PotionProdigy main, Player p) {
		this.view = view;
		this.model = model;
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
	}
	
	public void checkInventory() {
		//insert code
	}
	
	public void visitMarket() {
		//insert code
	}
	
	public void cauldronClick() {
		//insert code
	}
	
	public void checkSpellbook() {
		
	}
	
	public void collectLogin() {
		//insert code
	}

	
	
	//PUT OTHER CODES ABOVE GETTER SETTERS
	public HomePanel getView() {
		return view;
	}

	public void setView(HomePanel view) {
		this.view = view;
	}

	public HomeModel getModel() {
		return model;
	}

	public void setModel(HomeModel model) {
		this.model = model;
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
	
}
