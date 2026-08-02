
public class ItemArt { //looks up which image file belongs to which item

	private static final String INGREDIENT_FOLDER = "/PotionProdigyAssets/Ingredients/";
	private static final String POTION_FOLDER = "/PotionProdigyAssets/Potions/";
	private static final String CAULDRON_IMAGE = "/PotionProdigyAssets/UI Assets/Brewing Screen/Cauldron.png";

	private static final String[] itemNames = {"STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO", "PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT",
													"SYRUP BASE", "BUBBLE BASE", "PERFUME BASE", "MILK BASE", "LOTION BASE", "CAULDRON"};

	//matches itemNames; each entry is the file name inside the Ingredients folder, without the .png
	private static final String[] artFiles = {"Strawberry", "Orange", "Lemon", "Banana", "Mango", "Pineapple", "Kiwi", "Blueberry", "Coconut",
												"Syrup Base", "Soap Base", "Perfume Base", "Milk Base", "Lotion Base", ""};

	/**
	* Returns the file location of the image that belongs to an item.
	* <p>
	* The cauldron is handled separately because it uses the larger image from the brewing screen instead of
	* an ingredient image. Every other item is looked up in the Ingredients folder using the file name listed
	* in artFiles. An item whose image file is not in the project yet returns null, and the screens then
	* display its name on its own instead of failing.
	* </p>
	*
	* @param name the name of the item being looked up
	* @return the file location of the image; null if the item has no image file
	*/
	public static String pathOf(String name) {
		if(name == null)
			return null;

		if(name.equals("CAULDRON"))
			return existingPath(CAULDRON_IMAGE);

		for(int i = 0; i < itemNames.length; i++) {
			if(itemNames[i].equals(name) && !artFiles[i].isEmpty())
				return existingPath(INGREDIENT_FOLDER + artFiles[i] + ".png");
		}

		return null;
	}

	/**
	* Returns the file location of the image of a finished concoction.
	* <p>
	* Each drawing in the Potions folder is named after the concoction itself, so the name of the recipe is
	* all that is needed to find it. Concoctions that have not been drawn yet return null, and the screens
	* then display the name on its own.
	* </p>
	*
	* @param potionName the name of the concoction being looked up
	* @return the file location of the image; null if the concoction has no image file
	*/
	public static String potionPathOf(String potionName) {
		if(potionName == null)
			return null;

		return existingPath(POTION_FOLDER + potionName + ".png");
	}

	/**
	* Checks that an image file is actually part of the project before its location is handed out.
	* <p>
	* Asking for an image that is not there would otherwise stop the screen from opening, so a missing file
	* is reported as null instead. This also means a new drawing only has to be saved into the assets folder
	* to start appearing in the game.
	* </p>
	*
	* @param path the file location being checked
	* @return the same location if the file is present; null if it is not
	*/
	private static String existingPath(String path) {
		if(ItemArt.class.getResource(path) == null)
			return null;

		return path;
	}
}
