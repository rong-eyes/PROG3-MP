import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MarketController { //the Controller for the market, the buying screen and the selling screen

	private MarketModel model;
	private MarketPanel view;
	private PotionProdigy main;
	private Player currentPlayer;

	private BuyPanel buyView;
	private SellPanel sellView;
	private ArrayList<InventoryItem> onOffer;	//matches the rows displayed on the selling screen

	public MarketController(MarketModel m, MarketPanel v, PotionProdigy main, Player p) {
		this.model = m;
		this.view = v;
		this.main = main;
		this.currentPlayer = p;

		for(int i = 0; i < model.getSlotCount(); i++) {
			final int slot = i;
			view.slotListener(i, new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					openBuyScreen(slot);
				}
			});
		}

		view.sellCrateListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				openSellScreen();
			}
		});

		view.arrowListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				back();
			}
		});
	}

	/**
	* Enters the market and displays its current stock.
	* <p>
	* The market is refreshed here if the player has brewed enough concoctions since the last visit, and a
	* message informs the player when that happens. This is called after the screen is already displayed so
	* that the message appears over the market instead of the previous screen.
	* </p>
	*/
	public void enterMarket() {
		boolean refreshed = model.visitMarket();

		refreshShelves();

		if(refreshed)
			CustomPopUp.promptMessage(view, "The merchant has laid out a fresh batch of wares!");
	}

	/**
	* Displays the current stock of the market on the shelves and updates the crystal counter.
	*/
	public void refreshShelves() {
		for(int i = 0; i < model.getSlotCount(); i++) {
			if(model.isSlotEmpty(i))
				view.setSlotEmpty(i);
			else
				view.setSlot(i, model.slotName(i), model.slotStock(i), model.slotPrice(i));
		}

		view.setCrystals(currentPlayer.getCrystals());
	}

	/**
	* Opens the buying screen for one of the market slots.
	* <p>
	* A slot is always bought as a whole, so the purchase is all or nothing. A slot that has already been
	* bought, or one the player cannot fully afford, is rejected here so that the player is not brought to a
	* screen where no purchase is possible.
	* </p>
	*
	* @param slot the position of the slot the player clicked
	*/
	public void openBuyScreen(int slot) {
		if(model.isSlotEmpty(slot)) {
			CustomPopUp.promptMessage(view, "That stall has already been bought out. Brew a few concoctions and the merchant will restock.");
			return;
		}

		int lot = model.lotSize(slot);

		if(!model.canAffordLot(slot, currentPlayer)) {
			CustomPopUp.promptMessage(view, "The merchant will only part with the whole lot. All " + lot + " "
					+ model.slotName(slot) + " costs " + model.costOf(slot, lot) + " crystals and you only have "
					+ currentPlayer.getCrystals() + ".");
			return;
		}

		model.setSelectedSlot(slot);

		buyView = new BuyPanel();
		buyView.setItem(model.slotName(slot), ItemArt.pathOf(model.slotName(slot)));
		buyView.setStock(lot);
		buyView.setTotal(model.costOf(slot, lot));

		buyView.confirmListener(e -> confirmBuy());
		buyView.cancelListener(e -> backToStall());

		main.showScreen(buyView, "BUY");
	}

	/**
	* Completes the purchase of the selected slot and informs the player of the result.
	* <p>
	* The entire stock is added to the inventory at once, which leaves the slot empty until the market
	* refreshes.
	* </p>
	*/
	public void confirmBuy() {
		int slot = model.getSelectedSlot();
		String name = model.slotName(slot);
		int amount = model.lotSize(slot);

		int status = model.buy(slot, amount, currentPlayer);

		backToStall();
		CustomPopUp.promptMessage(view, model.buyMessage(status, name, amount, currentPlayer));
	}

	/**
	* Opens the selling screen, listing every item the player currently has stock of.
	*/
	public void openSellScreen() {
		onOffer = model.sellables(currentPlayer);

		if(onOffer.isEmpty()) {
			CustomPopUp.promptMessage(view, "You have nothing the merchant would want right now.");
			return;
		}

		sellView = new SellPanel();
		for(int i = 0; i < onOffer.size(); i++) {
			InventoryItem item = onOffer.get(i);
			sellView.addRow(item.getName(), item.getQuantity(), item.getPrice());
		}

		sellView.tickListener(e -> updateSellTotal());
		sellView.amountListener(e -> updateSellTotal());
		sellView.confirmListener(e -> confirmSell());
		sellView.cancelListener(e -> backToStall());

		updateSellTotal();

		main.showScreen(sellView, "SELL");
	}

	/**
	* Computes the total value of the ticked items and displays it on the selling screen.
	*/
	public void updateSellTotal() {
		int total = 0;
		int ticked = 0;
		StringBuilder summary = new StringBuilder();

		for(int i = 0; i < sellView.getRowCount(); i++) {
			if(sellView.isTicked(i)) {
				total += sellView.getAmount(i) * onOffer.get(i).getPrice();
				ticked++;
				if(ticked <= 6) {
					summary.append(onOffer.get(i).getName()).append(" x").append(sellView.getAmount(i)).append("<br>");
				}
			}
		}

		if(ticked == 0)
			summary.append("Tick what you want<br>the merchant to take.");
		else if(ticked > 6)
			summary.append("...and ").append(ticked - 6).append(" more");

		sellView.setPreview(summary.toString());
		sellView.setTotal(total);
	}

	/**
	* Sells every item the player ticked and informs the player of the crystals earned.
	*/
	public void confirmSell() {
		int sold = 0;
		int earned = 0;

		for(int i = 0; i < sellView.getRowCount(); i++) {
			if(sellView.isTicked(i)) {
				InventoryItem item = onOffer.get(i);
				int amount = sellView.getAmount(i);
				int worth = amount * item.getPrice();

				if(model.sell(item, amount, currentPlayer) == Market.TRANSACTION_OK) {
					sold += amount;
					earned += worth;
				}
			}
		}

		backToStall();
		CustomPopUp.promptMessage(view, model.sellMessage(sold, earned, currentPlayer));
	}

	/**
	* Returns the player to the market screen with the shelves and crystal counter updated.
	*/
	public void backToStall() {
		refreshShelves();
		main.showScreen(view, "MARKET");
	}

	/**
	* Leaves the market and returns the player to the home screen.
	*/
	public void back() {
		this.main.HomeScreen(currentPlayer);
	}

	//PUT OTHER CODES ABOVE GETTER SETTERS
	public MarketModel getModel() {
		return model;
	}

	public void setModel(MarketModel model) {
		this.model = model;
	}

	public MarketPanel getView() {
		return view;
	}

	public void setView(MarketPanel view) {
		this.view = view;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
}
