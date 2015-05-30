
public class ExploreStrategy implements Strategy{
	
	private Node[][] itemMap;
	private int posX;
	private int posY;
	private int facing;
	private String moves;
	
	@Override
	public String decideMove(Map map) {
		itemMap = map.getMap();
		posX = map.getPosX();
		posY = map.getPosY();
		facing = map.getFacing();
		
		doSearch();
		
		return moves;
	}
	
	private void doSearch() {
		
		int distance = 199;
		int direction = 0;
		int north = distanceToUnknown(Compass.NORTH);
		int east = distanceToUnknown(Compass.EAST);
		int south = distanceToUnknown(Compass.SOUTH);
		int west = distanceToUnknown(Compass.WEST);
		
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
		
		switch(facing) {
		case 0:
			if(direction == 0) {
				moves = "F";
			} else if(direction == 1) {
				moves = "RF";
			} else if(direction == 2) {
				moves = "RRF";
			} else if(direction == 3) {
				moves = "LF";
			}
			break;
			
		case 1:
			if(direction == 1) {
				moves = "F";
			} else if(direction == 2) {
				moves = "RF";
			} else if(direction == 3) {
				moves = "RRF";
			} else if(direction == 0) {
				moves = "LF";
			}
			break;
			
		case 2:
			if(direction == 2) {
				moves = "F";
			} else if(direction == 3) {
				moves = "RF";
			} else if(direction == 0) {
				moves = "RRF";
			} else if(direction == 1) {
				moves = "LF";
			}
			break;
		
		case 3:
			if(direction == 3) {
				moves = "F";
			} else if(direction == 0) {
				moves = "RF";
			} else if(direction == 1) {
				moves = "RRF";
			} else if(direction == 2) {
				moves = "LF";
			}
			break;
		}		
	}
	
	
	/**
	 * Checks nodes along a compass direction until it finds an unknown and returns the number of
	 * spaces in between.
	 * 
	 * @param direction the direction to check in
	 * 
	 * @return the number of numSpaces to the nearest unknown or a very large number
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
				if(itemMap[x][y].getItem() == '`') {
					return numSpaces;
				}
				else if(itemMap[x][y].isObstacle()) {
					// check for visibility past the wall or tree
					if(itemMap[x][y-1].getItem() == '`') {
						return numSpaces+1;
					} else {
						return 200; // Arbitrarily large number
					}					
				}
			}
			return 200;
			
		case 1:
			while(x < Map.MAXEDGE) {
				x += 1;
				numSpaces += 1;
				if(itemMap[x][y].getItem() == '`') {
					return numSpaces;
				}
				else if(itemMap[x][y].getItem() == '*' || itemMap[x][y].getItem() == 'T') {
					// check for visibility past the wall or tree
					if(itemMap[x+1][y].getItem() == '`') {
						return numSpaces+1;
					} else {
						return 200; // Arbitrarily large number
					}					
				}
			}
			return 200;
			
		case 2:
			while(y < Map.MAXEDGE) {
				y += 1;
				numSpaces += 1;
				if(itemMap[x][y].getItem() == '`') {
					return numSpaces;
				}
				else if(itemMap[x][y].getItem() == '*' || itemMap[x][y].getItem() == 'T') {
					// check for visibility past the wall or tree
					if(itemMap[x][y+1].getItem() == '`') {
						return numSpaces+1;
					} else {
						return 200; // Arbitrarily large number
					}					
				}
			}
			return 200;
			
		case 3:
			while(x > 0) {
				x -= 1;
				numSpaces += 1;
				if(itemMap[x][y].getItem() == '`') {
					return numSpaces;
				}
				else if(itemMap[x][y].getItem() == '*' || itemMap[x][y].getItem() == 'T') {
					// check for visibility past the wall or tree
					if(itemMap[x-1][y].getItem() == '`') {
						return numSpaces+1;
					} else {
						return 200; // Arbitrarily large number
					}					
				}
			}
		}
		return 0;
	}

}
