
public class HomeModel { //Model for the Home Screen; previous 'Main.java'

	/**
	* Checks whether any of the player's cauldrons contain junk from a failed experiment.
	*
	* @param p the player whose cauldrons are being checked
	* @return true if at least one cauldron needs blessing; false otherwise
	*/
	public boolean checkBrokenCauldrons(Player p) {
		return p.getInventory().getUnusableCauldrons() > 0;
	}

	/**
	* Blesses the first cauldron that needs it.
	*
	* @param p the player paying for the blessing
	* @return true if a cauldron was blessed; false if none needed blessing or the player cannot afford it
	*/
	public boolean blessCauldron(Player p) {
		Cauldron broken = p.getInventory().getBrokenCauldron();

		if(broken == null)
			return false;

		return broken.blessCauldron(p);
	}

	/**
	* Builds the message displayed to the player after a blessing.
	*
	* @param blessed whether the blessing was completed
	* @param p the player, whose remaining crystals are included in the message
	* @return the message to be shown in the pop up
	*/
	public String blessMessage(boolean blessed, Player p) {
		if(blessed)
			return "The cauldron has been blessed and is good as new! " + Cauldron.BLESSING_COST
					+ " crystals spent, and you have " + p.getCrystals() + " left.";

		return "A blessing costs " + Cauldron.BLESSING_COST + " crystals and you only have " + p.getCrystals() + ".";
	}

	/**
	* Builds the message displayed to the player after claiming the login bonus.
	*
	* @param pick the fruit the player received; null if the bonus was already claimed this session
	* @return the message to be shown in the pop up
	*/
	public String loginMessage(String pick) {
		if(pick == null)
			return "You already claimed your login bonus. Come back after exiting and re-entering the game.";

		return "The clock chimes! You claimed your login bonus and received 1 " + pick + ".";
	}
}
