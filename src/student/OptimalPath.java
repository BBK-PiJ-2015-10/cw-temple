package student;

/**
 * @author ypalac01
 * An implementation will provide the optimal path between two points.
 */
public interface OptimalPath {
	
	/**
	 * Calculates the optimal path between two end points.
	 * @return int. The optimal path distance between two points.
	 */
	int calculateoptimalpath();
	
	/**
	 * Traverses through the optimal path trajectory between two points.
	 */
	void run();
	
}
