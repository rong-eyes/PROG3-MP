package MCO1;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	private static final String COMPENDIUM_PATH = "POTION COMPENDIUM.csv";

	private Main() {
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("=====================================");
		System.out.println("        POTION PRODIGY (MCO1)");
		System.out.println("=====================================");

		ArrayList<Recipe> allRecipes = RecipeLoader.loadRecipes(COMPENDIUM_PATH);

		Player player = boot(sc, allRecipes);
		if (player != null) {
			runMainMenu(player, sc, allRecipes);
		}
		sc.close();
	}

	private static Player boot(Scanner sc, ArrayList<Recipe> allRecipes) {
		while (true) {
			System.out.println();
			System.out.println("1. New Game");
			System.out.println("2. Load Save");
			System.out.print("Choose an option: ");
			if (!sc.hasNextLine()) {
				System.out.println();
				System.out.println("No input received. Exiting.");
				return null;
			}
			String line = sc.nextLine().trim();
			int choice;
			try {
				choice = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				choice = 0;
			}

			if (choice == 1) {
				String name = promptName(sc);
				if (name == null) {
					return null;
				}
				Player player = new Player(name, allRecipes);
				System.out.println("Welcome, " + name + "! Your alchemy adventure begins.");
				return player;
			} else if (choice == 2) {
				String name = promptName(sc);
				if (name == null) {
					return null;
				}
				Player player = SaveManager.loadGame(name, allRecipes);
				if (player != null) {
					System.out.println("Welcome back, " + player.getPlayerName() + "!");
					return player;
				}
				// Missing/unloadable save: offer a new game so the player is not soft-locked.
				Boolean startNew = promptYesNo(sc,
						"Would you like to start a New Game as \"" + name + "\" instead? (Y/N): ");
				if (startNew == null) {
					return null;
				}
				if (startNew) {
					Player newPlayer = new Player(name, allRecipes);
					System.out.println("Welcome, " + name + "! Your alchemy adventure begins.");
					return newPlayer;
				}
				// Declined: acknowledge and loop back to show the boot prompt again.
				System.out.println("Returning to the start menu.");
			} else {
				System.out.println("Invalid choice. Please enter 1 or 2.");
			}
		}
	}

	private static String promptName(Scanner sc) {
		String name = "";
		while (name.isEmpty()) {
			System.out.print("Enter your name: ");
			if (!sc.hasNextLine()) {
				System.out.println();
				System.out.println("No input received. Exiting.");
				return null;
			}
			name = sc.nextLine().trim();
			if (name.isEmpty()) {
				System.out.println("Name cannot be empty. Please try again.");
			}
		}
		return name;
	}

	private static Boolean promptYesNo(Scanner sc, String prompt) {
		while (true) {
			System.out.print(prompt);
			if (!sc.hasNextLine()) {
				System.out.println();
				System.out.println("No input received. Exiting.");
				return null;
			}
			String answer = sc.nextLine().trim();
			if (answer.equalsIgnoreCase("Y")) {
				return true;
			} else if (answer.equalsIgnoreCase("N")) {
				return false;
			} else {
				System.out.println("Please enter Y or N.");
			}
		}
	}

	private static void runMainMenu(Player player, Scanner sc, ArrayList<Recipe> allRecipes) {
		// One shared market for the whole session; also passed to the brew wiring (Packet D).
		Market market = new Market();
		boolean running = true;
		while (running) {
			System.out.println();
			System.out.println("========== MAIN MENU ==========");
			System.out.println("Crystals: " + player.getCrystals());
			System.out.println("1. Brew Concoction");
			System.out.println("2. Check Inventory");
			System.out.println("3. Check Spellbook");
			System.out.println("4. Visit Market");
			System.out.println("5. Bless Cauldron");
			System.out.println("6. Login Bonus");
			System.out.println("7. Exit Game");
			System.out.print("Choose an option: ");

			if (!sc.hasNextLine()) {
				System.out.println();
				System.out.println("No more input. Exiting game. Goodbye!");
				running = false;
				continue;
			}

			String line = sc.nextLine().trim();
			if (line.isEmpty()) {
				System.out.println("Invalid choice. Please enter a number from 1 to 7.");
				continue;
			}
			int choice;
			try {
				choice = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number from 1 to 7.");
				continue;
			}

			switch (choice) {
				case 1 -> handleBrew(player, sc, allRecipes, market);
				case 2 -> {
					System.out.println();
					System.out.println("========== INVENTORY ==========");
					player.getInventory().displayInventory();
				}
				case 3 -> player.getSpellbook().printRecipes();
				case 4 -> market.marketMain(player, sc);
				case 5 -> handleBless(player, sc);
				case 6 -> player.claimLoginBonus();
				case 7 -> {
					SaveManager.saveGame(player);
					System.out.println("Thanks for playing Potion Prodigy. Goodbye!");
					running = false;
				}
				default -> System.out.println("Invalid choice. Please enter a number from 1 to 7.");
			}
		}
	}

	private static void handleBrew(Player player, Scanner sc, ArrayList<Recipe> allRecipes, Market market) {
		ArrayList<Cauldron> cauldrons = player.getInventory().getCauldrons();
		int usable = countUsable(cauldrons);
		if (usable == 0) {
			System.out.println("You have no usable cauldrons. Bless a junked cauldron before brewing.");
			return;
		}

		while (true) {
			System.out.println();
			System.out.println("Brew in which mode?");
			System.out.println("1. Recipe Mode");
			System.out.println("2. Creative Mode");
			System.out.println("0. Cancel (back to main menu)");
			System.out.print("Choose an option: ");
			if (!sc.hasNextLine()) {
				System.out.println();
				System.out.println("No input received. Returning to the main menu.");
				return;
			}
			String line = sc.nextLine().trim();
			if (line.isEmpty()) {
				System.out.println("Invalid choice. Enter 1, 2, or 0.");
				continue;
			}
			int mode;
			try {
				mode = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Enter 1, 2, or 0.");
				continue;
			}

			if (mode == 0) {
				System.out.println("Returning to the main menu.");
				return;
			} else if (mode == 1) {
				player.BrewConcoction(false, firstUsable(cauldrons), sc, allRecipes, market);
				return;
			} else if (mode == 2) {
				if (usable <= 1) {
					System.out.println("Creative mode is disabled with only " + usable
							+ " usable cauldron left: a failed experiment would leave you with none. "
							+ "Try recipe mode instead.");
					continue;
				}
				player.BrewConcoction(true, firstUsable(cauldrons), sc, allRecipes, market);
				return;
			} else {
				System.out.println("Invalid choice. Enter 1, 2, or 0.");
			}
		}
	}

	private static void handleBless(Player player, Scanner sc) {
		ArrayList<Cauldron> cauldrons = player.getInventory().getCauldrons();
		Cauldron junked = null;
		for (int i = 0; i < cauldrons.size() && junked == null; i++) {
			if (!cauldrons.get(i).isUsable()) {
				junked = cauldrons.get(i);
			}
		}
		if (junked == null) {
			System.out.println("There are no cauldrons that need blessing.");
			return;
		}
		junked.blessCauldron(player);
	}

	private static int countUsable(ArrayList<Cauldron> cauldrons) {
		int usable = 0;
		for (int i = 0; i < cauldrons.size(); i++) {
			if (cauldrons.get(i).isUsable()) {
				usable++;
			}
		}
		return usable;
	}

	private static Cauldron firstUsable(ArrayList<Cauldron> cauldrons) {
		for (int i = 0; i < cauldrons.size(); i++) {
			if (cauldrons.get(i).isUsable()) {
				return cauldrons.get(i);
			}
		}
		return null;
	}
}
