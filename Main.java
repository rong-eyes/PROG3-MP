//import java.util.ArrayList;
//import java.util.Scanner;
import javax.swing.JFrame;

public class Main { //this should be the entry point of the project

	//private static final String COMPENDIUM_PATH = "POTION COMPENDIUM.csv";

	public static void main(String[] args) {

		JFrame PotionProdigy = new JFrame();
		PotionProdigy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//PotionProdigy.setSize(1280,960); //replace by frame.pack()
		PotionProdigy.setResizable(false); //default 4:3 screen ratio; OMORI aspect ratio
		PotionProdigy.setTitle("Potion Prodigy");
		
		TitlePanel titleScreen = new TitlePanel();
		PotionProdigy.add(titleScreen);
		PotionProdigy.pack();
		/*
		 * frame.add(panel);
		 * frame.pack(); //Automatical sizes the window to fit the biggest componenet of panel
		 */
		
		PotionProdigy.setLocationRelativeTo(null); //opens the application at the center of the user's screen
		PotionProdigy.setVisible(true);//should be at the end
	
	}
	
}
