import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MarketPanel extends JPanel { //the View for the market, where the 8 slots are displayed

	final int screenWidth = 960;
	final int screenHeight = 720;
	private Image bgImage;
	private static final long serialVersionUID = 1L;

	private static final int NUM_SLOTS = 8;
	private static final int SLOTS_PER_SHELF = 4;
	private static final Color SIGN_INK = new Color(94, 47, 20);
	private static final Color SHELF_INK = new Color(255, 250, 240);

	//the x position of the four slots on a shelf, and the y positions of the two shelves
	private static final int[] slotCenterX = {477, 616, 754, 893};
	private static final int[] shelfPlateY = {174, 361};
	private static final int[] shelfTopY = {131, 319};

	private JLabel[] slotNames;
	private JLabel[] slotPrices;
	private String[] slotArtShowing;	//prevents the same image from being loaded again on every refresh
	private JLabel crystals;
	private JLabel sellCrate;
	private JLabel arrow;

	public MarketPanel() {
		try {
			this.bgImage = ImageIO.read(getClass().getResource("/PotionProdigyAssets/UI Assets/Merchant/Merchant.png"));
		} catch (IOException e) {
			this.bgImage = null;
		}

		//SETTING SIZE
		this.setLayout(null);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true); //better game performance

		this.slotNames = new JLabel[NUM_SLOTS];
		this.slotPrices = new JLabel[NUM_SLOTS];
		this.slotArtShowing = new String[NUM_SLOTS];

		//THE ITEMS ON DISPLAY; each label holds the item image with its name and quantity underneath,
		//and also serves as the clickable part of the slot, so it is added ahead of the shelf images
		for(int i = 0; i < NUM_SLOTS; i++) {
			int shelf = i / SLOTS_PER_SHELF;
			int column = i % SLOTS_PER_SHELF;

			slotNames[i] = new JLabel("", SwingConstants.CENTER);
			slotNames[i].setBounds(slotCenterX[column] - 65, shelfTopY[shelf] - 100, 130, 96);
			slotNames[i].setVerticalAlignment(SwingConstants.BOTTOM);
			slotNames[i].setVerticalTextPosition(SwingConstants.BOTTOM);
			slotNames[i].setHorizontalTextPosition(SwingConstants.CENTER);
			slotNames[i].setFont(new Font("Times New Roman", Font.BOLD, 14));
			slotNames[i].setForeground(SHELF_INK);
			slotNames[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			this.add(slotNames[i]);
		}

		for(int i = 0; i < NUM_SLOTS; i++) {
			int shelf = i / SLOTS_PER_SHELF;
			int column = i % SLOTS_PER_SHELF;

			slotPrices[i] = new JLabel("", SwingConstants.CENTER);
			slotPrices[i].setBounds(slotCenterX[column] - 8, shelfPlateY[shelf] - 11, 36, 22);
			slotPrices[i].setFont(new Font("Times New Roman", Font.BOLD, 12));
			slotPrices[i].setForeground(SIGN_INK);
			this.add(slotPrices[i]);
		}

		//THE PLAYER'S CRYSTAL COUNT, DISPLAYED ON THE GOLD SIGN
		crystals = new JLabel("", SwingConstants.CENTER);
		crystals.setBounds(160, 42, 158, 26);
		crystals.setFont(new Font("Times New Roman", Font.BOLD, 18));
		crystals.setForeground(SIGN_INK);
		this.add(crystals);

		//the crate is clicked to sell, so its clickable area is added ahead of the crate image
		sellCrate = new JLabel();
		sellCrate.setBounds(547, 426, 302, 171);
		sellCrate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(sellCrate);

		arrow = makeLabel("/PotionProdigyAssets/UI Assets/Back Arrow.png", 35, 38);
		arrow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(arrow);

		//THE BACKGROUND IMAGES; everything below this point is for display only
		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Merchant/Selling Crate.png", 730, 568));
		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Merchant/Shelves.png", 528, 175));
		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Merchant/Shelves.png", 528, 425));
		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Merchant/Gem Counter.png", 165, 38));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(bgImage != null) {
			g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
		}
	}

	//HELPER FUNCTION BC ITS MAKING ME DIZZY
	/**
	* Creates a label that displays an image and places it on the screen.
	* The image and its position are resized from the 1280x960 resolution the artwork was drawn in.
	*
	* @param path the file location of the image
	* @param posX the x position of the image, in the original 1280x960 resolution
	* @param posY the y position of the image, in the original 1280x960 resolution
	* @return the label containing the image, already positioned
	*/
	private JLabel makeLabel(String path, int posX, int posY) {
		ImageIcon icon = new ImageIcon(getClass().getResource(path));
		int imageWidth = (int)(icon.getIconWidth() * (screenWidth / 1280.0));
		int imageHeight = (int)(icon.getIconHeight() * (screenHeight / 960.0));
		int imageX = (int)(posX * (screenWidth / 1280.0));
		int imageY = (int)(posY * (screenHeight / 960.0));
		Image img = icon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
		JLabel lbl = new JLabel(new ImageIcon(img));
		lbl.setBounds(imageX, imageY, imageWidth, imageHeight);
		return lbl;
	}

	/**
	* Displays an item on one of the market slots, with its image above its name and remaining quantity.
	*
	* @param slot the position of the slot being filled in
	* @param name the name of the item on display
	* @param stock the remaining quantity of the item, already formatted as text
	* @param price the cost of one piece of the item
	*/
	public void setSlot(int slot, String name, String stock, int price) {
		slotNames[slot].setText("<html><div style='text-align:center;'>" + name + "<br>" + stock + "</div></html>");
		slotPrices[slot].setText("" + price);
		slotNames[slot].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		if(!name.equals(slotArtShowing[slot])) {
			slotNames[slot].setIcon(shelfIcon(name));
			slotArtShowing[slot] = name;
		}
	}

	/**
	* Marks a market slot as sold out, which is how a slot is displayed after it has been bought
	* until the market refreshes.
	*
	* @param slot the position of the slot being emptied
	*/
	public void setSlotEmpty(int slot) {
		slotNames[slot].setText("<html><div style='text-align:center;'>SOLD<br>OUT</div></html>");
		slotPrices[slot].setText("");
		slotNames[slot].setCursor(Cursor.getDefaultCursor());
		slotNames[slot].setIcon(null);
		slotArtShowing[slot] = null;
	}

	/**
	* Loads the image of an item and resizes it to fit on a market shelf.
	*
	* @param name the name of the item on display
	* @return the resized image of the item; null if the item has no image
	*/
	private ImageIcon shelfIcon(String name) {
		String path = ItemArt.pathOf(name);

		if(path == null)
			return null;

		ImageIcon full = new ImageIcon(getClass().getResource(path));

		return new ImageIcon(full.getImage().getScaledInstance(64, 48, Image.SCALE_SMOOTH));
	}

	/**
	* Displays the number of crystals the player currently has.
	*
	* @param amount the crystals the player has
	*/
	public void setCrystals(int amount) {
		crystals.setText("" + amount);
	}

	/**
	* Assigns the listener that responds when the player clicks one of the market slots.
	*
	* @param slot the position of the slot being assigned a listener
	* @param l the listener to be attached to that slot
	*/
	public void slotListener(int slot, MouseAdapter l) {
		slotNames[slot].addMouseListener(l);
	}

	/**
	* Assigns the listener that responds when the player clicks the selling crate.
	*
	* @param l the listener to be attached to the crate
	*/
	public void sellCrateListener(MouseAdapter l) {
		sellCrate.addMouseListener(l);
	}

	/**
	* Assigns the listener that responds when the player clicks the back arrow.
	*
	* @param l the listener to be attached to the back arrow
	*/
	public void arrowListener(MouseAdapter l) {
		arrow.addMouseListener(l);
	}
}
