package MCO1;
import java.util.Scanner;
import java.util.ArrayList;

public class Player {
	private final String playerName;
	private Inventory inventory;
	private int crystals;
	private Spellbook spellbook;
	
	public Player(String name) { //for new game instantiation
		playerName = name;
		inventory = new Inventory();
		spellbook = new Spellbook();
	}
	
	public Player(String name, Inventory inventory, int crystals, Spellbook sb) { //for file loading
		playerName = name;
		this.inventory = inventory;
		this.crystals = crystals;
		spellbook = sb;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public int getCrystals() {
		return crystals;
	}

	public void setCrystals(int crystals) {
		this.crystals = crystals;
	}

	public Spellbook getSpellbook() {
		return spellbook;
	}

	public void setSpellbook(Spellbook spellbook) {
		this.spellbook = spellbook;
	}

	public String getPlayerName() {
		return playerName;
	}
	
	public void BrewConcoction(boolean isCreative, Cauldron cauldron, Scanner s, ArrayList<Recipe> Recipes) { //this is what will be called in the menu option
		//make sure to have a check beforehand if it is usable
		//boolean isCreative decides what mode; false = recipe mode, true = creative
		int opt = 0;
		int tmp;
		
		if(isCreative) {
			boolean isCancel = false;
			char var = ' ';
			boolean selectedBase = false;

			while(!isCancel && !selectedBase) {
				//Base Selection
				System.out.println("Choose a Base: 1. SYRUP  2. BUBBLE  3. PERFUME  4. MILK  5. LOTION");
				while(opt < 1 || opt >= 6) {
					try {
						opt = s.nextInt();
						switch(opt) {
						case 1:
							tmp = inventory.isInInventory("Syrup Base", inventory.getBases()); //returns index of where it is in the ArrayList
							if(tmp > -1)
								cauldron.addBase(inventory.getBases().get(tmp).getName(), inventory);
							selectedBase = true;
							break;
						case 2:
							tmp = inventory.isInInventory("Bubble Base", inventory.getBases()); //returns index of where it is in the ArrayList
							if(tmp > -1)
								cauldron.addBase(inventory.getBases().get(tmp).getName(), inventory);
							selectedBase = true;
							break;
						case 3:
							tmp = inventory.isInInventory("Perfume Base", inventory.getBases()); //returns index of where it is in the ArrayList
							if(tmp > -1)
								cauldron.addBase(inventory.getBases().get(tmp).getName(), inventory);
							selectedBase = true;
							break;
						case 4:
							tmp = inventory.isInInventory("Milk Base", inventory.getBases()); //returns index of where it is in the ArrayList
							if(tmp > -1)
								cauldron.addBase(inventory.getBases().get(tmp).getName(), inventory);
							selectedBase = true;
							break;
						case 5:
							tmp = inventory.isInInventory("Lotion Base", inventory.getBases()); //returns index of where it is in the ArrayList
							if(tmp > -1)
								cauldron.addBase(inventory.getBases().get(tmp).getName(), inventory);
							selectedBase = true;
							break;
						default:
							System.out.println("Invalid Input. Try Again.");
						}
					}catch (Exception e){
						System.out.println("Invalid Input.");
					}
				}
				
				while(var != 'Y' && var != 'N') {
					System.out.println("Cancel Brew (Y/N)?: ");
					var = s.next().charAt(0);
					if(var == 'Y')
						isCancel = true;
					else if (var != 'N' && var != 'Y')
						System.out.println("Invalid Input");
						
				}
			}
			
			if(isCancel) {
				inventory.addInventory(cauldron.getConcoctionBase(), 1);
			}
			
			char again = 'Y';
			boolean done = false;
			while(!isCancel && again != 'N' && !done) {
				//Ingredients Selection
				
				//BIG BUG DISPLAYS DOES NOT HAVE INGREDIENT EVERY TIME DESPITE HAVING STOCK 
				
				System.out.println("Choose an Ingredient: (No Duplicates!)");
				System.out.println("1. STRAWBERRY  2. ORANGE  3. LEMON  4. BANANA  5. MANGO\n"
						         + "6. PINEAPPLE  7. KIWI  8. BLUEBERRY  9. COCONUT");
				boolean added = false;
				opt = 0;
				while((opt < 1 || opt >= 10) && !added && cauldron.getIngredients().size() != 3) {
					opt = 0;
					try {
						opt = s.nextInt();
						switch(opt) {
						case 1:
							tmp = inventory.isInInventory("Strawberry", inventory.getIngredients()); //returns index of where it is in the ArrayList
							if(tmp > -1) {
								cauldron.addIngredients(inventory.getIngredients().get(tmp), inventory);
								added = true;
							} else 
								System.out.println("You don't have this ingredient.");
							break;
						case 2:
							tmp = inventory.isInInventory("Orange", inventory.getIngredients()); //returns index of where it is in the ArrayList
							if(tmp > -1) {
								cauldron.addIngredients(inventory.getIngredients().get(tmp), inventory);
								added = true;
							} else 
								System.out.println("You don't have this ingredient.");
							break;
						case 3:
							tmp = inventory.isInInventory("Lemon", inventory.getIngredients()); //returns index of where it is in the ArrayList
							if(tmp > -1) {
								cauldron.addIngredients(inventory.getIngredients().get(tmp), inventory);
								added = true;
							} else 
								System.out.println("You don't have this ingredient.");
							break;
						case 4:
							tmp = inventory.isInInventory("Banana", inventory.getIngredients()); //returns index of where it is in the ArrayList
							if(tmp > -1) {
								cauldron.addIngredients(inventory.getIngredients().get(tmp), inventory);
								added = true;
							} else 
								System.out.println("You don't have this ingredient.");
							break;
						case 5:
							tmp = inventory.isInInventory("Mango", inventory.getIngredients()); //returns index of where it is in the ArrayList
							if(tmp > -1) {
								cauldron.addIngredients(inventory.getIngredients().get(tmp), inventory);
								added = true;
							} else 
								System.out.println("You don't have this ingredient.");
							break;
						case 6:
							tmp = inventory.isInInventory("Pineapple", inventory.getIngredients()); //returns index of where it is in the ArrayList
							if(tmp > -1) {
								cauldron.addIngredients(inventory.getIngredients().get(tmp), inventory);
								added = true;
							} else 
								System.out.println("You don't have this ingredient.");
							break;
						case 7:
							tmp = inventory.isInInventory("Kiwi", inventory.getIngredients()); //returns index of where it is in the ArrayList
							if(tmp > -1) {
								cauldron.addIngredients(inventory.getIngredients().get(tmp), inventory);
								added = true;
							} else 
								System.out.println("You don't have this ingredient.");
							break;
						case 8:
							tmp = inventory.isInInventory("Blueberry", inventory.getIngredients()); //returns index of where it is in the ArrayList
							if(tmp > -1) {
								cauldron.addIngredients(inventory.getIngredients().get(tmp), inventory);
								added = true;
							} else 
								System.out.println("You don't have this ingredient.");
							break;
						case 9:
							tmp = inventory.isInInventory("Coconut", inventory.getIngredients()); //returns index of where it is in the ArrayList
							if(tmp > -1) {
								cauldron.addIngredients(inventory.getIngredients().get(tmp), inventory);
								added = true;
							} else 
								System.out.println("You don't have this ingredient.");
							break;
						default:
							System.out.println("Invalid Input. Try Again.");
							break;
						}
					}catch (Exception e){
						System.out.println("Invalid Input.");
					}
					
					if(cauldron.getIngredients().size() == 3) {
						System.out.println("Cauldron Full!");
						again = 'N';
					}else if (added) {
						System.out.println("Continue Adding Ingredients? (Y/N)");
		                again = s.next().charAt(0);
					}
				}
				if (again == 'N' || !added) {
					var = ' ';
					while(var != 'Y' && var != 'N') {
						System.out.println("Cancel Brew (Y/N)?: ");
						var = s.next().charAt(0);
						if(var == 'Y') {
							isCancel = true;	
							done = true;
						}
						else if (var != 'N' && var != 'Y'){
							System.out.println("Invalid Input");
						}
					}
				}
			}
			
			if(isCancel) {
				for(int i = 0; i < cauldron.getIngredients().size(); i++) {
					inventory.addInventory(cauldron.getIngredients().get(i), 1);
				}
			}
			
			if(!isCancel) {
			System.out.println("Confirm Brew (Y/N)?: ");
			var = ' ';
				while(var != 'Y' && var != 'N') {
					var = s.next().charAt(0);
					if(var == 'N') {
						inventory.addInventory(cauldron.getConcoctionBase(), 1);
						for(int i = 0; i < cauldron.getIngredients().size(); i++) {
							inventory.addInventory(cauldron.getIngredients().get(i), 1);
						}
					} else if(var == 'Y') {
						//ADD A CODE THAT CHECKS FOR FORMULA VALIDITY
						
						if(cauldron.validBrew(Recipes) != null) {
							Recipe temp = cauldron.validBrew(Recipes);
							if(spellbook.getRecipe(temp.getConcoctionID()) == null) {
								spellbook.addRecipe(temp);
								System.out.println(temp.getName() + " Unlocked!");
							}
							setCrystals(getCrystals() + temp.getPrice());
							System.out.println(temp.getName() + "successfully brewed! + " + temp.getPrice() + "crystals");
						}else {
							System.out.println("Oh no! Alchemy failed!");
							cauldron.setUsable(false);
						}
						cauldron.cauldronFlush();
					}
				}
			} 
			
			
		}
			else { //RECIPE MODE 
			Recipe temp;
			spellbook.printRecipes();
			System.out.println("Enter the Recipe ID to Brew:");
			while(spellbook.getRecipe(opt) == null) {
				opt = s.nextInt();
				if(spellbook.getRecipe(opt) != null) {
					temp = spellbook.getRecipe(opt);
					cauldron.setConcoctionBase(temp.getConcoctionBase());
					cauldron.setIngredients(temp.getIngredients());
					inventory.removeInventory(temp.getConcoctionBase(), 1);
					for(int i = 0; i < temp.getIngredients().size(); i++) {
						inventory.removeInventory(temp.getIngredients().get(i), 1);
					}
					
					setCrystals(getCrystals() + temp.getPrice());
					System.out.println(temp.getName() + "successfully brewed! + " + temp.getPrice() + "crystals");
				}else {
					System.out.println("Invalid Input!");
				}
			}
		}
	}
}
