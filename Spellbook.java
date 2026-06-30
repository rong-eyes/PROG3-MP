package MCO1;
import java.util.ArrayList;

public class Spellbook {
	private ArrayList<Recipe> unlockedRecipes;
	
	public Spellbook() {
		//add default recipes
	}

	public ArrayList<Recipe> getUnlockedRecipes() {
		return unlockedRecipes;
	}

	public void setUnlockedRecipes(ArrayList<Recipe> unlockedRecipes) {
		this.unlockedRecipes = unlockedRecipes;
	}
	
	public void sortRecipes() { //insertion sort
								//must be called right after a recipe is unlocked
		   int i, j;
		    for (i = 1; i < unlockedRecipes.size(); i++) {
		        Recipe tmp = unlockedRecipes.get(i);
		        j = i;
		        while ((j > 0) && (unlockedRecipes.get(j - 1).getConcoctionID() > tmp.getConcoctionID())) {
		        	unlockedRecipes.set(j, unlockedRecipes.get(j - 1));
		            j--;
		        }
		        unlockedRecipes.set(j, tmp);
		    }
	}
	
	public Recipe getRecipe(int id) { //for recipe mode
		int lo = 0;
		int hi = unlockedRecipes.size() - 1;
		
		while(lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			
			if(id == unlockedRecipes.get(mid).getConcoctionID()) {
				return unlockedRecipes.get(mid);
			}else if(id > unlockedRecipes.get(mid).getConcoctionID()) {
				lo = mid + 1;
			}else
				hi = mid -1;
		}
		
		return null;  //recipe not found
	}
	
	public void printRecipes() { //might need to be modified once GUI is involved
		for(int i = 0; i < unlockedRecipes.size(); i++) {
			System.out.println(unlockedRecipes.get(i).getConcoctionID() + unlockedRecipes.get(i).getName() + "\t" + unlockedRecipes.get(i).getPrice());
			System.out.println("\t" + unlockedRecipes.get(i).getConcoctionBase() + "\n");
			for(int j = 0; j < unlockedRecipes.get(i).getIngredients().size(); j++) {
				System.out.print("\t" + unlockedRecipes.get(i).getIngredients().get(j).getName() + " ");
			}
		}
	}
	
	public void addRecipe(Recipe recipe) {
		unlockedRecipes.add(recipe);
		sortRecipes();
	}
}
