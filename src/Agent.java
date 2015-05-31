/*********************************************
 *  Agent.java 
 *  Sample Agent for Text-Based Adventure Game
 *  COMP3411 Artificial Intelligence
 *  UNSW Session 1, 2012
*/

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.*;


public class Agent {

	//Code that agent uses to select an action

	Map map = new Map(80);
	Compass compass = new Compass();
	Inventory inventory = new Inventory();
	
	//Create a list of moves (in a queue) to follow until completion
	Queue<Character> moveList = new LinkedList<Character>();
	
	//Variables for inventory locations 
	boolean foundGold = false;
	Node goldNode = null;
	boolean foundAxe = false;
	Node axeNode = null;
	boolean foundBoat = false;
	Node boatNode = null;
	
	//List of nodes that contain dynamite
	ArrayList<Node> dynamite = new ArrayList<Node>();
	
	ArrayList<Node> heuristicsSeen = new ArrayList<Node>();
	ArrayList<Node> heuristicsUnobtainable = new ArrayList<Node>();
	PriorityQueue<DiscoverableNode> heuristicsObtainable = new PriorityQueue<DiscoverableNode>();
	
	public char get_action( char view[][] ) {
		
		//Update the local copy of the map with the new view
		map.update(view, compass);
		map.printMap();
		
		char returnedMove;
		ArrayList<Node> nextMoves;
		String nextCommands;
		
		//If no moves to process
		if(moveList.isEmpty()){
			
			//Obtain a strategy to proceed with
			nextMoves = decideStrategy(map);
			
			//Make move generator
			MoveGenerator m = new MoveGenerator(nextMoves, compass);
			nextCommands = m.getMoves();
			
			//Add all the characters individually to the moveList
			for(char c : nextCommands.toCharArray()){
				moveList.add(c);
			}
		}

		//Pop a value off the moveList
		returnedMove = moveList.poll();
		
		//Update the local assets with the new move
		updateLocalAssets(returnedMove);
		
		// delay
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			System.out.println("Interrupt Exception" + e);
		}
		
		//Print the move out for inspection
		System.out.println("Agent chose: " + returnedMove);
		
