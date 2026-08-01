//import java.awt.Component;
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

public class HomePanel extends JPanel {
	final int screenWidth = 960; 
	final int screenHeight = 720;
	private Image bgImage; //replace with Home screen file
	private static final long serialVersionUID = 1L;
	
	private JLabel cauldron;	
	private JLabel cabinet;
	private JLabel pouch;
	private JLabel spellbook;
	private JLabel clock;
	private JLabel arrow;
	private JLabel crystals;
	private Player p;		    //just to pass info to the controller
	
	public HomePanel() {
		try {
			this.bgImage = ImageIO.read(getClass().getResource("/PotionProdigyAssets/UI Assets/Home Screen/BG.png"));
		} catch (IOException e) {
			this.bgImage = null;
		}
		
		//SETTING SIZE
		this.setLayout(null);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true); //better game performance
		
		//THE CRYSTAL COUNT; the crystals must remain visible on the main menu at all times
		crystals = new JLabel("", SwingConstants.CENTER);
		crystals.setBounds(145, 40, 150, 26);
		crystals.setFont(new Font("Times New Roman", Font.BOLD, 18));
		crystals.setForeground(new Color(94, 47, 20));
		this.add(crystals);

		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Merchant/Gem Counter.png", 150, 35));

		cabinet = makeLabel("/PotionProdigyAssets/UI Assets/Home Screen/Inventory.png", 700, 0);
		cabinet.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(cabinet);
		
		cauldron = makeLabel("/PotionProdigyAssets/UI Assets/Home Screen/Cauldron.png", 840, 554);
		cauldron.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(cauldron);
		
		spellbook = makeLabel("/PotionProdigyAssets/UI Assets/Home Screen/Spellbook.png", 10, 421);
		spellbook.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(spellbook);
		
		clock = makeLabel("/PotionProdigyAssets/UI Assets/Home Screen/Cuckoo Clock.png", 432, 17);
		clock.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(clock);
		
		arrow = makeLabel("/PotionProdigyAssets/UI Assets/Back Arrow.png", 20, 28);
		arrow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(arrow);
		
		JLabel fireplace = makeLabel("/PotionProdigyAssets/UI Assets/Home Screen/Fireplace.png", 712, 350);
		this.add(fireplace);
		
		//clickable pouch is behind the fireplace, hence its before the jlabel instantiaion
		pouch = makeLabel("/PotionProdigyAssets/UI Assets/Home Screen/Pouch.png", 421, 656);
		pouch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(pouch);
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(bgImage != null) {
			g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
	
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
	* Displays the number of crystals the player currently has.
	*
	* @param amount the crystals the player has
	*/
	public void setCrystals(int amount) {
		crystals.setText("" + amount);
	}

	/**
	* Assigns the listener that responds when the player clicks the cabinet.
	*
	* @param l the listener to be attached to it
	*/
	public void cabinetListener(MouseAdapter l) {
		cabinet.addMouseListener(l);
	}

	/**
	* Assigns the listener that responds when the player clicks the cauldron.
	*
	* @param l the listener to be attached to it
	*/
	public void cauldronListener(MouseAdapter l) {		//subject to change
		cauldron.addMouseListener(l);
	}

	/**
	* Assigns the listener that responds when the player clicks the spellbook.
	*
	* @param l the listener to be attached to it
	*/
	public void spellbookListener(MouseAdapter l) {
		spellbook.addMouseListener(l);
	}

	/**
	* Assigns the listener that responds when the player clicks the cuckoo clock.
	*
	* @param l the listener to be attached to it
	*/
	public void clockListener(MouseAdapter l) {
		clock.addMouseListener(l);
	}

	/**
	* Assigns the listener that responds when the player clicks the pouch.
	*
	* @param l the listener to be attached to it
	*/
	public void pouchListener(MouseAdapter l) {
		pouch.addMouseListener(l);
	}

	/**
	* Assigns the listener that responds when the player clicks the back arrow.
	*
	* @param l the listener to be attached to it
	*/
	public void arrowListener(MouseAdapter l) {
		arrow.addMouseListener(l);
	}

	/**
	* Asks the player whether to brew a concoction or bless a cauldron.
	*
	* @return 1 to brew a concoction; 2 to bless a cauldron; 0 if the pop up was closed
	*/
	public int promptBrewOrBless() {
		return CustomPopUp.promptBrewOrBless(this);
	}

	/**
	* Asks the player which brewing mode to enter.
	*
	* @return true for recipe mode; false for creative mode
	*/
	public boolean promptBrewMode() {
		return CustomPopUp.promptBrewMode(this);
	}

	public Player getPlayer() {
		return p;
	}

	public void setPlayer(Player p) {
		this.p = p;
	}
}
