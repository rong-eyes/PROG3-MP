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
		
		//clickable pouch is behind the fireplace, hence its before the jlabel instantiaion
		JLabel pouch = new JLabel(new ImageIcon(getClass().getResource("/PotionProdigyAssets/UI Assets/Home Screen/Pouch.png")));
		pouch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(pouch);
		
		JLabel fireplace = new JLabel(new ImageIcon(getClass().getResource("/PotionProdigyAssets/UI Assets/Home Screen/Fireplace.png")));
		this.add(fireplace);
		
		JLabel cabinet = new JLabel(new ImageIcon(getClass().getResource("/PotionProdigyAssets/UI Assets/Home Screen/Inventory.png")));
		cabinet.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(cabinet);
		
		JLabel cauldron = new JLabel(new ImageIcon(getClass().getResource("/PotionProdigyAssets/UI Assets/Home Screen/Cauldron.png")));
		cauldron.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(cauldron);
		
		JLabel spellbook = new JLabel(new ImageIcon(getClass().getResource("/PotionProdigyAssets/UI Assets/Home Screen/Spellbook.png")));
		spellbook.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(spellbook);
		
		JLabel clock = new JLabel(new ImageIcon(getClass().getResource("/PotionProdigyAssets/UI Assets/Home Screen/Cuckoo Clock.png")));
		clock.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(clock);
		
		JLabel arrow = new JLabel(new ImageIcon(getClass().getResource("/PotionProdigyAssets/UI Assets/Home Screen/Back Arrow.png")));
		arrow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(arrow);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(bgImage != null) {
			g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
		}
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
	

	public Player getPlayer() {
		return p;
	}

	public void setPlayer(Player p) {
		this.p = p;
	}
}
