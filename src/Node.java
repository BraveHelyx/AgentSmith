
/*
 * Node class for each x, y value in the map.
 */
public class Node{
	
	private int x;
	private int y;
	private char item;
	private int h;
	
	public Node() {
		x = 0;
		y = 0;
		item = ' ';
		h = 0;
	}
	
	public Node(int x_, int y_, char item_, int h_) {
		x = x_;
		y = y_;
		item = item_;
		h = h_;

	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public char getItem() {
		return item;
	}
	
	public void setH(int newH) {
		h = newH;
	}
	public int getH() {
		return h;
	}
	
	public void setX(int x_) {
		x = x_;
	}
	
	public void setY(int y_) {
		y = y_;
	}
	
	public void setItem(char item_) {
		item = item_;
	}
	
	public boolean isHeuristic(){
		if(item == 'g' || item == 'a' || item == 'd' || item == 'B'){
			return true;
		}
		return false;
	}
	
	public boolean isObstacle() {
		if(item == '*' || item == '~' || item == 'T') {
			return true;
		}
		return false;
	}
	
	public boolean isMapEdge() {
		if(item == '.') {
			return true;
		}
		return false;
	}
	
	public void updateH(int cost) {
		h += cost;
	}
	
	@Override
	public Node clone(){
		return new Node(x, y, item, h);
	}
	
	@Override
	public boolean equals(Object other){
		boolean result = false;
		if(other instanceof Node){
			Node target = (Node)other;
			result = (this.getX() == target.getX() && this.getY() == target.getY() && this.getItem() == target.getItem());
		}
		return result;
	}
}
