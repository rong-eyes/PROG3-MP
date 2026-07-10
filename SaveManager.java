package MCO1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {

	private static final String[] FRUIT_NAMES = { "STRAWBERRY", "ORANGE", "LEMON", "BANANA", "MANGO","PINEAPPLE", "KIWI", "BLUEBERRY", "COCONUT" };
	private static final String[] BASE_NAMES = { "SYRUP BASE", "BUBBLE BASE", "PERFUME BASE", "MILK BASE", "LOTION BASE"};
	private static final String NL = "\r\n";

	private SaveManager() {
	}

	public static boolean saveGame(Player player) {
		String fileName = player.getPlayerName() + ".txt";
		Inventory inv = player.getInventory();
		StringBuilder sb = new StringBuilder();

		sb.append("NAME = ").append(player.getPlayerName()).append(NL);
		sb.append(NL);
		sb.append("CRYSTALS = ").append(player.getCrystals()).append(NL);
		sb.append(NL);
		sb.append("[INVENTORY]").append(NL);
		for (String fruit : FRUIT_NAMES) {
			sb.append(fruit).append(" = ").append(quantityOf(fruit, inv.getIngredients())).append(NL);
		}
		sb.append(NL);
		for (String base : BASE_NAMES) {
			sb.append(base).append(" = ").append(quantityOf(base, inv.getBases())).append(NL);
		}
		sb.append(NL);
		sb.append("TOTAL CAULDRONS = ").append(inv.getCauldrons().size()).append(NL);
		sb.append("USABLE CAULDRONS = ").append(countUsable(inv.getCauldrons())).append(NL);
		sb.append(NL);
		sb.append("[SPELLBOOK]").append(NL);
		sb.append(spellbookIds(player.getSpellbook())).append(NL);

		try {
			Files.writeString(Path.of(fileName), sb.toString(), StandardCharsets.UTF_8);
			System.out.println("Your progress has been saved to \"" + fileName + "\".");
			return true;
		} catch (IOException e) {
			System.out.println("Error: your progress could not be saved to \"" + fileName + "\".");
			return false;
		}
	}

	public static boolean saveExists(String name) {
		return Files.exists(Path.of(name + ".txt"));
	}
	
	public static Player loadGame(String name, ArrayList<Recipe> allRecipes) {
		Path path = Path.of(name + ".txt");
		if (!Files.exists(path)) {
			System.out.println("The save file \"" + name + ".txt\" could not be found.");
			return null;
		}

		try {
			List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			String playerName = name;
			int crystals = 0;
			int totalCauldrons = 0;
			int usableCauldrons = 0;
			ArrayList<InventoryItem> fruits = new ArrayList<>();
			ArrayList<InventoryItem> bases = new ArrayList<>();
			Spellbook spellbook = new Spellbook();
			String section = "";

			for (String raw : lines) {
				String line = raw.trim();
				if (line.isEmpty()) {
					continue;
				}
				if (line.startsWith("NAME =")) {
					playerName = valueAfterEquals(line);
				} else if (line.startsWith("CRYSTALS =")) {
					crystals = Integer.parseInt(valueAfterEquals(line));
				} else if (line.equals("[INVENTORY]")) {
					section = "INVENTORY";
				} else if (line.startsWith("TOTAL CAULDRONS =")) {
					totalCauldrons = Integer.parseInt(valueAfterEquals(line));
				} else if (line.startsWith("USABLE CAULDRONS =")) {
					usableCauldrons = Integer.parseInt(valueAfterEquals(line));
				} else if (line.equals("[SPELLBOOK]")) {
					section = "SPELLBOOK";
				} else if (section.equals("INVENTORY") && line.contains("=")) {
					String key = line.substring(0, line.indexOf('=')).trim();
					int qty = Integer.parseInt(valueAfterEquals(line));
					if (isBaseName(key)) {
						bases.add(new InventoryItem(InventoryItem.TYPE_BASE, key, qty));
					} else {
						fruits.add(new InventoryItem(InventoryItem.TYPE_INGREDIENT, key, qty));
					}
				} else if (section.equals("SPELLBOOK")) {
					for (String idStr : line.split(",")) {
						String trimmed = idStr.trim();
						if (trimmed.isEmpty()) {
							continue;
						}
						Recipe r = RecipeLoader.findRecipeById(allRecipes, Integer.parseInt(trimmed));
						if (r != null) {
							spellbook.addRecipe(r);
						}
					}
				}
			}

			ArrayList<Cauldron> cauldrons = new ArrayList<>();
			for (int i = 0; i < totalCauldrons; i++) {
				Cauldron c = new Cauldron();
				c.setUsable(i < usableCauldrons);
				cauldrons.add(c);
			}

			Inventory inventory = new Inventory(fruits, bases, cauldrons);
			inventory.setUsableCauldrons(usableCauldrons);

			Player player = new Player(playerName, inventory, crystals, spellbook);
			System.out.println("The save file has been successfully loaded.");
			return player;
		} catch (IOException e) {
			System.out.println("Error: the save file \"" + name + ".txt\" could not be read.");
			return null;
		} catch (RuntimeException e) {
			System.out.println("Error: the save file \"" + name + ".txt\" is corrupted and could not be loaded.");
			return null;
		}
	}

	private static int quantityOf(String name, ArrayList<InventoryItem> items) {
		for (InventoryItem item : items) {
			if (item.getName().equals(name)) {
				return item.getQuantity();
			}
		}
		return 0;
	}

	private static int countUsable(ArrayList<Cauldron> cauldrons) {
		int usable = 0;
		for (Cauldron c : cauldrons) {
			if (c.isUsable()) {
				usable++;
			}
		}
		return usable;
	}

	private static String spellbookIds(Spellbook spellbook) {
		ArrayList<Recipe> recipes = spellbook.getUnlockedRecipes();
		StringBuilder ids = new StringBuilder();
		for (int i = 0; i < recipes.size(); i++) {
			if (i > 0) {
				ids.append(",");
			}
			ids.append(recipes.get(i).getConcoctionID());
		}
		return ids.toString();
	}

	private static String valueAfterEquals(String line) {
		int idx = line.indexOf('=');
		return line.substring(idx + 1).trim();
	}

	private static boolean isBaseName(String name) {
		for (String b : BASE_NAMES) {
			if (b.equals(name)) {
				return true;
			}
		}
		return false;
	}
}
