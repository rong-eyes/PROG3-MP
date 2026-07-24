package MCO1;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	private static final String COMPENDIUM_PATH = "POTION COMPENDIUM.csv";

	/**
	* Constructor for Main; initializes the needed info for the game to run (such as loading recipes and player)
	*
	*/
	private Main() {
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("=====================================");
		System.out.println("        POTION PRODIGY ");
		System.out.println("=====================================");

		ArrayList<Recipe> allRecipes = RecipeLoader.loadRecipes(COMPENDIUM_PATH);

		Player player = boot(sc, allRecipes);
		if (player != null) {
			runMainMenu(player, sc, allRecipes);
		}
		sc.close();
	}

	/**
	* Handles the initialization of a player profile.
	* <p>
	* 'New Game' creates a new player profile (.txt file) with the starter items and recipes. 'Load Game' reads from a previously saved player profile (.txt file) and loads the
	* parsed info onto the player object of the game. If the user starts a new game with the same name as a previous save, the program will first confirm before overwriting. It returns 
	* the use back to the main menu if they decide not to confirm overwriting.
	* </p>
	*
	* @param sc the Scanner being read from
	* @param allRecipes the list of all the unlockable (valid) Recipes in the game
	* @return the Player object with the proper info loaded onto
	*/
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
				// Warn up front when the name would overwrite an existing save (spec p.1 1.c.i).
				boolean declined = false;
				if (SaveManager.saveExists(name)) {
					Boolean cont = promptYesNo(sc, "A save file named \"" + name
							+ ".txt\" already exists. Starting a new game will overwrite it"
							+ " when you save. Continue as \"" + name + "\"? (Y/N): ");
					if (cont == null) {
						return null;
					}
					if (!cont) {
						// Declined: back to the start menu to pick another name or load the save.
						System.out.println("Returning to the start menu.");
						declined = true;
					}
				}
				if (!declined) {
					Player player = new Player(name, allRecipes);
					System.out.println("Welcome, " + name + "! Your alchemy adventure begins.");
					return player;
				}
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

	/**
	* Prompts the user to input a name for the player profile
	*
	* @param sc the Scanner being read from
	* @return the string of the player inputted name
	*/
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

	/**
	* Prompts the user to input Y (Yes) or N (No); converts the user input into a boolean value
	*
	* @param sc the Scanner being read from
	* @param prompt the message or prompt that the player answers to
	* @return the true for 'Yes' and false for 'No'
	*/
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

	/**
	* Runs the 'Main Menu' of the game where the player can choose what action to do
	* <ol>
	*	<li> Brew Concoction to allow the player brew a potion in either creative or recipe mode. </li>
	* 	<li> Check Inventory to view the items that the player has and how much </li>
	*	<li> Check Spellbook to view the unlocked recipes </li>
	*	<li> Visit Market to allow the playe to buy and sell ingredients and buy cauldrons </li>
	* 	<li> Bless Cauldron to make an unusable cauldron usable again, if any </li>
	*	<li> Login Bonus to collect the Login Bonus (a random item to be rewarded and placed in the player inventory every login) if applicable </li>
	* 	<li> Exit Game to terminate the game session </li>
	* </ol>
	*
	* @param player the player profile instantiated at the start of the game
	* @param sc the Scanner being read from
	* @param allRecipes the list of valid recipes to be used as a reference list throughout the entire session
	*/
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
			} else {
				String line = sc.nextLine().trim();
				if (line.isEmpty()) {
					System.out.println("Invalid choice. Please enter a number from 1 to 7.");
				} else {
					int choice = 0;
					boolean valid = true;
					try {
						choice = Integer.parseInt(line);
					} catch (NumberFormatException e) {
						System.out.println("Invalid input. Please enter a number from 1 to 7.");
						valid = false;
					}
					if (valid) {
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
			}
		}
	}

	/**
	* Runs the 'Brew Concoction' action of the game where the player can brew a potion in creative or recipe mode
	* <ol>
	*	<li> Recipe Mode: Lets player choose among their unlocked recipes what to brew </li>
	* 	<li> Creative Mode: Lets the player play around different combinations to discover recipes </li>
	* 	<li> Cancel (0): Lets the player go back to Main menu </li>
	* </ol>
	*
	* @param player the player profile instantiated at the start of the game
	* @param sc the Scanner being read from
	* @param allRecipes the list of valid recipes to be used as a reference list throughout the entire session
	* @param market the market, for tracking brew progress for the market refresh quota
	*/
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
			} else {
				int mode = 0;
				boolean valid = true;
				try {
					mode = Integer.parseInt(line);
				} catch (NumberFormatException e) {
					System.out.println("Invalid input. Enter 1, 2, or 0.");
					valid = false;
				}
				if (valid) {
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
						} else {
							player.BrewConcoction(true, firstUsable(cauldrons), sc, allRecipes, market);
							return;
						}
					} else {
						System.out.println("Invalid choice. Enter 1, 2, or 0.");
					}
				}
			}
		}
	}

	/**
	* Runs the 'Bless Cauldron' action of the game where the player can bless a cauldron to make it usable again.
	* <p>
	* Only blesses one cauldron at a time and blesses the first cauldron that is unusable in the list that the iterator encounters.
	* </p>
	*
	* @param player the player profile instantiated at the start of the game
	* @param sc the Scanner being read from
	*/
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

	/**
	* Counts the amount of usable cauldrons that player possesses.
	*
	* @param cauldrons the list of cauldrons that the player posseses.
	* @return the computed total quantity of usable cauldrons in the players possession.
	*/
	private static int countUsable(ArrayList<Cauldron> cauldrons) {
		int usable = 0;
		for (int i = 0; i < cauldrons.size(); i++) {
			if (cauldrons.get(i).isUsable()) {
				usable++;
			}
		}
		return usable;
	}

	/**
	* Iterates through the players list of cauldrons to search for the first usable cauldron
	*
	* @param cauldrons the list of cauldrons that the player posseses.
	* @return the first usable cauldron in the players list of cauldrons; null if none are usable
	*/
	private static Cauldron firstUsable(ArrayList<Cauldron> cauldrons) {
		for (int i = 0; i < cauldrons.size(); i++) {
			if (cauldrons.get(i).isUsable()) {
				return cauldrons.get(i);
			}
		}
		return null;
	}
}
