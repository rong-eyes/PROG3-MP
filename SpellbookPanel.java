import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SpellbookPanel extends JPanel{
	final int screenWidth = 960; 
	final int screenHeight = 720;
	private Image bgImage; 
	private ImageIcon fieldImage;
	private JLabel arrow;
	private JLabel spellbook;
	private static final long serialVersionUID = 1L;
	
	
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
		
		arrow = makeLabel("/PotionProdigyAssets/UI Assets/Back Arrow.png", 46, 63);
		arrow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.add(arrow);
		
		ImageIcon fieldTemp = new ImageIcon(getClass().getResource("/PotionProdigyAssets/UI Assets/Spellbook/Search Bar.png"));
		
		this.fieldImage = resizeIcon(fieldTemp);
		
		JTextField searchBar = new JTextField(20) {
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
		this.add(spellbook);
		this.add(new RecipesPanel());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(bgImage != null) {
			g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
	
	private ImageIcon resizeIcon(ImageIcon i) {
		int newWidth = (int)(i.getIconWidth() * (960.0/1280.0));
		int newHeight= (int)(i.getIconHeight() * (960.0/1280.0));
		
		ImageIcon scaled = new ImageIcon(i.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
		
		return scaled;
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
	
	public void arrowListener(MouseAdapter l) {
		arrow.addMouseListener(l);
	}

	public JLabel getSpellbook() {
		return spellbook;
	}

	public void setSpellbook(JLabel spellbook) {
		this.spellbook = spellbook;
	}

}

class RecipesPanel extends JPanel{
	
	/**
	 * eclipse thing
	 */
	private static final long serialVersionUID = 1L;
	private final int RECIPES_PER_PAGE = 10;
	//private int currentPage = 0;
	private ArrayList<Recipe> unlockedRecipes;
	
	public RecipesPanel() {
		this.unlockedRecipes = new ArrayList<>();
		
		setLayout(null);
		displayPage();
	}
	
	public void displayPage() {
		int currentPage = 0;
		int start = currentPage * getRECIPES_PER_PAGE();
		int end = Math.min(start + getRECIPES_PER_PAGE(), unlockedRecipes.size());
		
		int posY = 50;
		
		for(int i = start; i < end; i++) {
			String Recipe = unlockedRecipes.get(i).getName();
			JLabel name = new JLabel(Recipe);
			name.setBounds(100, posY, 300, 30);
			name.setFont(new Font("Times New Roman", Font.ITALIC, 12));
			name.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			
			//int index = i;
			name.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent e) {
	                //call panel display
	            }
	        });
			add(name);
			posY += 10;
		}
	}
	
	public ArrayList<Recipe> getUnlockedRecipes() {
		return unlockedRecipes;
	}

	public void setUnlockedRecipes(ArrayList<Recipe> unlockedRecipes) {
		this.unlockedRecipes = unlockedRecipes;
	}

	public Spellbook passSpellbook(Player p) {
		return p.getSpellbook();
	}
	
	public int getRECIPES_PER_PAGE() {
		return RECIPES_PER_PAGE;
	}
}

class displayRecipePanel{
	
}
