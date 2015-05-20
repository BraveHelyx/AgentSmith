
/*
 * Class that handles an Agent's inventory at any one time
 * 
 * @precondition	 	numDynamite > 0
 */
public class Inventory {
	private boolean axe;
	private boolean gold;
	
	private int numDynamite;	

	public Inventory(){
		axe = false;
		gold = false;
		
		numDynamite = 0;
	}
	
	public boolean haveAxe(){
		return axe;
	}
	
	public void obtainedAxe(){
		axe = true;
	}
	
	public boolean haveGold(){
		return gold;
	}
	
	public void obtainedGold(){
		gold = true;
	}
	
	public void addDynamite(){
		numDynamite++;
	}
	
	/*
	 * Method that lazily removes dynamite. Checks must be performed prior to this.
	 * @precondition numDynamite must be > 0
	 */
	public Boolean hasDynamite(){
		if(numDynamite > 0){
			return true;
		}
		return false;
	}
	
	
	
}


