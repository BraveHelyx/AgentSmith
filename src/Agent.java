/*********************************************
 *  Agent.java 
 *  Sample Agent for Text-Based Adventure Game
 *  COMP3411 Artificial Intelligence
 *  UNSW Session 1, 2012
*/

import java.util.*;
import java.io.*;
import java.net.*;

public class Agent {

	//Code that agent uses to select an action

	Map map = new Map();
	Compass compass = new Compass();
	
	public char get_action( char view[][] ) {
		
		
		map.update(view, compass);
		map.printMap();

		int ch=0;
		
		System.out.print("Enter Action(s): ");

		try {
			while ( ch != -1 ) {
				// read character from keyboard
	            ch  = System.in.read();
	
	            switch( ch ) { // if character is a valid action, return it
	            case 'F':
	            case 'f':
	            	char inFront = view[1][2]; // character directly in front of agent
	            	if(!(inFront == 'T' || inFront == '*' || inFront == '.')) {		// check agent can actually move forward
	            		compass.setAgentPosition(compass.getForwardPosition());
	            	}
	            	return (char) ch;
	            	
	            case 'L':
	            case 'l':
	            	compass.turnLeft();
	            	return (char) ch;
	            	
	            case 'R':
	            case 'r':
	            	compass.turnRight();
	            	return (char) ch;
	            	
	            case 'C':
	            case 'c':
	            case 'B':	            	
	            case 'b':
	               return((char) ch );
               }
			}
		} catch (IOException e) {
			System.out.println ("IO error:" + e );
		}

		return 0;
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

		try { // scan 5-by-5 wintow around current location
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
}
