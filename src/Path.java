import java.util.ArrayList;


/**
 * Objects for handling pathing information for A* search
 * 
 * @author Bruce Hely
 *
 */
public class Path {
	private ArrayList<Node> currentPath = new ArrayList<Node>();
	Node currentNode;
	private int numNodes;
	private int numRotations;
	private Inventory availableTools;
	private int currDirection;
	
	//	g(x) value
	private int pathValue;
	
	/**
	 * Constructor used for creating initial paths from an origin
	 * 
	 * @param startNode				The origin of the search
	 * @param direction				The direction agent is currently facing to get to startNode
	 * 
	 */
	public Path(Node startNode, int direction, Inventory playerInventory){
		
		//Add the agent's position to the list of paths, to ensure no backtracking occurs.
		currentNode = startNode;
		currentPath.add(startNode);
		
		currDirection = direction;
		availableTools = playerInventory.clone();
		
		numNodes = 1;
		numRotations = 0;
		
		updatePathValue();
	}
	
	/**
	 * Constructor used for cloning new paths from old ones
	 * 
	 */
	public Path(ArrayList<Node> _currentPath, Node _currentNode, int _numNodes, int _numRotations, int _currDirection){
		for(Node n : _currentPath){
			currentPath.add(n);
		} 
		
		currentNode = _currentNode;
		numNodes = _numNodes;
		numRotations = _numRotations;
		currDirection = _currDirection;
	}
	
	public ArrayList<Node> getCurrentPath(){
		return currentPath;
	}
	
	/**
	 * Method that should be run every time a new node is added to update this
	 * path's path value (g(x)). Returns 1 less than the number of nodes in the path,
	 */
	private void updatePathValue(){
		pathValue = (numNodes-1) + numRotations;
	}
	
	/**
	 * Method for adding a node to the path
	 * 
	 * @precondition	currentPath cannot already contain newNode
	 * @precondition	newNode must be a node that is vertically or horizontally adjacent to currentNode in 2D Geometry.
	 */
	public void addToPath(Node newNode){
		int currX = currentNode.getX();
		int currY = currentNode.getY();
		
		int nextX = newNode.getX();
		int nextY = newNode.getY();
		
		//Handles addition of rotations
		//Note that we will never have to incremement rotations by 2, because backtracking is not allowed.
		if(currX == nextX){			//If we have moved Vertically, then the X Values are the same
			if(currDirection == Compass.WEST || currDirection == Compass.EAST){
				numRotations++;
			}
		} else {					//If we have moved Horizontally, then the X Values are different
			if(currDirection == Compass.NORTH || currDirection == Compass.SOUTH){
				numRotations++;
			} 
		}
		
		//Add the node to path, and update the current number of nodes in the path
		currentPath.add(newNode);
		numNodes++;
		
		//Update this path's g(x) cost.
		updatePathValue();
	}
	
	public Node getCurrentNode(){
		return currentNode;
	}
	
	@Override
	public Path clone(){
		return new Path(currentPath, currentNode, numNodes, numRotations, currDirection);
	}
}
