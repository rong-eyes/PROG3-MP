
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SpellbookPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	final int screenWidth = 960; 
	final int screenHeight = 720;
	private Image bgImage; 
	private ImageIcon fieldImage;
	private JLabel arrow;
	private JLabel spellbook; 
	private JLabel leftArr;
	private JLabel rightArr;
	private JTextField searchBar;
	
	//CONTENTS
	private Spellbook sb;
	private JLabel[] recipeLabels;
	private int currentPage = 0;
	private final int RECIPES_PER_PAGE = 10;
	private RecipeListener l;
	
	//RIGHT PAGE
	private JTextArea name;
	private JTextArea Base;
	private JTextArea list;
	private JLabel potion;
	
	public SpellbookPanel(Spellbook s) {
		try {
			this.bgImage = ImageIO.read(getClass().getResource("/PotionProdigyAssets/UI Assets/Workshop Blurred_w Cauldron.png"));
		} catch (IOException e) {
			this.bgImage = null;
		}
		
		setRecipeLabels(new JLabel[RECIPES_PER_PAGE]); //10 items per page
		setSb(s);
		
		//SETTING SIZE
		this.setLayout(null);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true); //better game performance
		
		arrow = makeLabel("/PotionProdigyAssets/UI Assets/Back Arrow.png", 46, 63);
		arrow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(arrow);
		
		ImageIcon fieldTemp = new ImageIcon(getClass().getResource("/PotionProdigyAssets/UI Assets/Spellbook/Search Bar.png"));
		
		this.fieldImage = resizeIcon(fieldTemp);
		
		searchBar = new JTextField(20) {
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
		
		spellbook = (makeLabel("/PotionProdigyAssets/UI Assets/Spellbook/Spellbook Screen.png", 0, 10));
		
		
		leftArr = makeLabel("/PotionProdigyAssets/UI Assets/Spellbook/Arrow_Left.png", 111, 811);
		leftArr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		rightArr = makeLabel("/PotionProdigyAssets/UI Assets/Spellbook/Arrow_Right.png", 566, 811);
		rightArr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		this.add(leftArr);
		this.add(rightArr);
		
		this.displayPage();
		
	}
	
	public int getRECIPES_PER_PAGE() {
		return RECIPES_PER_PAGE;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(bgImage != null) {
			g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
		}
		 if (spellbook != null) {
		        g.drawImage(((ImageIcon) spellbook.getIcon()).getImage(),
		                    spellbook.getX(),
		                    spellbook.getY(),
		                    spellbook.getWidth(),
		                    spellbook.getHeight(),
		                    this);
		    }
	}
	
	private ImageIcon resizeIcon(ImageIcon i) {
		int newWidth = (int)(i.getIconWidth() * (960.0/1280.0));
		int newHeight= (int)(i.getIconHeight() * (960.0/1280.0));
		
		ImageIcon scaled = new ImageIcon(i.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
		
		return scaled;
	}
	
	
	public void displayPage() {
		
		removeAll();
		add(arrow);
		add(searchBar);
		add(leftArr);
		add(rightArr);
		
		int start = this.currentPage * getRECIPES_PER_PAGE();
		int end = Math.min(start + getRECIPES_PER_PAGE(), this.sb.getUnlockedRecipes().size());
		
		int posY = 220;
		
		for(int i = start; i < end; i++) {
			int currentIndex = i; 										//weird java glitch fix saying: 'i' needs to be effectively final
			Recipe r = this.sb.getUnlockedRecipes().get(currentIndex);
			String Recipe = r.getName();
			JLabel name = new JLabel(r.getConcoctionID() + " " + Recipe);
			name.setBounds(150, posY, 300, 30);
			name.setFont(new Font("Times New Roman", Font.ITALIC + Font.BOLD, 20));
			name.setForeground(new Color(105, 28, 32));
			name.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			
			//int index = i;
			name.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent e) {
	                l.RecipeClicked(r);	//interface composition over inheritence 
	            }
	        });
			this.add(name);
			posY += 40;
		}
		
		revalidate();
		repaint();
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
		
		name = new JTextArea(r.getName());
		name.setFont(new Font("Times New Roman",Font.BOLD, 36));
		name.setForeground(new Color(105, 28, 32));
		name.setBounds(550, 100, 300, 250);
		name.setOpaque(false);
		name.setLineWrap(true);
		name.setWrapStyleWord(true);
		this.add(name);
		
		ImageIcon pTemp = new ImageIcon(getClass().getResource("/PotionProdigyAssets/Potions/" + r.getName() + ".png"));
		Image img = pTemp.getImage().getScaledInstance(350, 200, Image.SCALE_SMOOTH);
		
		potion = new JLabel(new ImageIcon(img));
		potion.setBounds(500, 200, img.getWidth(null), img.getHeight(null));
		
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
		
		//brings to front
		setComponentZOrder(name, 0);
	    setComponentZOrder(potion, 0);
	    setComponentZOrder(Base, 0);
	    setComponentZOrder(list, 0);
		
		revalidate();
		repaint();
		
	}
	
	
	//HELPER FUNCTION BC ITS MAKING ME DIZZY
	public JLabel makeLabel(String path, int posX, int posY) {
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
	
	public void leftArrListener(MouseAdapter l) {
		leftArr.addMouseListener(l);
	}
	
	public void rightArrListener(MouseAdapter l) {
		rightArr.addMouseListener(l);
	}
	
	public void arrowListener(MouseAdapter l) {
		arrow.addMouseListener(l);
	}
	
	public void textListener(ActionListener e) {
		searchBar.addActionListener(e);
	}

	public JTextField getSearchBar() {
		return searchBar;
	}

	public void setSearchBar(JTextField searchBar) {
		this.searchBar = searchBar;
	}

	public JLabel getSpellbook() {
		return spellbook;
	}

	public void setSpellbook(JLabel spellbook) {
		this.spellbook = spellbook;
	}

	public Spellbook getSb() {
		return sb;
	}

	public void setSb(Spellbook sb) {
		this.sb = sb;
	}

	public JLabel[] getRecipeLabels() {
		return recipeLabels;
	}

	public void setRecipeLabels(JLabel[] recipeLabels) {
		this.recipeLabels = recipeLabels;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public void setRecipeListener(RecipeListener l) {
		this.l = l;
	}
	
	public RecipeListener getRecipeListener() {
		return this.l;
	}
}
