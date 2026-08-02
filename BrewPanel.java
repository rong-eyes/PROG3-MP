import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class BrewPanel extends JPanel {

	final int screenWidth = 960;
	final int screenHeight = 720;
	private Image bgImage;
	private static final long serialVersionUID = 1L;

	private static final Color INK = new Color(240, 230, 210);
	private static final Color SIGN_INK = new Color(94, 47, 20);
	private static final Color GREYED = new Color(150, 130, 115);

	private JPanel choiceList;
	private ArrayList<JRadioButton> choices;
	private ButtonGroup onlyOne;
	private JLabel added;
	private JLabel cauldronCount;
	private JLabel gems;
	private JLabel heading;
	private JButton addButton;
	private JButton brewButton;
	private JLabel arrow;

	public BrewPanel(boolean isCreative) {
		try {
			this.bgImage = ImageIO.read(getClass().getResource("/PotionProdigyAssets/UI Assets/Workshop Blurred_w Cauldron.png"));
		} catch (IOException e) {
			this.bgImage = null;
		}

		//Set size
		this.setLayout(null);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true); 
		
		this.choices = new ArrayList<>();
		this.onlyOne = new ButtonGroup();

		// The recipes from the recipe mode, bases then fruits in creative mode
		choiceList = new JPanel(new GridLayout(0, 1, 0, 2));
		choiceList.setOpaque(false);

		//the list is pinned to the top, otherwise a handful of choices get stretched down the whole panel
		JPanel listHolder = new JPanel(new BorderLayout());
		listHolder.setOpaque(false);
		listHolder.add(choiceList, BorderLayout.NORTH);

		JScrollPane scroller = new JScrollPane(listHolder);
		scroller.setBounds(42, 148, 540, 415);
		scroller.setOpaque(false);
		scroller.getViewport().setOpaque(false);
		scroller.setBorder(null);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.getVerticalScrollBar().setUnitIncrement(16);
		scroller.getVerticalScrollBar().setOpaque(false);
		scroller.getVerticalScrollBar().setPreferredSize(new Dimension(9, 0)); //thin enough to pass for the one painted on the panel
		this.add(scroller);

		heading = new JLabel("", SwingConstants.CENTER);
		heading.setBounds(42, 120, 540, 26);
		heading.setFont(new Font("Times New Roman", Font.BOLD, 19));
		heading.setForeground(INK);
		this.add(heading);

		cauldronCount = new JLabel("", SwingConstants.CENTER);
		cauldronCount.setBounds(185, 53, 150, 30);
		cauldronCount.setFont(new Font("Times New Roman", Font.BOLD, 25));
		cauldronCount.setForeground(SIGN_INK);
		this.add(cauldronCount);

		gems = new JLabel("", SwingConstants.CENTER);
		gems.setBounds(413, 53, 146, 30);
		gems.setFont(new Font("Times New Roman", Font.BOLD, 25));
		gems.setForeground(SIGN_INK);
		this.add(gems);

		//what is inside the cauldron
		added = new JLabel("", SwingConstants.CENTER);
		added.setBounds(706, 312, 209, 62);
		added.setVerticalAlignment(SwingConstants.TOP);
		added.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		added.setForeground(INK);
		this.add(added);

		//lined up with the middle of the ingredients panel, which runs from x 21 to x 608
		addButton = makeButton("/PotionProdigyAssets/UI Assets/Brewing Screen/Add Ingredient.png", 232, 579, 164, 70);
		addButton.setVisible(isCreative); 
		this.add(addButton);

		brewButton = makeButton("/PotionProdigyAssets/UI Assets/Brewing Screen/BREW  Button.png", 543, 573, 369, 119);
		this.add(brewButton);

		arrow = makeLabel("/PotionProdigyAssets/UI Assets/Back Arrow.png", 45, 40);
		arrow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(arrow);

		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Brewing Screen/Ingredients Added.png", 935, 375));
		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Brewing Screen/Available Cauldrons.png", 197, 50));
		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Brewing Screen/Gems.png", 500, 50));
		this.add(makeLabel("/PotionProdigyAssets/UI Assets/Brewing Screen/Ingredients Panel.png", 28, 22));
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
	* @param width the width of the button on the screen
	* @param height the height of the button on the screen
	* @return the button containing the image, already positioned
	*/
	private JButton makeButton(String path, int posX, int posY, int width, int height) {
		ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		JButton button = new JButton(icon);

		button.setBounds(posX, posY, width, height);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		return button;
	}

	/**
	* Displays the instruction that tells the player what to select from the list below it.
	*
	* @param text the instruction to be displayed
	*/
	public void setHeading(String text) {
		heading.setText(text);
	}

	/**
	* Adds one choice to the list.
	*
	* @param text the text displayed for the choice
	* @param imagePath the file location of the drawing shown beside it; null if there is none
	* @param enabled false to disable the choice, which is how a recipe the player lacks the
	* 		  ingredients for is displayed
	*/
	public void addChoice(String text, String imagePath, boolean enabled) {
		final JRadioButton choice = new JRadioButton(text);
		choice.setOpaque(false);
		choice.setFocusPainted(false);
		choice.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		choice.setEnabled(enabled);
		choice.setBounds(70, 7, 452, 30);

		if(enabled)
			choice.setForeground(INK);
		else
			choice.setForeground(GREYED);

		JPanel row = new JPanel();
		row.setLayout(null);
		row.setOpaque(false);
		row.setPreferredSize(new Dimension(526, 44)); //nine fruits at this height still fit without scrolling

		JLabel picture = new JLabel();
		picture.setBounds(4, 2, 60, 40);
		if(imagePath != null)
			picture.setIcon(fitIcon(imagePath, 58, 38));
		row.add(picture);

		row.add(choice);

		//the whole row answers to a click, not just the small circle, so the picture can be clicked too
		if(enabled) {
			MouseAdapter pickRow = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					choice.setSelected(true);
				}
			};

			row.addMouseListener(pickRow);
			picture.addMouseListener(pickRow);
			row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

		choices.add(choice);
		onlyOne.add(choice);
		choiceList.add(row);
	}

	/**
	* Loads a drawing and shrinks it down to fit beside a choice on the list.
	* <p>
	* The ingredients and the concoctions were drawn at different shapes and sizes, so the width and the
	* height are reduced by the same amount to keep each one in proportion.
	* </p>
	*
	* @param path the file location of the drawing
	* @param maxWidth the widest the drawing is allowed to be
	* @param maxHeight the tallest the drawing is allowed to be
	* @return the drawing resized to fit
	*/
	private ImageIcon fitIcon(String path, int maxWidth, int maxHeight) {
		ImageIcon full = new ImageIcon(getClass().getResource(path));
		double widthLimit = maxWidth / (double) full.getIconWidth();
		double heightLimit = maxHeight / (double) full.getIconHeight();
		double scale = Math.min(widthLimit, heightLimit);

		return new ImageIcon(full.getImage().getScaledInstance((int)(full.getIconWidth() * scale),
				(int)(full.getIconHeight() * scale), Image.SCALE_SMOOTH));
	}

	/**
	* Lays out the list again after all of its choices have been added.
	*/
	public void refreshList() {
		choiceList.revalidate();
		choiceList.repaint();
	}

	/**
	* Removes every choice from the list so that it can be filled in again.
	*/
	public void clearChoices() {
		for(int i = 0; i < choices.size(); i++) {
			onlyOne.remove(choices.get(i));
		}

		choices.clear();
		choiceList.removeAll();
		choiceList.revalidate();
		choiceList.repaint();
	}

	/**
	* Determines which choice the player selected.
	*
	* @return the position of the selected choice; -1 if nothing has been selected yet
	*/
	public int getPickedChoice() {
		for(int i = 0; i < choices.size(); i++) {
			if(choices.get(i).isSelected())
				return i;
		}

		return -1;
	}

	/**
	* Displays the current contents of the cauldron inside the ingredients box. 
	* Makes sure that it's in the center.
	*
	* @param text the contents of the cauldron
	*/
	public void setAdded(String text) {
		added.setText("<html><div style='text-align:center;'>" + text + "</div></html>");
	}

	/**
	* Displays the number of usable cauldrons.
	*
	* @param count the number of usable cauldrons
	*/
	public void setCauldronCount(int count) {
		cauldronCount.setText("" + count);
	}

	/**
	* Displays the number of crystals the player currently has.
	*
	* @param amount the crystals the player has
	*/
	public void setGems(int amount) {
		gems.setText("" + amount);
	}

	/**
	* Assigns the listener that responds when the player presses the add button.
	*
	* @param l the listener to be attached to the add button
	*/
	public void addListener(ActionListener l) {
		addButton.addActionListener(l);
	}

	/**
	* Assigns the listener that responds when the player presses the brew button.
	*
	* @param l the listener to be attached to the brew button
	*/
	public void brewListener(ActionListener l) {
		brewButton.addActionListener(l);
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
