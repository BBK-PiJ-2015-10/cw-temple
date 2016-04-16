package student;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import game.ExplorationState;
import game.NodeStatus;

/** 
 * @author ypalac01
 * An implementation of the ExplorationStrategy interface using the a Depth-First-Search algorithm
 * aided by an optimizer based on the minimal distance to the target node.
 */
public class ExplorationDepthFirstSearch implements ExplorationStrategy {
	
	/**
	 * An exploration state field, to provide application context to this class.
	 */
	private ExplorationState state;
	
	/**
	 * Constructor with the injection of an exploration state.
	 * @param ExplorationState . The exploration state in whose context this class will operate.
	 */
	public ExplorationDepthFirstSearch(ExplorationState state){
		this.state = state;
	}
	
	/**
	 * A set of long to capture the id's of all the nodes already visited. This is to
	 * optimize navigation by minimizing the visit to already visited nodes.
	 */
	private Set<Long> seen = new LinkedHashSet<>();
	
	/**
	 * Stack of long to create a stack of node id's. This is to keep track of the sequence
	 * in which nodes have been visited to facilitate navigation.
	 */
	private Stack<Long> compass = new Stack<>();
	
	/**
	* Implementation of the method from interface. Description of procedures:
	* a - Executes until target is reached.
	* b - Push current node into stack and marks it as visited on the visit tracking map.
	* c - Retrieves (if any) a neighbor of the current node that is unvisited and who is closer
	* d - to that target. Moves to that node. Repeat steps a to c.
	* e - If step c didn't retrieve a value. Pops a node from the stack and moves into that node. 
	* Repeat steps a to c.
	**/
	@Override
	public void execute(){
		
		/*
		 * The while statement guarantees navigation until the target is reached.	
		 */
	    while (state.getDistanceToTarget() != 0) {
	        
	        compass.push(state.getCurrentLocation());
	        seen.add(state.getCurrentLocation());
	        
	        /*
	         * This predicate filters out any already visited nodes.
	         */
	        Predicate<NodeStatus> unvisited = p -> seen.contains(p.getId()) == false;
	              
	        /*
	         * This is the id of the next node to be visited.
	         */
	        Long next;
	   
	        /*
	         * Returns a possible neighbor of the current node that has not being visited 
	         * and is closest to the target node.
	         */
	        Optional<NodeStatus> nextpossible = state.getNeighbours().stream().filter(unvisited).min((p1,p2)-> p1.compareTo(p2));
	        
	        /*
	         * If nextpossible exists it assigns its id to next; if it doesn't exist, next takes the id of the previously visited node.
	         */
	        if (nextpossible.isPresent()){
	        	next = nextpossible.get().getId();
	        }
	        else {
	        		compass.pop();
	            	next = compass.pop();
	        }
	        
	        //It moves from the current location to the node associated with the next id.
	        state.moveTo(next);  
	       
	    }
	
	}

}
