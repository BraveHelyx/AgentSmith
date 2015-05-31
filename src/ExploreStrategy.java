
public class ExploreStrategy implements Strategy{
	
	private Map localMap;
	private Node[][] itemMap;
	private int posX;
	private int posY;
	private int facing;
	private String moves;
	
	@Override
	public String decideMove(Map map) {
		localMap = map;
		itemMap = map.getMap();
		posX = map.getPosX();
		posY = map.getPosY();
		facing = map.getFacing();
		
		doSearch();
		
		return moves;
	}
	
	/**
	 * Does a search in each direction for the closest unknown tile and finds the minimum value.
	 * It then sets moves to the appropriate agent moves. 
	 */
	private void doSearch() {
		
		int distance = 199;
		int direction = 0;
		int north = distanceToUnknown(Compass.NORTH);
		int east = distanceToUnknown(Compass.EAST);
		int south = distanceToUnknown(Compass.SOUTH);
		int west = distanceToUnknown(Compass.WEST);
		
		// find minimum distance and set direction to go in.
		if(north < distance) {
			distance = north;
			direction = Compass.NORTH;
		} else if(east < distance) {
			distance = east;
			direction = Compass.EAST;
		} else if(south < distance) {
			distance = south;
			direction = Compass.SOUTH;
		} else if(west < distance) {
			distance = west;
			direction = Compass.WEST;
		}
		
		
		// set moves to go in the right direction
		switch(facing) {
		case 0:
			if(direction == 0) {
				moves = "F";
			} else if(direction == 1) {
				moves = "R";
			} else if(direction == 2) {
				moves = "RR";
			} else if(direction == 3) {
				moves = "L";
			}
			break;
			
		case 1:
			if(direction == 1) {
				moves = "F";
			} else if(direction == 2) {
				moves = "R";
			} else if(direction == 3) {
				moves = "RR";
			} else if(direction == 0) {
				moves = "L";
			}
			break;
			
		case 2:
			if(direction == 2) {
				moves = "F";
			} else if(direction == 3) {
				moves = "R";
			} else if(direction == 0) {
				moves = "RR";
			} else if(direction == 1) {
				moves = "L";
			}
			break;
		
		case 3:
			if(direction == 3) {
				moves = "F";
			} else if(direction == 0) {
				moves = "R";
			} else if(direction == 1) {
				moves = "RR";
			} else if(direction == 2) {
				moves = "L";
			}
			break;
		}		
	}
	
	
	/**
	 * Checks nodes along a compass direction until it finds an unknown and returns the number of
	 * spaces in between. Stops check if node is an obstacle.
	 * 
	 * @param direction the direction to check in
	 * 
	 * @return the number of numSpaces to the nearest unknown or a very large number if obstacle
	 */
	public int distanceToUnknown(int direction) {
		
		int numSpaces = 0;
		int x = posX;
		int y = posY;
		
		switch(direction) {
		case 0:
			while(y > 0) {
				y -= 1;
				numSpaces += 1;
				
				if(itemMap[x][y].isObstacle()) {
					if(itemMap[x][y-1].getItem() == '`') {
						return numSpaces + 1;
					} else {
						return 200; // Arbitrarily large number
					}
				}
				
				for(int i = -2; i < 2; i++) {
					if(itemMap[x+i][y].getItem() == '`') {
						return numSpaces;
					}
				}
			}
			return 200;
			
		case 1:
			while(x < localMap.getMaxEdge()) {
				x += 1;
				numSpaces += 1;
				
				if(itemMap[x][y].isObstacle()) {
					if(itemMap[x+1][y].getItem() == '`') {
						return numSpaces + 1;
					} else {
						return 200; // Arbitrarily large number				
					}
				}
				
				for(int i = -2; i < 2; i++) {
					if(itemMap[x][y+i].getItem() == '`') {
						return numSpaces;
					}
				}
			}
			return 200;
			
		case 2:
			while(y < localMap.getMaxEdge()) {
				y += 1;
				numSpaces += 1;
				
				if(itemMap[x][y].isObstacle()) {
					if(itemMap[y+1][x].getItem() == '`') {
						return numSpaces + 1;
					} else {
						return 200; // Arbitrarily large number	
					}			
				}
				
				for(int i = -2; i < 2; i++) {
					if(itemMap[x+i][y].getItem() == '`') {
						return numSpaces;
					}
				}
			}
			return 200;
			
		case 3:
			while(x > 0) {
				x -= 1;
				numSpaces += 1;
				
				if(itemMap[x][y].isObstacle()) {
					if(itemMap[x-1][y].getItem() == '`') {
						return numSpaces + 1;
					} else {
						return 200; // Arbitrarily large number		
					}
				}
				
				for(int i = -2; i < 2; i++) {
					if(itemMap[x][y+i].getItem() == '`') {
						return numSpaces;
					}
				}
			}
			return 200;
		}
		return 200;
	}

}
