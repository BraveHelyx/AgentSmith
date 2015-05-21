
/*
 * Map consisting of a character array of each possible item.
 * Unknown elements are denoted as ` to start with.
 * 
 * The initial Map is updated with all squares in the view.
 * Future updates will only need to update the first 5 in 'front' of the agent.
 * 
 */
public class Map {
	
	private char itemMap[][];
	private static int MAXEDGE = 80;
	private int localX;
	private int localY;
	private int facing;
	
	public Map(char[][] initView) {
		init(initView);
	}
	
	public void init(char[][] initView) {
		localX = 0;
		localY = 0;
		
		itemMap = new char[MAXEDGE][MAXEDGE];
		
		for(int i = 0; i < MAXEDGE; i++) {
			for(int j = 0; j < MAXEDGE; j++) {
				itemMap[i][j] = '`';
			}
		}
		
		initUpdate(initView);
	}
	
	public char[][] getMap() {
		return itemMap;
	}
	
	private void initUpdate(char[][] view) {
		int x;
		int y;
		
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				x = localX + i + (MAXEDGE/2) - 2; // correct for global map
				y = localY + j + (MAXEDGE/2) - 2;
				if((i == 2) && (j == 2)){
					itemMap[x][y] = '^'; // put the agent icon in the middle
				} else {
					itemMap[x][y] = view[i][j];
				}
			}
		}
	}
	
	private void update(char[][] view) {
		/*
		 * TODO Write a function that updates the map based on the direction the agent is facing.
		 */
	}
	
	/*
	 * TODO Write functions that update localX and localY based on the direction the agent is facing.
	 */
	
	public void printMap() {
		for(int i = 0; i < MAXEDGE; i++) {
			for(int j = 0; j < MAXEDGE; j++) {
				System.out.print(itemMap[i][j]);
			}
			System.out.print('\n');
		}
	}
}
