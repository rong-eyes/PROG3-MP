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
import java.awt.Container;
import java.awt.FlowLayout;
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
		JButton button = new JButton(new ImageIcon(new ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(330, 80, Image.SCALE_SMOOTH)));
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
    	CustomPopUp cd = new CustomPopUp(parent, "Confirm?");
    	JLabel label = new JLabel(message);							//the prompt
    	cd.getContentPanel().add(label);
    	
    	JButton confirm = cd.customButton("/PotionProdigyAssets/UI Assets/Confirm.png");
    	JButton cancel = cd.customButton("/PotionProdigyAssets/UI Assets/Cancel.png");
    	cd.getButtonPanel().setLayout(new FlowLayout(FlowLayout.CENTER));
    	cd.getButtonPanel().add(confirm);
        cd.getButtonPanel().add(cancel);
        
        final boolean[] result = new boolean[1];
        
        confirm.addActionListener(e -> {result[0] = true;
        								cd.getDialog().dispose();
        							   });
        cancel.addActionListener(e -> {result[0] = false;
										cd.getDialog().dispose();
        							  });
    	cd.getDialog().setVisible(true);
        
    	return result[0];
    }

    //recursively remove default solid backgrounds; HELPER FUNCTION
    public static void makeComponentsTransparent(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
                ((JPanel) comp).setOpaque(false);
            }
            if (comp instanceof Container) {
                makeComponentsTransparent((Container) comp);
            }
        }
    }
}
