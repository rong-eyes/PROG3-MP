package MCO1;
import java.util.Scanner;
import java.util.Random;

public class Market {
	private String[] items = {"Strawberry", "Orange", "Lemon", "Banana", "Mango", "Pineapple", "Kiwi", "Blueberry", "Coconut", //15 items
							  "Syrup Base", "Bubble Base", "Perfume Base", "Milk Base", "Lotion Base", "Cauldron"};
	private int[] buyPrices = {125, 80, 50, 75, 90, 240, 200, 120, 180, 50, 80, 250, 60, 150, 3000};
	private int[] sellPrices = {25, 40, 25, 50, 30, 120, 80, 20, 90, 10, 20, 50, 15, 25, 0};
	private Random randomizer;
	int[][] itemSlots = new int[8][8]; //the item slots in the market
	int maxQty; //max quantity per item
	
	public Market() {
		this.randomizer = new Random();
		maxQty = 5;
	}

	public String[] getItems() {
		return items;
	}

	public void setItems(String[] items) {
		this.items = items;
	}

	public Random getRandomizer() {
		return randomizer;
	}

	public void setRandomizer(Random randomizer) {
		this.randomizer = randomizer;
	}

	public int[][] getItemSlots() {
		return itemSlots;
	}

	public void setItemSlots(int[][] randomItem) {
		this.itemSlots = randomItem;
	}
	
	public void initializeItems() { //call before displaying array
		int min = 0, max = 14; //indices of the array
		int temp = randomizer.nextInt(max - min + 1) + min; // randomizer.nextInt(max);
		
		for(int i = 0; i < itemSlots.length; i++) {
			if(temp == 14) {
				itemSlots[0][i] = temp;
				itemSlots[1][i] = 1;
				temp = randomizer.nextInt((max - 1) - min + 1) + min; //prevents the cauldron from being picked again
			}else if(temp >= 0 && temp <= 13){
				itemSlots[0][i] = temp;
				itemSlots[1][i] = randomizer.nextInt(maxQty - min + 1) + min;
				temp = randomizer.nextInt(max);
			}
		}
	}
	
	public void buyItems(int item, Player player, Scanner s) {
		if(itemSlots[0][item] >= 0 && itemSlots[0][item] < 8) {
			int qty = 0;
			System.out.println("Quantity to buy: ");

			if(player.getCrystals() >= buyPrices[item]) {
				Ingredient ingredient = new Ingredient((items[itemSlots[0][item]]), itemSlots[1][item]);
				while(qty > ingredient.getQuantity() && (qty * buyPrices[item]) > player.getCrystals()) {
					qty = s.nextInt();
					if((qty * buyPrices[item]) > player.getCrystals())
						System.out.println("Insufficient crystals!."); //note: this should be a slider in GUI like skyrim lol
				}
				player.getInventory().addInventory(ingredient, qty);
				player.setCrystals(player.getCrystals() - (buyPrices[item]) * qty);
			}else
				System.out.println("Insufficient crystals!");
			
		}else if(itemSlots[0][item] >= 9 && itemSlots[0][item] < 13) {
			int qty = 0;
			System.out.println("Quantity to buy: ");

			if(player.getCrystals() >= buyPrices[item]) {
				Base base = new Base((items[itemSlots[0][item]]), itemSlots[1][item]);
				while(qty > base.getQuantity() && (qty * buyPrices[item]) > player.getCrystals()) {
					qty = s.nextInt();
					if((qty * buyPrices[item]) > player.getCrystals())
						System.out.println("Insufficient crystals!."); //note: this should be a slider in GUI like skyrim lol
				}
				player.getInventory().addInventory(base, qty);
				player.setCrystals(player.getCrystals() - (buyPrices[item]) * qty);
			}else
				System.out.println("Insufficient crystals!");
		}else {
			if(player.getCrystals() >= buyPrices[item]) {
				player.getInventory().addCauldron();
			player.setCrystals(player.getCrystals() - buyPrices[item]);
			}else 
				System.out.println("Insufficient crystals!");
		}
		
		System.out.println("You have: " + player.getCrystals() + " crystals left");
	}
	
