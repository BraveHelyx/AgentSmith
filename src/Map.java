
/*
 * Written by James Virtue, modified by Bruce Hely, 2015
 * 
 * Map consisting of a character array of each possible item.
 * Unknown elements are denoted as ` to start with.
 * 
 * The initial Map is updated with all squares in the view.
 * Future updates will only need to update the first 5 in 'front' of the agent.
 * 
 * @param itemMap		2 dimensional array of Node objects storing node information
 * 
 * 
 */
public class Map {
	
	private Node[][] itemMap;
	private static int MAXEDGE = 80;
	private boolean initFlag;
	private BountyPoint coordinateOffset;
	private int facing;
	
	/*
	 * Constructor for map
	 */
	public Map() {
		init();
	}
	
	/*
	 * Initialises the Map
	 */
	public void init() {
		itemMap = new Node[MAXEDGE][MAXEDGE];
		coordinateOffset = new BountyPoint(MAXEDGE/2,MAXEDGE/2);
		facing = 0;
		initFlag = false;
		
		//Fills nodes with undiscovered symbol '`'
		for(int i = 0; i < MAXEDGE; i++) {
			for(int j = 0; j < MAXEDGE; j++) {
				itemMap[i][j] = new Node(j, i, '`', 0, 0);
			}
		}
		
	}
	
	public Node[][] getMap() {
		return itemMap;
	}
	
	/*
	 * Method that processes the very first view.
	 */
	private void initUpdate(char[][] view) {
		int x;
		int y;
		
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				x = coordinateOffset.getX() + i - 2; // correct for global map
				y = coordinateOffset.getY() + j - 2;
				if((i == 2) && (j == 2)){
					itemMap[x][y].setItem('^'); // put the agent icon in the middle
				} else {
					itemMap[x][y].setItem(view[i][j]);
				}
			}
		}
	}
	
	/*
	 * Updates the local memory map with a new view window
	 */
	public void update(char[][] view) {
		
		//Run once to initialise the 5x5 cell	
		if(initFlag == false){
			initUpdate(view);
			initFlag = true;
		} else {
			//Runs all other times, either updating a new row of 5 cells, or not updating at all
			
		}
	}
	
	/*
	 * TODO Write functions that update localX and localY based on the direction the agent is facing.
	 */
	
	public void printMap() {
		for(int i = 0; i < MAXEDGE; i++) {
			for(int j = 0; j < MAXEDGE; j++) {
				System.out.print(itemMap[i][j].getItem());
			}
			System.out.print('\n');
		}
	}
}
