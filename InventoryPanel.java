import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class InventoryPanel extends JPanel {
	final int screenWidth = 960;
	final int screenHeight = 720;
	private Image bgImage;
	private static final long serialVersionUID = 1L;

	private static final Color INK = new Color(240, 230, 210);
	private static final Color HEADER_INK = new Color(226, 170, 92);

	private JLabel arrow;
	private JLabel cauldronCount;
	private JLabel crystalCount;
	private JPanel itemList;

	public InventoryPanel() {
		try {
			this.bgImage = ImageIO.read(getClass().getResource("/PotionProdigyAssets/UI Assets/Workshop Blurred.png"));
		} catch (IOException e) {
			this.bgImage = null;
		}

		//SETTING SIZE
		this.setLayout(null);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true); //better game performance

		//THE LIST OF EVERYTHING THE PLAYER OWNS; sits inside the dark box of the panel art
		itemList = new JPanel(new GridLayout(0, 2, 12, 0));
		itemList.setOpaque(false);

		JScrollPane scroller = new JScrollPane(itemList);
		scroller.setBounds(350, 58, 550, 530);
		scroller.setOpaque(false);
		scroller.getViewport().setOpaque(false);
		scroller.setBorder(null);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.getVerticalScrollBar().setUnitIncrement(16);
		scroller.getVerticalScrollBar().setOpaque(false);
		scroller.getVerticalScrollBar().setPreferredSize(new Dimension(9, 0)); //thin enough to pass for the one painted on the panel
		this.add(scroller);

		//COUNTERS ON THE LEFT SIDE
		cauldronCount = makeCounterText(70, 283);
		this.add(cauldronCount);

		crystalCount = makeCounterText(70, 533);
		this.add(crystalCount);

		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Inventory UI/Cauldrons Counter.png", 93, 160));
		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Inventory UI/Crystals Counter.png", 93, 493));

		arrow = makeLabel("/PotionProdigyAssets/UI Assets/Back Arrow.png", 60, 50);
		arrow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(arrow);

		//the screen frame is added last so that the components above appear inside its cut outs
		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Inventory UI/Inventory Panel.png", 0, 0));
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
	* Creates the label that displays a number inside one of the counters on the left of the screen.
	*
	* @param posX the x position of the label
	* @param posY the y position of the label
	* @return the label the count will be displayed on
	*/
	private JLabel makeCounterText(int posX, int posY) {
		JLabel lbl = new JLabel("", SwingConstants.CENTER);
		lbl.setBounds(posX, posY, 240, 26);
		lbl.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lbl.setForeground(INK);
		return lbl;
	}

	/**
	* Adds one line to the list of items the player owns.
	*
	* @param label the name displayed on the line
	* @param value the quantity displayed on the line
	* @param header true if the line is a section title instead of an actual item
	*/
	public void addRow(String label, String value, boolean header) {
		JLabel left = new JLabel(label);
		JLabel right = new JLabel(value, SwingConstants.RIGHT);

		if(header) {
			left.setFont(new Font("Times New Roman", Font.BOLD, 18));
			left.setForeground(HEADER_INK);
			right.setFont(new Font("Times New Roman", Font.BOLD, 18));
			right.setForeground(HEADER_INK);
		} else {
			left.setFont(new Font("Times New Roman", Font.PLAIN, 16));
			left.setForeground(INK);
			right.setFont(new Font("Times New Roman", Font.PLAIN, 16));
			right.setForeground(INK);
		}

		itemList.add(left);
		itemList.add(right);
	}

	/**
	* Removes every line from the list so that it can be filled in again with updated quantities.
	*/
	public void clearRows() {
		itemList.removeAll();
		itemList.revalidate();
		itemList.repaint();
	}

	/**
	* Displays the number of cauldrons the player owns on the cauldron counter.
	*
	* @param text the cauldron count to be displayed
	*/
	public void setCauldronCount(String text) {
		cauldronCount.setText(text);
	}

	/**
	* Displays the number of crystals the player has on the crystal counter.
	*
	* @param text the crystal count to be displayed
	*/
	public void setCrystalCount(String text) {
		crystalCount.setText(text);
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
