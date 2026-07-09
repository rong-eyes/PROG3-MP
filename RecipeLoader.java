package MCO1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RecipeLoader {

	private static final int FIRST_INGREDIENT_COLUMN = 4;

	private RecipeLoader() {
	}

	public static ArrayList<Recipe> loadRecipes(String path) {
		ArrayList<Recipe> recipes = new ArrayList<>();
		Path csv = Path.of(path);

		if (!Files.exists(csv)) {
			System.out.println("Error: could not find the potion compendium at \"" + path
					+ "\". Creative-mode validation will be unavailable.");
			return recipes;
		}

		try {
			List<String> lines = Files.readAllLines(csv, StandardCharsets.UTF_8);
			for (String raw : lines) {
				String line = stripBom(raw).trim();
				if (line.isEmpty()) {
					continue;
				}
				String[] fields = line.split(",", -1);
				if (fields.length < FIRST_INGREDIENT_COLUMN + 1) {
					continue; // need at least ID, NAME, BASE, PRICE and one ingredient
				}
				try {
					int id = Integer.parseInt(fields[0].trim());
					String name = fields[1].trim();
					String base = fields[2].trim();
					int price = Integer.parseInt(fields[3].trim());
					ArrayList<InventoryItem> ingredients = new ArrayList<>();
					for (int i = FIRST_INGREDIENT_COLUMN; i < fields.length; i++) {
						String ing = fields[i].trim();
						if (!ing.isEmpty()) {
							ingredients.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, ing, 1));
						}
					}
					recipes.add(new Recipe(id, name, base, price, ingredients));
				} catch (NumberFormatException e) {
					// skip this malformed row and keep loading the rest
				}
			}
		} catch (IOException e) {
			System.out.println("Error: could not read the potion compendium at \"" + path + "\".");
		}

		return recipes;
	}

	public static Recipe findRecipeById(ArrayList<Recipe> recipes, int id) {
		for (int i = 0; i < recipes.size(); i++) {
			if (recipes.get(i).getConcoctionID() == id) {
				return recipes.get(i);
			}
		}
		return null;
	}

	private static String stripBom(String s) {
		if (!s.isEmpty() && s.charAt(0) == 0xFEFF) {
			return s.substring(1);
		}
		return s;
	}
}
