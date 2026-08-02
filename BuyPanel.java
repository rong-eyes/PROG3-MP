import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BuyPanel extends JPanel { //the View for buying an item from a market slot

	final int screenWidth = 960;
	final int screenHeight = 720;
	private Image bgImage;
	private static final long serialVersionUID = 1L;

	private static final Color INK = new Color(240, 230, 210);
	private static final Color SIGN_INK = new Color(94, 47, 20);

	private JLabel itemDisplay;
	private JLabel total;
	private JLabel stock;
	private JButton confirm;
	private JButton cancel;

	public BuyPanel() {
		try {
			this.bgImage = ImageIO.read(getClass().getResource("/PotionProdigyAssets/UI Assets/Merchant Blurred.png"));
		} catch (IOException e) {
			this.bgImage = null;
		}

		//SETTING SIZE
		this.setLayout(null);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true); //better game performance

		//THE ITEM BEING BOUGHT; the image is displayed above the name inside the dark box
		itemDisplay = new JLabel("", SwingConstants.CENTER);
		itemDisplay.setBounds(236, 255, 221, 210);
		itemDisplay.setVerticalTextPosition(SwingConstants.BOTTOM);
		itemDisplay.setHorizontalTextPosition(SwingConstants.CENTER);
		itemDisplay.setFont(new Font("Times New Roman", Font.BOLD, 18));
		itemDisplay.setForeground(INK);
		this.add(itemDisplay);

		//A MARKET SLOT IS ALWAYS BOUGHT AS A WHOLE, SO THIS ONLY STATES HOW MANY PIECES THAT IS
		stock = new JLabel("", SwingConstants.CENTER);
		stock.setBounds(575, 398, 136, 50);
		stock.setFont(new Font("Times New Roman", Font.BOLD, 16));
		stock.setForeground(INK);
		this.add(stock);

		total = new JLabel("0", SwingConstants.CENTER);
		total.setBounds(588, 337, 106, 32);
		total.setFont(new Font("Times New Roman", Font.BOLD, 20));
		total.setForeground(SIGN_INK);
		this.add(total);

		confirm = makeButton("/PotionProdigyAssets/UI Assets/Confirm.png", 324, 525);
		this.add(confirm);

		cancel = makeButton("/PotionProdigyAssets/UI Assets/Cancel.png", 499, 525);
		this.add(cancel);

		//the screen frame is added last so that the components above appear inside its cut outs
		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Merchant_Buy UI/Merchant_Buy Screen.png", 0, 0));
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
	* Creates a button that displays an image and places it on the screen.
	* The default button decorations are removed so that only the image is visible.
	*
	* @param path the file location of the button image
	* @param posX the x position of the button on the screen
	* @param posY the y position of the button on the screen
	* @return the button containing the image, already positioned
	*/
	private JButton makeButton(String path, int posX, int posY) {
		ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(136, 58, Image.SCALE_SMOOTH));
		JButton button = new JButton(icon);

		button.setBounds(posX, posY, 136, 58);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBorderPainted(false);

		return button;
	}

	/**
	* Displays the item the player selected, together with its image if one exists.
	*
	* @param name the name of the item
	* @param imagePath the file location of the item's image; null if the item has no image
	*/
	public void setItem(String name, String imagePath) {
		itemDisplay.setText(name);

		if(imagePath == null) {
			itemDisplay.setIcon(null);
		} else {
			ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
			Image img = icon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
			itemDisplay.setIcon(new ImageIcon(img));
		}
	}

	/**
	* Displays how many pieces the purchase covers, since a market slot is always bought as a whole.
	*
	* @param amount the quantity of the item in the market slot
	*/
	public void setStock(int amount) {
		if(amount == 1)
			stock.setText("<html><div style='text-align:center;'>Buying the<br>only one left</div></html>");
		else
			stock.setText("<html><div style='text-align:center;'>Buying all " + amount + "<br>of them</div></html>");
	}

	/**
	* Displays the total number of crystals the purchase will cost.
	*
	* @param amount the total cost of the purchase
	*/
	public void setTotal(int amount) {
		total.setText("" + amount);
	}

	/**
	* Assigns the listener that responds when the player confirms the purchase.
	*
	* @param l the listener to be attached to the confirm button
	*/
	public void confirmListener(ActionListener l) {
		confirm.addActionListener(l);
	}

	/**
	* Assigns the listener that responds when the player cancels the purchase.
	*
	* @param l the listener to be attached to the cancel button
	*/
	public void cancelListener(ActionListener l) {
		cancel.addActionListener(l);
	}
}
