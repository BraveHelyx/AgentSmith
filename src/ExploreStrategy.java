import java.util.ArrayList;


public class ExploreStrategy implements Strategy{
	
	private Map localMap;
	private Inventory inventory;
	private ArrayList<Node> path;
	private Compass compass;
	
	@Override
	public ArrayList<Node> decideMove(Map map, Inventory inventory_, Compass compass_) {
		localMap = map;
		inventory = inventory_.clone();
		compass = compass_.clone();
		
		Explore explore = new Explore(localMap, inventory);
		path = explore.findPath();
		return path;	
	}
}
