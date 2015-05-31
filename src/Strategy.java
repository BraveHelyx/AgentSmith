/*
 * Strategy interface
 */
public interface Strategy {
	public String decideMove(Map map, Inventory inventory, Compass compass);
}