		return returnedMove;

	}

	public void updateLocalAssets(char move){
		switch( move ) { // if character is a valid action, return it
        case 'F':
        case 'f':
    		compass.setAgentPosition(compass.getForwardPosition());
    		break;
    		
        case 'L':
        case 'l':
        	compass.turnLeft();
        	break;
        	
        case 'R':
        case 'r':
        	compass.turnRight();
        	break;
        	
        case 'C':
        case 'c':
        	break;
        case 'B':	            	
        case 'b':
        	inventory.useDynamite();   	
        }
	}

	//Merely prints the game state to the console
	void print_view( char view[][] ){
		int i,j;

		System.out.println("\n+-----+");
		for( i=0; i < 5; i++ ) {
			System.out.print("|");
			for( j=0; j < 5; j++ ) {
				if(( i == 2 )&&( j == 2 )) {
					System.out.print('^');
				} else {
					System.out.print( view[i][j] );
				}
			}
			System.out.println("|");
		}
		System.out.println("+-----+");
		
	}

	public static void main( String[] args ) {
		InputStream in  = null;
		OutputStream out= null;
		Socket socket   = null;
		Agent  agent    = new Agent();
		char   view[][] = new char[5][5];
		char   action   = 'F';
		int port;
		int ch;
		int i,j;

		if( args.length < 2 ) {
			System.out.println("Usage: java Agent -p <port>\n");
			System.exit(-1);
		}

		port = Integer.parseInt( args[1] );

		try { // open socket to Game Engine
			socket = new Socket( "localhost", port );
			in  = socket.getInputStream();
			out = socket.getOutputStream();
		} catch( IOException e ) {
			System.out.println("Could not bind to port: "+port);
			System.exit(-1);
		}

		try { // scan 5-by-5 window around current location
			while( true ) {
				for( i=0; i < 5; i++ ) {
					for( j=0; j < 5; j++ ) {
						if( !(( i == 2 )&&( j == 2 ))) {
							ch = in.read();
							if( ch == -1 ) {
								System.exit(-1);
							}
							view[i][j] = (char) ch;
						}
					}
				}
				agent.print_view( view ); // COMMENT THIS OUT BEFORE SUBMISSION
				action = agent.get_action( view );
				out.write( action );
			}
		}
		catch( IOException e ) {
			System.out.println("Lost connection to port: "+ port );
			System.exit(-1);
		} finally {
			try {
				socket.close();
			}
			catch( IOException e ) {}
		}
	}
	
	/**
	 * Method that decides which strategy to perform
	 * 
	 * @param map		The current map object to scan.
	 */
	ArrayList<Node> decideStrategy(Map map){
		
		ArrayList<Node> searchResults = null;

		ArrayList<Node> returnedMoves = null;
		
		//Return home if have gold
		if(foundGold) {
			// Get home Node
			BountyPoint homePoint = new BountyPoint(map.getMaxEdge()/2,map.getMaxEdge()/2);
			Node homeNode = map.getNode(homePoint);
			
			// Create a new search for the home node
			Search newSearch = new Search(map, inventory, homeNode);
			returnedMoves = newSearch.findPath();
			
			// Return the moves straight away
			return returnedMoves;
		}
		
		//Adds all the nodes that are heuristics to the seen heuristics 
		scanForHeuristics(map);	
		
		//For every node in heuristicsSeen, can we obtain it? Classify everything
		if(!heuristicsSeen.isEmpty()){
			for(Node n : heuristicsSeen){
				Search newSearch = new Search(map, inventory, n);		//Create a new search for the node
				searchResults = newSearch.findPath();
				
				//Check if we got a path
				if(!searchResults.isEmpty()){
					heuristicsObtainable.add(new DiscoverableNode(n, searchResults));	//If so, add to obtainables
				} else {
					heuristicsUnobtainable.add(n);	//If not, add to unobtainable
				}		
			}
		}
		
		//We have classified all heuristics
		heuristicsSeen.clear();
		
		//Get moves to heuristic if obtainable ones exist
		if(!heuristicsObtainable.isEmpty()){
			System.out.println("GET SHINY!!");
			DiscoverableNode targetNode = heuristicsObtainable.poll();
			returnedMoves = targetNode.getPathToNode();
			
			//Preupdate the value fields as if we have already obtainedthe item
			if(targetNode.getItem() == 'g'){
				inventory.obtainedGold();
				targetNode.setItem(' ');
			} else if (targetNode.getItem() == 'a'){
				inventory.obtainedAxe();
				targetNode.setItem(' ');
			} else if (targetNode.getItem() == 'B'){
				inventory.toggleBoat();
			} else if (targetNode.getItem() == 'd'){
				inventory.obtainedDynamite();
			}

		} else {
			System.out.println("EXPLORE!!");
			Strategy explore = new ExploreStrategy();
			returnedMoves = explore.decideMove(map, inventory, compass);
		}
		
		
		return returnedMoves;

	}
	
	/**
	 * Method to categorize and sort the nodes into respectable fields that we can process
	 * 
	 * @param node		Node to be evaluated and categorized
	 */
	public void categorizeNode(Node node){
		//If a node turns out to be a gold node
		if(node.getItem() == 'g'){
			foundGold = true;
			goldNode = node.clone();
		} else if (node.getItem() == 'a'){
			foundAxe = true;
			axeNode = node.clone();
		} else if (node.getItem() == 'd'){
			dynamite.add(node.clone());
		} else if (node.getItem() == 'B'){
			foundBoat = true;
			boatNode = node.clone();
		}
	}
	/**
	 * Method that scans a map and adds all nodes that are heuristics to the 
	 * list of seen heuristics.
	 */
	public void scanForHeuristics(Map map){
		Node[][] currentMap = map.getMap();
		
		int i = 0;
		int j = 0;
		for(i = 0; i < map.getMaxEdge(); i++){
			for(j = 0; j < map.getMaxEdge(); j++){
				if(currentMap[i][j].isHeuristic()){
					heuristicsSeen.add(currentMap[i][j].clone());
				}
			}
		}
	}
	
	
}
