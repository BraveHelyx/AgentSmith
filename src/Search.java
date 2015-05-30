import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class Search {
	Map map;
	Inventory inventory;
	Node objective;
	
	public Search(Map _map, Inventory _inventory, Node _objective){
		map = _map;
		inventory = _inventory;
		objective = _objective;
	}
	
	public ArrayList<Node> findPath(){
		
		Node currNode;
		ArrayList<Node> adjacentNodes;
		ArrayList<Node> returnedPath = null;
		
		//Flag if we have found the objective node
		boolean found = false;
		
		//The frontier of paths to visit
		PriorityQueue<Path> pathsToVisit = new PriorityQueue<Path>();
		
		//TODO Flood the map with heuristic values
		//
		//
		
		
		//Create a new path and add it to frontier
		Path currPath = new Path(map.getAgentNode(), map.getDirection(), inventory);
		pathsToVisit.add(currPath);
		
		//While there are still nodes to visit
		while(!pathsToVisit.isEmpty() && found == false){
			currPath = pathsToVisit.poll();	//Pop off frontier
			currNode = currPath.getCurrentNode();
			
			//Check if we have found a path to the goal
			if(currNode.equals(objective)){
				returnedPath = currPath.getCurrentPath();
			} else {
				//Process what happens when we encounter the item
			}
				
			//For each of the nodes
			adjacentNodes = map.getAdjacentNodes(currNode);
			for(Node n : adjacentNodes){
				if(!currPath.getCurrentPath().contains(n)){		//If it is a node that isn't in the path
					Path newPath = currPath.clone();			//Clone the path, and add the node to it.
					newPath.addToPath(n);
				}
			}
				
		}
		
		return returnedPath;
	}
	
	
}
