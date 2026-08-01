import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;


public class TitlePanel extends JPanel{ //the View for SaveManager / Title Screen

	//SCREEN SETTING
	final int screenWidth = 960; 
	final int screenHeight = 720;
	private Image bgImage; //replace with title screen file
	private static final long serialVersionUID = 1L;
	
	private JButton newGameButton;
	private JButton loadGameButton;
	
	public TitlePanel() {
		try {
			this.bgImage = ImageIO.read(getClass().getResource("/PotionProdigyAssets/UI Assets/Main Menu/BG.png"));
		} catch (IOException e) {
			this.bgImage = null;
		}
		
		//SETTING SIZE
		this.setLayout(null);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true); //better game performance
		
		//BUTTONS
		// One single line to load, scale, wrap, and assign the image to the button		
		this.newGameButton = createButton("/PotionProdigyAssets/UI Assets/Main Menu/New Game.png", 310, 440);
		this.add(newGameButton);
		
		this.loadGameButton = createButton("/PotionProdigyAssets/UI Assets/Main Menu/Load Game.png", 310, 530);
		this.add(loadGameButton);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(bgImage != null) {
			g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
	
	//BUTTON FUNCTIONS
	private JButton createButton(String path, int posX, int posY) {
		ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(330, 80, Image.SCALE_SMOOTH));
		JButton button = new JButton(icon);
		
		//BUTTON SETTINGS
		button.setBounds(posX, posY, 330, 80);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		
		return button;
	}
	
	public void addNewGameListener(ActionListener l) {
		newGameButton.addActionListener(l);
	}
	
	public void addLoadGameListener(ActionListener l) {
		loadGameButton.addActionListener(l);
	}
	
	//POP UPS
	public String promptPlayerName() {
		return CustomPopUp.promptName(this);
	}
	
	public boolean promptOverWriteConfirm() {
		return CustomPopUp.promptYesNo(this, "A save file already has that name. Overwrite it?");
	}
	
	public boolean promptNewGameConfirm() {
		return CustomPopUp.promptYesNo(this, "No such save exists. Start a new game instead?");
	}
	
	public int WelcomeMessage(String p, boolean newGame) {
		return CustomPopUp.promptWelcome(this, p, newGame);
	}
}