	public void sellItems(int item, Player player, Scanner s) {
		int index = -1;
		if(itemSlots[0][item] >= 0 && itemSlots[0][item] < 8) {
			index = player.getInventory().isInInventory(items[itemSlots[0][item]], player.getInventory().getIngredients());
			if(index > -1) {
				int qty = 0;
				System.out.println("Quantity to sell: ");
				
				while(qty > player.getInventory().getIngredients().get(index).getQuantity()) {
					qty = s.nextInt();
					if(qty > player.getInventory().getIngredients().get(index).getQuantity())
						System.out.println("You do not have that much."); //note: this should be a slider in GUI like skyrim lol
				}
				
				player.getInventory().removeInventory(player.getInventory().getIngredients().get(index), qty);
				player.setCrystals(player.getCrystals() + (sellPrices[item] * qty));
			}else
				System.out.println("You don't have this!");
			
		}else if(itemSlots[0][item] >= 9 && itemSlots[0][item] < 13) {
			index = player.getInventory().isInInventory(items[itemSlots[0][item]], player.getInventory().getBases());
			if(index > -1) {
				int qty = 0;
				System.out.println("Quantity to sell: ");
				
				while(qty > player.getInventory().getBases().get(index).getQuantity()) {
					qty = s.nextInt();
					if(qty > player.getInventory().getBases().get(index).getQuantity())
						System.out.println("You do not have that much."); //note: this should be a slider in GUI like skyrim lol
				}
				
				player.getInventory().removeInventory(player.getInventory().getBases().get(index), qty);
				player.setCrystals(player.getCrystals() + (sellPrices[item] * qty));
			}else
				System.out.println("You don't have this!");
		}
		
		System.out.println("You now have: " + player.getCrystals() + " crystals");
	}
	
	
	public void marketMain(Player player, Scanner s) { //preferably in a while loop? so that every after transaction 
		int opt = 0, choice = 0;
		char option = 'Y';
		System.out.println("=====================================================================\n"
						 + "/								                                    /\n"
						 + "/                       WELCOME TO THE MARKET!			            /\n"
						 + "/								                                    /\n"
						 + "=====================================================================\n");
		System.out.println("|----------------|----------------|----------------|----------------|");
		System.out.printf ("|%s%2dx|%s%2dx|%s%2dx|%s%2dx|\n", items[itemSlots[0][0]], itemSlots[1][0], items[itemSlots[0][1]], itemSlots[1][1], items[itemSlots[0][2]], itemSlots[1][2], items[itemSlots[0][3]], itemSlots[1][3]);
		System.out.printf ("|%s%2dx|%s%2dx|%s%2dx|%s%2dx|\n", items[itemSlots[0][4]], itemSlots[1][4], items[itemSlots[0][5]], itemSlots[1][5], items[itemSlots[0][6]], itemSlots[1][6], items[itemSlots[0][7]], itemSlots[1][7]);
		System.out.println("|----------------|----------------|----------------|----------------|");
		System.out.println("=====================================================================");
		System.out.println("What would you like to do?");
		System.out.println("1. BUY  2. SELL  3.EXIT");
		while(opt != 3) {
			try {
				opt = s.nextInt();
				switch(opt) {
				case 1:
					while(option != 'N') {
						System.out.println("Pick an item to buy: ");
						while(choice >= 8 && choice < 1) {
							choice = s.nextInt() - 1;
							if(choice >= 8 && choice < 1)
								System.out.println("Invalid Input!");
						}
						this.buyItems(opt, player, s);
						System.out.println("Continue buying (Y/N)? ");
						option = s.next().charAt(0);
					}
					break;
				case 2:
					while(option != 'N') {
						player.getInventory().displaySellables();
						System.out.println("Pick an item to sell: ");
						while(choice >= 8 && choice < 1) {
							choice = s.nextInt() - 1;
							if(choice >= 8 && choice < 1)
								System.out.println("Invalid Input!");
						}
						this.buyItems(opt, player, s);
						System.out.println("Continue buying (Y/N)? ");
						option = s.next().charAt(0);
					}
					break;
				default:
					break;
				}
			}catch (Exception e){
				System.out.println("Invalid Input!");
			}
		}
		
	}

}
