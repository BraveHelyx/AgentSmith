import java.util.ArrayList;
import java.util.Iterator;


public class MoveGenerator {
	
	Compass compass;
	ArrayList<Node> path;
	String moves;
	
	MoveGenerator(ArrayList<Node> path_, Compass compass_){
		path = path_;
		compass = compass_;
		moves = "";
	}
	
	public void generateMoves() {		
		Iterator<Node> pathIterator = path.iterator();
		Node nextNode;
		BountyPoint nextPoint;
		while(pathIterator.hasNext()){
			nextNode = pathIterator.next();
			nextPoint = nextNode.getPoint();
			if(compass.isForward(nextPoint)){
				if(nextNode.getItem() == '*'){
					moves += "BF";
				} else if(nextNode.getItem() == 'T'){
					moves += "CF";
				} else {
					moves += "F";
				}
			} else if(compass.isRight(nextPoint)){
				if(nextNode.getItem() == '*'){
					moves += "RBF";
				} else if(nextNode.getItem() == 'T'){
					moves += "RCF";
				} else {
					moves += "RF";
				}
			} else if(compass.isLeft(nextPoint)){
				if(nextNode.getItem() == '*'){
					moves += "LBF";
				} else if(nextNode.getItem() == 'T'){
					moves += "LCF";
				} else {
					moves += "LF";
				}
			} else {
				if(nextNode.getItem() == '*'){
					moves += "RRBF";
				} else if(nextNode.getItem() == 'T'){
					moves += "RRCF";
				} else {
					moves += "RRF";
				}
			}			
		}		
	}
	
	public String getMoves(){
		return moves;
	}
	
}
