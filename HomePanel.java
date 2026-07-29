//import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HomePanel extends JPanel {
	final int screenWidth = 960; 
	final int screenHeight = 720;
	private Image bgImage; //replace with Home screen file
	private static final long serialVersionUID = 1L;
	
	private JLabel cauldron;	//might change to an optionpane as it needs a dropdown
	private JLabel cabinet;
	private JLabel pouch;
	private JLabel spellbook;
	private JLabel clock;
	private JLabel arrow;
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
	
	//HELPER FUNCTION BC ITS MAKING ME DIZZY
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
	
	public void cabinetListener(MouseAdapter l) {
		cabinet.addMouseListener(l);
	}
	
	public void cauldronListener(MouseAdapter l) {		//subject to change
		cauldron.addMouseListener(l);
	}
	
	public void spellbookListener(MouseAdapter l) {
		spellbook.addMouseListener(l);
	}
	
	public void clockListener(MouseAdapter l) {
		clock.addMouseListener(l);
	}
	
	public void pouchListener(MouseAdapter l) {
		pouch.addMouseListener(l);
	}
	
	public void arrowListener(MouseAdapter l) {
		arrow.addMouseListener(l);
	}
	
	
	public int promptBrewOrBless() {
		return CustomPopUp.promptBrewOrBless(this);
	}
	
	public boolean promptBrewMode() {
		return CustomPopUp.promptBrewMode(this);
	}
	
	public int WelcomeMessage(String p, boolean newGame) {
		return CustomPopUp.promptWelcome(this, p, newGame);
	}

	public Player getPlayer() {
		return p;
	}

	public void setPlayer(Player p) {
		this.p = p;
	}
}
