//import java.util.ArrayList;
//import java.util.Scanner;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main { //this should be the entry point of the project

	public static void main(String[] args) {
		
		JFrame PotionProdigy = new JFrame();
		PotionProdigy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//resizingPotionProdigy.setSize(1280,960); //replace by frame.pack()
		PotionProdigy.setResizable(false); //default 4:3 screen ratio; OMORI aspect ratio
		PotionProdigy.setTitle("Potion Prodigy");
		
		JPanel currentPanel = new JPanel(new CardLayout());	//for easyscreen switching
		
		//TITLE SCREEN
		TitleModel titleModel = new TitleModel();
		TitlePanel titleScreen = new TitlePanel();
		currentPanel.add(titleScreen, "TITLE");
		
		//HOME SCREEN
		HomeModel homeModel = new HomeModel();
		HomePanel homeScreen = new HomePanel();
			
		//CONTROLLERS
		TitleController titleController = new TitleController(titleScreen, titleModel, currentPanel, homeScreen);

		PotionProdigy.add(currentPanel);
		PotionProdigy.pack();
		PotionProdigy.setLocationRelativeTo(null); //opens the application at the center of the user's screen
		PotionProdigy.setVisible(true);//should be at the end
	
	}
	
}
