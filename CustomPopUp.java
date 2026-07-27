import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;

//all customPopUp should be here
public class CustomPopUp extends JPanel{
	
	/**
	 * dont mind serial version UID; eclipse problem apparently
	 */
	private static final long serialVersionUID = 854237780511199283L;
	private JLabel pnel;
	
	
    public static String promptName(Component parent) {
        String name = "";

        JPanel popUpBg = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                    g.setColor(new Color(250, 245, 225)); 
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
        };
        
        //BorderFactory.createMatteBorder() to use a repeating image for the border tile
        Border customBorder = BorderFactory.createLineBorder(new Color(94, 47, 20), 4); // 4px gold border
        popUpBg.setBorder(customBorder);
        
        
        //CUSTOM BUTTONS
        ImageIcon okIcon = new ImageIcon("optionButton.jpg");
        ImageIcon cancelIcon = new ImageIcon("optionButton.jpg");
        
        JButton confirmButton = new JButton(okIcon);
        confirmButton.setPreferredSize(new Dimension(100, 40));
        confirmButton.setBorderPainted(false);
        confirmButton.setContentAreaFilled(false);
        confirmButton.setFocusPainted(false); // Removes the ugly selection box outline

        JButton cancelButton = new JButton(cancelIcon);
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setBorderPainted(false);
        cancelButton.setContentAreaFilled(false);
        cancelButton.setFocusPainted(false);

     // Group your custom buttons into an object array
        Object[] customButtons = { confirmButton, cancelButton };

        
        JOptionPane optionPane = new JOptionPane("Enter your name:", JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, customButtons, confirmButton);
        optionPane.setWantsInput(true);


        makeComponentsTransparent(optionPane);
        optionPane.setOpaque(false);

        popUpBg.add(optionPane, BorderLayout.CENTER);

        JDialog dialog = new JDialog((JDialog) null, "Enter Player Name:", true);
        dialog.setContentPane(popUpBg);
        dialog.setUndecorated(true); // removes default windows border
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        
        confirmButton.addActionListener(e -> dialog.setVisible(false));
        cancelButton.addActionListener(e -> {
            optionPane.setInputValue(null); // Clear value flag to mark it as cancelled
            dialog.setVisible(false);
        });

        optionPane.addPropertyChangeListener(e -> {
            String prop = e.getPropertyName();
            if (dialog.isVisible() && (e.getSource() == optionPane) && 
               (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
                dialog.setVisible(false);
            }
        });
       

        while (name.isEmpty()) {
            dialog.setVisible(true);
            Object inputValue = optionPane.getInputValue();

            if (inputValue == null || inputValue.equals(JOptionPane.UNINITIALIZED_VALUE)) {
                return null; // User cancelled out
            }

            name = inputValue.toString().trim();
            
            // Reset value for next loop if validation fails
            if (name.isEmpty()) {
                optionPane.setInputValue(JOptionPane.UNINITIALIZED_VALUE);
            }
        }

        return name;
    }
    
    public static boolean promptYesNo() {
    	return true;
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
