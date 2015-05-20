
/*
 * Node class for each x, y value in the map.
 */
public class Node {
	
	private int x;
	private int y;
	private char item;
	private int h;
	private int g;
	
	public Node() {
		x = 0;
		y = 0;
		item = ' ';
		h = 0;
		g = 0;
	}
	
	public Node(int x_, int y_, char item_, int h_, int g_) {
		x = x_;
		y = y_;
		item = item_;
		h = h_;
		g = g_;
	}
	
	public int f() {
		return h + g;
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
	
	public int getH() {
		return h;
	}
	
	public int getG() {
		return g;
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
	
	public void updateH(int cost) {
		h += cost;
	}
	
	public void updateG(int cost) {
		g += cost;
	}


}
