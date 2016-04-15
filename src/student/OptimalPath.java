package student;

/**
 * @author ypalac01
 * An implementation will provide the optimal path between two points.
 */
public interface OptimalPath {
	
	/**
	 * Calculates the minimal path between two end points.
	 * @return int. The minimal path distance between two points.
	 */
	public int calculateminpath();
	
	/**
	 * Traverses through the minimal path trajectory between two points.
	 */
	public void run();
	
}
