package student;

/**
 * @author YasserAlejandro
 * OptimalPath will provide you the optimal path between two points.
 */
public interface OptimalPath {
	
	/**
	 * Calculates the minimal path between two end points.
	 * 
	 * @return the minimal path distance between two points.
	 */
	public int calculateminpath();
	
	/**
	 * Will execute a minimal path trajectory calculated in calculatemintpath. 
	 */
	public void run();
	
}
