import java.util.ArrayList;
import java.util.PriorityQueue;


public class Explore {
	
	private Map map;
	private Inventory inventory;
	
	public Explore(Map map_, Inventory inventory_) {
		map = map_;
		inventory = inventory_;
	}
	
	public ArrayList<Node> findPath(){
		
		Node currNode;
		ArrayList<Node> adjacentNodes;
		ArrayList<Node> returnedPath = null;
		Path newPath;
		
		//Flag if we have found the objective node
		boolean found = false;
		
		//The frontier of paths to visit
		PriorityQueue<Path> pathsToVisit = new PriorityQueue<Path>();
		
		//Create a new path and add it to frontier
		Path currPath = new Path(map.getAgentNode(), map.getDirection(), inventory);
		pathsToVisit.add(currPath);
		
		//While there are still nodes to visit
		while(!pathsToVisit.isEmpty() && found == false){
			currPath = pathsToVisit.poll();	//Pop off frontier
			currNode = currPath.getCurrentNode();
			
			//Check if we have found a path to the goal
			if(currNode.getItem() == '`'){
				found = true;
				returnedPath = currPath.getCurrentPath();
			} 
				
			if(!found){
				//For each of the nodes
				adjacentNodes = map.getAdjacentNodes(currNode);
				
				for(Node n : adjacentNodes){
					if(!currPath.getCurrentPath().contains(n)){			//If it is a node that isn't in the path
						
						
						Inventory pathInventory = currPath.getInventory();
						
						
						if(n.getItem() == ' '){	//If on boat and go onto land, toggle boat		
							if(currPath.isInBoat()){
								currPath.toggleBoat();
							}
							newPath = currPath.clone();
							newPath.addToPath(n);
							
							//Add to the frontier
							pathsToVisit.add(newPath);
							
						} else if(n.isObstacle()){						//Process what happens if we encountered an obstacle		
							
							if(n.getItem() == 'T'){						//If we encounter a tree and we have an axe, add to frontier
								
								if(pathInventory.containsAxe()){
									newPath = currPath.clone();			
									newPath.addToPath(n);
									
									//Add to the frontier
									pathsToVisit.add(newPath);
								}
								
							} else if(n.getItem() == '*'){				//If we encounter a wall and we have dynamite, add to frontier
								
								if(pathInventory.containsDynamite()){	
									//Use dynamite to expand and then clone
									pathInventory.useDynamite();
									newPath = currPath.clone();			
									newPath.addToPath(n);
									
									//Add to the frontier
									pathsToVisit.add(newPath);
								}
								
							} else {									//If we encounter water and we're on a boat, add to frontier
								if(currPath.isInBoat()){
									newPath = currPath.clone();			
									newPath.addToPath(n);
									
									//Add to the frontier
									pathsToVisit.add(newPath);
								}
							}
						} else if(n.getItem() == 'B'){					//If item expanded is a boat
							newPath = currPath.clone();
							newPath.addToPath(n);
							newPath.toggleBoat();
							
							//Add to the frontier
							pathsToVisit.add(newPath);
						}
					} else {}
				}	
			}	
		}
		
		return returnedPath;
	}
}
