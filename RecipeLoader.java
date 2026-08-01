import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RecipeLoader {

	private static final int FIRST_INGREDIENT_COLUMN = 4;

	/**
	* Loads all the valid combinations into an ArrayList to be used later as a reference list for combos
	*
	* @param path the path that describes the location of the file containing the valid combinations (POTION COMPENDIUM.CSV)
	* @return an ArrayList with all the valid Recipes (combinations); an empty list if the file could not be read
	*/
	public static ArrayList<Recipe> loadRecipes(String path) {
		ArrayList<Recipe> recipes = new ArrayList<>();
		List<String> lines = readAllLines(path);

		for (String raw : lines) {
			String line = stripBom(raw).trim();
			if (!line.isEmpty()) {
				String[] fields = line.split(",", -1);
				// need at least ID, NAME, BASE, PRICE and one ingredient
				if (fields.length >= FIRST_INGREDIENT_COLUMN + 1) {
					try {
						int id = Integer.parseInt(fields[0].trim());
						String name = fields[1].trim();
						String base = fields[2].trim();
						int price = Integer.parseInt(fields[3].trim());
						ArrayList<Ingredient> ingredients = new ArrayList<>();
						for (int i = FIRST_INGREDIENT_COLUMN; i < fields.length; i++) {
							String ing = fields[i].trim();
							if (!ing.isEmpty()) {
								ingredients.add(new Ingredient(ing, 1));
							}
						}
						recipes.add(new Recipe(id, name, base, price, ingredients));
					} catch (NumberFormatException e) {
						// skip this malformed row and keep loading the rest
					}
				}
			}
		}

		return recipes;
	}

	/**
	* Reads the compendium file line by line.
	* <p>
	* The file is searched for alongside the compiled classes first, which is where it is found when the game
	* is run from the project or from an exported jar file. If it is not there, the folder the game was
	* launched from is checked instead. A file that cannot be read returns an empty list so that the game
	* does not crash.
	* </p>
	*
	* @param path the name of the compendium file
	* @return the lines of the file; an empty list if the file could not be read
	*/
	private static List<String> readAllLines(String path) {
		List<String> lines = new ArrayList<>();
		InputStream bundled = RecipeLoader.class.getResourceAsStream("/" + path);

		if (bundled != null) {
			Scanner reader = new Scanner(bundled, StandardCharsets.UTF_8);
			while (reader.hasNextLine()) {
				lines.add(reader.nextLine());
			}
			reader.close();
		} else {
			Path csv = Path.of(path);
			if (Files.exists(csv)) {
				try {
					lines = Files.readAllLines(csv, StandardCharsets.UTF_8);
				} catch (IOException e) {
					lines = new ArrayList<>();
				}
			}
		}

		return lines;
	}


	/**
	* Finds the specified recipe from the ID
	*
	* @param recipes the list of valid recipes
	* @param id the ID of recipe to be found
	* @return returns the Recipe if found; null otherwise
	*/
	public static Recipe findRecipeById(ArrayList<Recipe> recipes, int id) {
		if (recipes == null) {
			return null;
		}

		for (int i = 0; i < recipes.size(); i++) {
			if (recipes.get(i).getConcoctionID() == id) {
				return recipes.get(i);
			}
		}
		return null;
	}

	/**
	* Removes the Byte Order Mark (BOM) from the beginning of a text string when reading a file
	*
	* @param s the string or line in the file being read
	*/
	private static String stripBom(String s) {
		if (!s.isEmpty() && s.charAt(0) == 0xFEFF) {
			return s.substring(1);
		}
		return s;
	}
}
