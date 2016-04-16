package student;

/**
 * @author ypalac01
 * An EscapeStrategy is a path navigation strategy to go from the starting node
 * of the Escape state to the Exit node.
 */

public interface EscapeStrategy {

	/**
	 * Executes the defined EscapeStrategy to go from the Escape state starting node
	 * to the Exit node.
	 */
	public void execute();
	
}
