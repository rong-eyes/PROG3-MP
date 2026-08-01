import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SpellbookPanel extends JPanel{
	final int screenWidth = 960;
	final int screenHeight = 720;
	private Image bgImage;
	private ImageIcon fieldImage;
	private JLabel arrow;
	private JLabel spellbook;
	private static final long serialVersionUID = 1L;

	private static final int ROWS_PER_PAGE = 10;
	private static final Color BOOK_INK = new Color(122, 40, 38);

	private JTextField searchBar;
	private JLabel[] recipeRows;
	private JLabel detail;
	private JLabel pageNumber;
	private JLabel leftArrow;
	private JLabel rightArrow;

	private JTextArea name;
	private JTextArea Base;
	private JTextArea list;
	private JTextArea price;
	private JLabel potion;

	public SpellbookPanel() {
		try {
			this.bgImage = ImageIO.read(getClass().getResource("/PotionProdigyAssets/UI Assets/Workshop Blurred_w Cauldron.png"));
		} catch (IOException e) {
			this.bgImage = null;
		}

		//SETTING SIZE
		this.setLayout(null);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true); //better game performance

		ImageIcon fieldTemp = new ImageIcon(getClass().getResource("/PotionProdigyAssets/UI Assets/Spellbook/Search Bar.png"));

		this.fieldImage = resizeIcon(fieldTemp);

		searchBar = new JTextField(20) {
			/**
			 * eclipse thing
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				if(fieldImage != null) {
					fieldImage.paintIcon(this, g, 0, 0);
				}
				super.paintComponent(g);
			}
		};

		searchBar.setOpaque(false);
		searchBar.setCaretColor(Color.red);

		Dimension fieldSize = new Dimension(fieldImage.getIconWidth(), fieldImage.getIconHeight());

		searchBar.setSize(fieldSize);
		searchBar.setBorder(BorderFactory.createEmptyBorder(5, 30, 10, 10));
		searchBar.setLocation(108, 175);
		searchBar.setFont(new Font("Times New Roman", Font.BOLD, 24));
		this.add(searchBar);

		//THE RECIPE LIST ON THE LEFT PAGE
		recipeRows = new JLabel[ROWS_PER_PAGE];
		for(int i = 0; i < ROWS_PER_PAGE; i++) {
			recipeRows[i] = new JLabel("");
			recipeRows[i].setBounds(118, 232 + (i * 34), 330, 30);
			recipeRows[i].setFont(new Font("Times New Roman", Font.PLAIN, 17));
			recipeRows[i].setForeground(BOOK_INK);
			this.add(recipeRows[i]);
		}

		pageNumber = new JLabel("", SwingConstants.CENTER);
		pageNumber.setBounds(200, 615, 120, 26);
		pageNumber.setFont(new Font("Times New Roman", Font.BOLD, 17));
		pageNumber.setForeground(BOOK_INK);
		this.add(pageNumber);

		
		//THE WRITE UP ON THE RIGHT PAGE
		detail = new JLabel("");
		detail.setBounds(515, 110, 350, 480);
		detail.setVerticalAlignment(SwingConstants.TOP);
		detail.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		detail.setForeground(BOOK_INK);
		this.add(detail);

		leftArrow = makeLabel("/PotionProdigyAssets/UI Assets/Spellbook/Arrow_Left.png", 115, 822);
		leftArrow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(leftArrow);

		rightArrow = makeLabel("/PotionProdigyAssets/UI Assets/Spellbook/Arrow_Right.png", 570, 822);
		rightArrow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(rightArrow);

		arrow = makeLabel("/PotionProdigyAssets/UI Assets/Back Arrow.png", 46, 63);
		arrow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(arrow);

		//the book image is added last so that the text appears on top of its pages
		spellbook = makeLabel("/PotionProdigyAssets/UI Assets/Spellbook/Spellbook Screen.png", 0, 10);
		this.add(spellbook);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(bgImage != null) {
			g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
		}
	}

	/**
	* Resizes an icon from the resolution it was drawn in to the resolution the screen uses.
	*
	* @param i the icon being resized
	* @return the resized icon
	*/
	private ImageIcon resizeIcon(ImageIcon i) {
		int newWidth = (int)(i.getIconWidth() * (960.0/1280.0));
		int newHeight= (int)(i.getIconHeight() * (960.0/1280.0));

		ImageIcon scaled = new ImageIcon(i.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));

		return scaled;
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
	* Returns how many recipes fit on one page of the book.
	*
	* @return the number of lines the left page can hold
	*/
	public int getRowsPerPage() {
		return ROWS_PER_PAGE;
	}

	/**
	* Displays one recipe on a line of the left page.
	*
	* @param row the position of the line being filled in
	* @param text the text to be displayed on the line
	* @param clickable true if the line holds a recipe the player can click
	*/
	public void setRow(int row, String text, boolean clickable) {
		recipeRows[row].setText(text);

		if(clickable)
			recipeRows[row].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		else
			recipeRows[row].setCursor(Cursor.getDefaultCursor());
	}
	
public void RecipeDisplay(Recipe r) {
		
		//cannot call removeAll(since it will reset the previosuly traversed spellbook pages
		if (name != null) 
			remove(name);
		if (Base != null) 
			remove(Base);
		if (list != null) 
			remove(list);
		if (potion != null) 
			remove(potion);
		if (price != null)
			remove(price);
		
		name = new JTextArea(r.getName());
		name.setFont(new Font("Times New Roman",Font.BOLD, 36));
		name.setForeground(new Color(105, 28, 32));
		name.setBounds(550, 100, 300, 250);
		name.setOpaque(false);
		name.setLineWrap(true);
		name.setWrapStyleWord(true);
		this.add(name);
		
		ImageIcon pTemp = new ImageIcon(getClass().getResource("/PotionProdigyAssets/Potions/" + r.getName() + ".png"));
		
		int originalWidth = pTemp.getImage().getWidth(null);
		int originalHeight = pTemp.getImage().getHeight(null);

		int targetWidth = 300;
		int targetHeight = 150;

		//fitting the image into a box without stretcheing it
		if ((double) originalWidth / originalHeight > (double) targetWidth / targetHeight) {
		    targetHeight = -1; 
		} else {
		    targetWidth = -1;
		}

		Image img = pTemp.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
		
		potion = new JLabel(new ImageIcon(img));
		potion.setBounds(525, 200, 300, 250);
		potion.setPreferredSize(new Dimension(300, 250));
		potion.setHorizontalAlignment(JLabel.CENTER);
		this.add(potion);
		
		Base = new JTextArea("Base: " + r.getConcoctionBase().getName());
		Base.setFont(new Font("Times New Roman", Font.PLAIN, 24));
		Base.setForeground(new Color(105, 28, 32));
		Base.setBounds(550, 450, 300, 50);
		Base.setOpaque(false);
		this.add(Base);
		
		StringBuilder ing = new StringBuilder("Ingredients: ");
		for(Ingredient i : r.getIngredients()) {
			ing.append(i.getName());
			ing.append("  ");
		}
		
		list = new JTextArea(ing.toString());
		list.setFont(new Font("Times New Roman", Font.PLAIN, 24));
		list.setForeground(new Color(105, 28, 32));
		list.setBounds(550, 500, 300, 100);
		list.setOpaque(false);
		list.setLineWrap(true);
		list.setWrapStyleWord(true);
		this.add(list);
		
		price = new JTextArea("Sell Price: " + r.getPrice() + " crystals");
		price.setFont(new Font("Times New Roman", Font.PLAIN, 24));
		price.setForeground(new Color(105, 28, 32));
		price.setBounds(550, 600, 300, 50);
		price.setOpaque(false);
		this.add(price);
		
		//brings to front
		setComponentZOrder(name, 0);
	    setComponentZOrder(potion, 0);
	    setComponentZOrder(Base, 0);
	    setComponentZOrder(list, 0);
	    setComponentZOrder(price, 0);
		
		revalidate();
		repaint();
		
	}


	/**
	* Displays the details of the recipe the player selected on the right page.
	*
	* @param text the details to be displayed
	*/
	public void setDetail(String text) {
		detail.setText("<html><div style='width:330px;'>" + text + "</div></html>");
	}

	/**
	* Displays which page of the book is currently open at the bottom of the left page.
	*
	* @param text the page indicator to be displayed
	*/
	public void setPageNumber(String text) {
		pageNumber.setText(text);
	}

	/**
	* Returns the text the player typed into the search bar.
	*
	* @return the contents of the search bar
	*/
	public String getSearchText() {
		return searchBar.getText();
	}

	/**
	* Assigns the listener that responds when the player presses enter in the search bar.
	*
	* @param l the listener to be attached to the search bar
	*/
	public void searchListener(ActionListener l) {
		searchBar.addActionListener(l);
	}

	/**
	* Assigns the listener that responds when the player clicks one of the recipe lines.
	*
	* @param row the position of the line being assigned a listener
	* @param l the listener to be attached to that line
	*/
	public void rowListener(int row, MouseAdapter l) {
		recipeRows[row].addMouseListener(l);
	}

	/**
	* Assigns the listener that responds when the player moves back a page.
	*
	* @param l the listener to be attached to the left arrow
	*/
	public void leftArrowListener(MouseAdapter l) {
		leftArrow.addMouseListener(l);
	}

	/**
	* Assigns the listener that responds when the player moves to the next page.
	*
	* @param l the listener to be attached to the right arrow
	*/
	public void rightArrowListener(MouseAdapter l) {
		rightArrow.addMouseListener(l);
	}

	/**
	* Assigns the listener that responds when the player clicks the back arrow.
	*
	* @param l the listener to be attached to the back arrow
	*/
	public void arrowListener(MouseAdapter l) {
		arrow.addMouseListener(l);
	}

	//PUT OTHER CODES ABOVE GETTER SETTERS
	public JLabel getSpellbook() {
		return spellbook;
	}

	public void setSpellbook(JLabel spellbook) {
		this.spellbook = spellbook;
	}
}
