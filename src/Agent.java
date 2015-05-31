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

import com.sun.jmx.remote.internal.ArrayQueue;

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
	
	//variables for keeping
	
	//List of nodes that contain dynamite
	ArrayList<Node> dynamite = new ArrayList<Node>();
	
	ArrayList<Node> heuristicsSeen = new ArrayList<Node>();
	ArrayList<Node> heuristicsObtainable = new ArrayList<Node>();
	ArrayList<Node> heuristicsUnobtainable = new ArrayList<Node>();
	
	public char get_action( char view[][] ) {
		
		//Update the local copy of the map with the new view
		map.update(view, compass);
		map.printMap();

		int ch=0;
		
		//
		char returnedMove;
		String nextMoves;

		//If no moves to process
		if(moveList.isEmpty()){
			
			//Obtain a strategy to proceed with
			Strategy chosenStrategy = decideStrategy(map);
			
			//Retrieve a string (single or many characters which decides what to do)
			nextMoves = chosenStrategy.decideMove(map, inventory, compass);
			
			//Add all the characters individually to the moveList
			for(char c : nextMoves.toCharArray()){
				moveList.add(c);
			}
		}

		//Pop a value off the moveList
		returnedMove = moveList.poll();
		
		//Fail-safe against illegal moves
		char inFront = view[1][2]; // character directly in front of agent
    	if(inFront == 'T' || inFront == '*' || inFront == '.') {
    		if(returnedMove == 'F' || returnedMove == 'f') {
    			returnedMove = 'R';
    			System.out.println("Agent just tried to move forward. He was redirected.");
    		}
    	}
		
		//Update the local assets with the new move
		updateLocalAssets(returnedMove);
		
		//Wait until we give permission to make the move (via keyboard input)
		/*
		try {
			while ( ch != -1 ) {				
				// read character from keyboard
	            ch  = System.in.read();
			}
		} catch (IOException e) {
			System.out.println ("IO error:" + e );
		}
		*/
		
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
	Strategy decideStrategy(Map map){
		//Adds all the nodes that are heuristics to the seen heuristics 
		//scanForHeuristics(map);	
		
		//Placeholder code to demonstrate how this function works. Will change rapidly.
		if(heuristicsSeen.isEmpty()){
			return new ExploreStrategy();
		} else {
			return new RunStrategy();
		}
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
