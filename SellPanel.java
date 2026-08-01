import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;

public class SellPanel extends JPanel { //the View for selling items to the market

	final int screenWidth = 960;
	final int screenHeight = 720;
	private Image bgImage;
	private static final long serialVersionUID = 1L;

	private static final Color INK = new Color(240, 230, 210);
	private static final Color SIGN_INK = new Color(94, 47, 20);

	private JPanel itemList;
	private ArrayList<JCheckBox> tickBoxes;
	private ArrayList<JSpinner> amounts;
	private JLabel preview;
	private JLabel total;
	private JButton confirm;
	private JButton cancel;

	public SellPanel() {
		try {
			this.bgImage = ImageIO.read(getClass().getResource("/PotionProdigyAssets/UI Assets/Merchant Blurred.png"));
		} catch (IOException e) {
			this.bgImage = null;
		}

		//SETTING SIZE
		this.setLayout(null);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true);

		this.tickBoxes = new ArrayList<>();
		this.amounts = new ArrayList<>();

		//THE LIST OF SELLABLE ITEMS, EACH ONE WITH ITS OWN CHECKBOX
		itemList = new JPanel(new GridLayout(0, 1, 0, 4));
		itemList.setOpaque(false);

		JScrollPane scroller = new JScrollPane(itemList);
		scroller.setBounds(375, 72, 515, 570);
		scroller.setOpaque(false);
		scroller.getViewport().setOpaque(false);
		scroller.setBorder(null);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.getVerticalScrollBar().setUnitIncrement(16);
		scroller.getVerticalScrollBar().setOpaque(false);
		scroller.getVerticalScrollBar().setPreferredSize(new Dimension(9, 0));
		this.add(scroller);

		preview = new JLabel("", SwingConstants.CENTER);
		preview.setBounds(108, 198, 199, 188);
		preview.setVerticalAlignment(SwingConstants.CENTER);
		preview.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		preview.setForeground(INK);
		this.add(preview);

		total = new JLabel("0", SwingConstants.CENTER);
		total.setBounds(191, 502, 71, 28);
		total.setFont(new Font("Times New Roman", Font.BOLD, 16));
		total.setForeground(SIGN_INK);
		this.add(total);

		confirm = makeButton("/PotionProdigyAssets/UI Assets/Confirm.png", 75, 592);
		this.add(confirm);

		cancel = makeButton("/PotionProdigyAssets/UI Assets/Cancel.png", 222, 592);
		this.add(cancel);

		//the screen frame is added last so that the components above appear inside its cut outs
		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Merchant_Sell UI/Merchant_Sell UI.png", 0, 0));
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
	* Adds one item to the list of items the player is able to sell.
	* <p>
	* Each row has its own checkbox and its own quantity spinner. This allows the player to sell several
	* different items in a single transaction while still choosing the quantity for each one.
	* </p>
	*
	* @param name the name of the item
	* @param owned the quantity of the item the player currently has
	* @param price the selling price of one piece of the item
	*/
	public void addRow(String name, int owned, int price) {
		JPanel row = new JPanel();
		row.setLayout(null);
		row.setOpaque(false);
		row.setPreferredSize(new Dimension(490, 30));

		JCheckBox tick = new JCheckBox(name + "  (x" + owned + ", " + price + " each)");
		tick.setBounds(0, 0, 350, 28);
		tick.setOpaque(false);
		tick.setFocusPainted(false);
		tick.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		tick.setForeground(INK);
		row.add(tick);

		JSpinner amount = new JSpinner(new SpinnerNumberModel(1, 1, owned, 1));
		amount.setBounds(370, 1, 70, 26);
		amount.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		row.add(amount);

		tickBoxes.add(tick);
		amounts.add(amount);
		itemList.add(row);
	}

	/**
	* Removes every row from the list so that it can be filled in again with updated quantities.
	*/
	public void clearRows() {
		tickBoxes.clear();
		amounts.clear();
		itemList.removeAll();
		itemList.revalidate();
		itemList.repaint();
	}

	/**
	* Returns the number of rows currently displayed on the list.
	*
	* @return the number of items the player is able to sell
	*/
	public int getRowCount() {
		return tickBoxes.size();
	}

	/**
	* Checks whether the checkbox of a row has been ticked by the player.
	*
	* @param row the position of the row being checked
	* @return true if the checkbox is ticked; false otherwise
	*/
	public boolean isTicked(int row) {
		return tickBoxes.get(row).isSelected();
	}

	/**
	* Returns the quantity the player selected on the spinner of a row.
	*
	* @param row the position of the row being read
	* @return the quantity displayed on that row's spinner
	*/
	public int getAmount(int row) {
		return (Integer) amounts.get(row).getValue();
	}

	/**
	* Displays a summary of the items the player has ticked inside the preview box.
	*
	* @param text the summary to be displayed
	*/
	public void setPreview(String text) {
		preview.setText("<html><div style='text-align:center;'>" + text + "</div></html>");
	}

	/**
	* Displays the total number of crystals the sale will earn.
	*
	* @param amount the total the player will receive
	*/
	public void setTotal(int amount) {
		total.setText("" + amount);
	}

	/**
	* Assigns the listener that responds when the player ticks or unticks any checkbox.
	*
	* @param l the listener to be attached to every checkbox
	*/
	public void tickListener(ActionListener l) {
		for(int i = 0; i < tickBoxes.size(); i++) {
			tickBoxes.get(i).addActionListener(l);
		}
	}

	/**
	* Assigns the listener that responds when the player changes the quantity on any spinner.
	*
	* @param l the listener to be attached to every spinner
	*/
	public void amountListener(ChangeListener l) {
		for(int i = 0; i < amounts.size(); i++) {
			amounts.get(i).addChangeListener(l);
		}
	}

	/**
	* Assigns the listener that responds when the player confirms the sale.
	*
	* @param l the listener to be attached to the confirm button
	*/
	public void confirmListener(ActionListener l) {
		confirm.addActionListener(l);
	}

	/**
	* Assigns the listener that responds when the player cancels the sale.
	*
	* @param l the listener to be attached to the cancel button
	*/
	public void cancelListener(ActionListener l) {
		cancel.addActionListener(l);
	}
}
