
/*
 * Written by James Virtue, modified by Bruce Hely, 2015
 * 
 * Map consisting of a character array of each possible item.
 * Unknown elements are denoted as ` to start with.
 * 
 * The initial Map is populated with ` characters.
 * On the first update, all values in view are placed on the map.
 * Subsequent updates only update the first 5 elements in front of the agent
 *  as well as directly in front (in case an item is used),
 *  and directly behind (to clear last agent position or set down boat).
 *   
 */
public class Map {
	
	private Node itemMap[][];
	public static int MAXEDGE = 80;
	private int posX;
	private int posY;
	private int facing;
	
	//Flags
	private boolean initUpdate;
	
	public Map() {
		init();
		initUpdate = false;
	}
	
	public void init() {
		
		//Sets the agent's initial position
		posX = MAXEDGE/2;
		posY = MAXEDGE/2;
		
		itemMap = new Node[MAXEDGE][MAXEDGE];
		
		//Initialise the map with nodes containing undiscovered symbols
		for(int j = 0; j < MAXEDGE; j++) {
			for(int i = 0; i < MAXEDGE; i++) {
				itemMap[i][j] = new Node(i, j, '`', 0, 0);
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
		int x, y;

		for(int j = 0; j < 5; j++) {
			y = posY- 2 + j;
			for(int i = 0; i < 5; i++) {
				x = posX - 2 + i;
				if((i == 2) && (j == 2)){
					itemMap[x][y].setItem('^');			// put the agent icon in the middle
				} else {
					itemMap[x][y].setItem(view[j][i]);	// view provides row, col. -> change to col, row for x, y.
				}
			}
		}
	}
	
	public void update(char[][] view, Compass compass) {
		if(!initUpdate) {	//Run Once
			initUpdate(view);
			compass.setAgentPosition(posX, posY);
			initUpdate = true;
		} else {
			//Get Agent direction
			facing = compass.getAgentDirection();
			
			//Get agent coordinate
			posX = compass.getAgentPosition().getX();
			posY = compass.getAgentPosition().getY();
			int x, y;
			
			//Decide where to update
			switch(facing){
			case 0:		//NORTH
				for(int i = 0; i < 5; i++) {
					x = posX - 2 + i;
					y = posY - 2;
					itemMap[x][y].setItem(view[0][i]);
				}
				
				itemMap[posX][posY-1].setItem(view[1][2]);	// update directly in front
				itemMap[posX][posY].setItem('^');			// set agent position
				itemMap[posX][posY+1].setItem(view[3][2]);	// update directly behind
				break;
				
			case 1:		// EAST
				for(int j = 0; j < 5; j++) {
					x = posX + 2;
					y = posY - 2 + j;
					itemMap[x][y].setItem(view[0][j]);
				}
				
				itemMap[posX+1][posY].setItem(view[1][2]);	// update directly in front
				itemMap[posX][posY].setItem('>');			// set agent position
				itemMap[posX-1][posY].setItem(view[3][2]);	// update directly behind
				break;
				
			case 2:		// SOUTH
				for(int i = 0; i < 5; i++) {
					x = posX + 2 - i;
					y = posY + 2;
					itemMap[x][y].setItem(view[0][i]);
				}
				
				itemMap[posX][posY+1].setItem(view[1][2]);	// update directly in front
				itemMap[posX][posY].setItem('v');			// set agent position
				itemMap[posX][posY-1].setItem(view[3][2]);	// update directly behind
				break;
			
			case 3:		// WEST
				for(int j = 0; j < 5; j++) {
					x = posX - 2;
					y = posY + 2 - j;
					itemMap[x][y].setItem(view[0][j]);
				}
				
				itemMap[posX-1][posY].setItem(view[1][2]);	// update directly in front
				itemMap[posX][posY].setItem('<');			// set agent position
				itemMap[posX+1][posY].setItem(view[3][2]);	// update directly behind
				break;
			}
		}
	}
	
	public void printMap() {
		for(int j = 0; j < MAXEDGE; j++) {
			for(int i = 0; i < MAXEDGE; i++) {
				System.out.print(itemMap[i][j].getItem());
			}
			System.out.print('\n');
		}
	}
}
