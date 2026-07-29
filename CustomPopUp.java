import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
//import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

//all customPopUp should be here
public class CustomPopUp{
	
	private JPanel contentPanel;
	private JPanel buttonPanel;
	private JDialog dialog;			//these attributes are the templates 
	
	
	public CustomPopUp(Component parent, String title) {
		this.dialog = new JDialog((JDialog) null, title, true);
		dialog.setUndecorated(true); // removes default windows border
		
		//the default bg for all the pop ups in game 
		JPanel popUpBg = new JPanel (new BorderLayout()) {
			 @Override
	            protected void paintComponent(Graphics g) {
	                super.paintComponent(g);
	                    g.setColor(new Color(250, 245, 225)); 
	                    g.fillRect(0, 0, getWidth(), getHeight());
	                }
	        };
	        
	    Border customBorder = BorderFactory.createLineBorder(new Color(94, 47, 20), 4); // 4px brown border
	    popUpBg.setBorder(customBorder);
	    
	    //invisible for now
	    contentPanel = new JPanel();
        contentPanel.setOpaque(false);

        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        
        popUpBg.add(contentPanel, BorderLayout.CENTER);
        popUpBg.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setContentPane(popUpBg);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
	}

	//GETTERS
    public JPanel getContentPanel() {
        return contentPanel;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public JDialog getDialog() {
        return dialog;
    }
	
	//WILL NEED TO EDIT THE POSITIONS
	private JButton customButton(String path) {
		JButton button = new JButton(new ImageIcon(new ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(90, 45, Image.SCALE_SMOOTH)));
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		
		return button;
	}
	
    public static String promptName(Component parent) {
        CustomPopUp cd = new CustomPopUp(parent, "Enter Name:");
        
        JTextField textField = new JTextField(15);
        JLabel label = new JLabel("Enter your name: ");
        JButton enter = new JButton("OK");
        
        label.setFont(new Font("Times New Roman", Font.BOLD, 20));
        
        final String[] result = new String[1];
        enter.addActionListener(e -> {result[0] = textField.getText();
        							  cd.getDialog().dispose();});
        enter.setVisible(true);
        enter.setContentAreaFilled(false);
		enter.setFocusPainted(false);
		enter.setBorderPainted(false);
        
        cd.getDialog().getRootPane().setDefaultButton(enter); 		//changed the confirm button to 'ENTER' keyboard input
        
        cd.getContentPanel().setLayout(new BorderLayout());
        cd.getContentPanel().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        cd.getContentPanel().add(label, BorderLayout.NORTH);		//adds the label to the content pane
        cd.getContentPanel().add(textField, BorderLayout.CENTER);	//adds the filed that will be accepting use rinput to the panel
        cd.getContentPanel().add(enter, BorderLayout.SOUTH);							//adds the eenter function to the contentpanel
        
        cd.getDialog().pack();
        cd.getDialog().setLocationRelativeTo(null);
        cd.getDialog().setVisible(true);
        
        return result[0];
        }

    
    //default prompt yes no, just put what the program is asking for in string message
    public static boolean promptYesNo(Component parent, String message) {
    	CustomPopUp cd = new CustomPopUp(parent, message);
    	JLabel label = new JLabel(message);							//the prompt
    	cd.getContentPanel().add(label);
    	
    	label.setFont(new Font("Times New Roman", Font.BOLD, 20));
    	
    	JButton confirm = cd.customButton("/PotionProdigyAssets/UI Assets/Confirm.png");
    	JButton cancel = cd.customButton("/PotionProdigyAssets/UI Assets/Cancel.png");
    	cd.getButtonPanel().setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
    	cd.getButtonPanel().add(confirm);
        cd.getButtonPanel().add(cancel);
        
        final boolean[] result = new boolean[1];
        
        confirm.addActionListener(e -> {result[0] = true;
        								cd.getDialog().dispose();
        							   });
        cancel.addActionListener(e -> {result[0] = false;
										cd.getDialog().dispose();
        							  });
        cd.getDialog().pack();
        cd.getDialog().setLocationRelativeTo(null);
        cd.getDialog().setVisible(true);
        
    	return result[0];
    }

    public static int promptBrewOrBless(Component parent) {
    	CustomPopUp cd = new CustomPopUp(parent, " ");
    	
    	JLabel label = new JLabel("Brew Concoction or Bless Cauldron?");
        JButton brew = new JButton("Brew");
        JButton bless = new JButton("Bless");
        
        label.setFont(new Font("Times New Roman", Font.BOLD, 20));
        
        final int[] result = new int[1];
        brew.addActionListener(e -> {result[0] = 1;
        							  cd.getDialog().dispose();});
        brew.setVisible(true);
        brew.setContentAreaFilled(false);
        brew.setFocusPainted(false);
        brew.setBorderPainted(false);
        cd.getButtonPanel().add(brew);
        
        bless.addActionListener(e -> {result[0] = 2;
									  cd.getDialog().dispose();});
        bless.setVisible(true);
        bless.setContentAreaFilled(false);
        bless.setFocusPainted(false);
        bless.setBorderPainted(false);
        cd.getButtonPanel().add(bless);
        
        cd.getContentPanel().setLayout(new BorderLayout());
        cd.getContentPanel().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        cd.getContentPanel().add(label, BorderLayout.NORTH);		//adds the label to the content pane
        cd.getContentPanel().add(cd.getButtonPanel(), BorderLayout.SOUTH);							//adds the eenter function to the contentpanel
        
        cd.getDialog().pack();
        cd.getDialog().setLocationRelativeTo(null);
        cd.getDialog().setVisible(true);
        
        return result[0];
    }
    
    public static boolean promptBrewMode(Component parent) {
    	CustomPopUp cd = new CustomPopUp(parent, " ");
    	
    	JLabel label = new JLabel("Creative Mode or Recipe Mode?");
        JButton creative = new JButton("Creative");
        JButton recipe = new JButton("Recipe");
        
        label.setFont(new Font("Times New Roman", Font.BOLD, 20));
        
        final boolean[] result = new boolean[1];
        creative.addActionListener(e -> {result[0] = false;
        							  cd.getDialog().dispose();});
        creative.setVisible(true);
        creative.setContentAreaFilled(false);
        creative.setFocusPainted(false);
        creative.setBorderPainted(false);
        cd.getButtonPanel().add(creative);
        
        recipe.addActionListener(e -> {result[0] = true;
									  cd.getDialog().dispose();});
        recipe.setVisible(true);
        recipe.setContentAreaFilled(false);
        recipe.setFocusPainted(false);
        recipe.setBorderPainted(false);
        cd.getButtonPanel().add(recipe);
        
        cd.getContentPanel().setLayout(new BorderLayout());
        cd.getContentPanel().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        cd.getContentPanel().add(label, BorderLayout.NORTH);		//adds the label to the content pane
        cd.getContentPanel().add(cd.getButtonPanel(), BorderLayout.SOUTH);							//adds the eenter function to the contentpanel
        
        cd.getDialog().pack();
        cd.getDialog().setLocationRelativeTo(null);
        cd.getDialog().setVisible(true);
        
        return result[0];
    }
    
    public static int promptWelcome(Component parent, String p, boolean newGame) {
    	CustomPopUp cd = new CustomPopUp(parent, "");
    	
    	JLabel label;
    	
    	if(newGame == true) {
    		label = new JLabel("<html><div style='text-align:center;'>Welcome " + p + "! Your alchemy adventure begins. <br>Don't forget to click the back arrow before exiting to save!</div></html>");
    	}else
    		label = new JLabel("<html><div style='text-align:center;'>Welcome back " + p + "!<br>Don't forget to click the back arrow before exiting to save!</div></html>");
        
        JButton confirm = cd.customButton("/PotionProdigyAssets/UI Assets/Confirm.png");
    	cd.getButtonPanel().setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
    	cd.getButtonPanel().add(confirm);
        
        label.setFont(new Font("Times New Roman", Font.BOLD, 20));
        
        final int[] result = new int[1];
        confirm.addActionListener(e -> {result[0] = 1;
        							  cd.getDialog().dispose();});
       
        cd.getDialog().getRootPane().setDefaultButton(confirm); 		//changed the confirm button to 'ENTER' keyboard input
        
        cd.getContentPanel().setLayout(new BorderLayout());
        cd.getContentPanel().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        cd.getContentPanel().add(label, BorderLayout.NORTH);		//adds the label to the content pane
        cd.getContentPanel().add(confirm, BorderLayout.SOUTH);							//adds the eenter function to the contentpanel
        
        cd.getDialog().pack();
        cd.getDialog().setLocationRelativeTo(null);
        cd.getDialog().setVisible(true);
        
        return result[0];
    }
}
