package student;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import game.ExplorationState;
import game.NodeStatus;

/** 
 * @author YasserAlejandro
 * This class is an implementation of the ExplorationStrategy interface using the a Depth-First-Search algorithm
 * aided by an optimizer based on the minimal distance to the target node.
 */
public class ExplorationDepthFirstSearch implements ExplorationStrategy {
	
	/**
	 * An ExplorationState field in the class. Its methods are being leveraged across this class.
	 */
	private ExplorationState state;
	
	/**
	 * Constructor with the injection of an ExplorationState.
	 * @param an ExplorationState state.
	 */
	public ExplorationDepthFirstSearch(ExplorationState state){
		this.state = state;
	}
	
	/**
	 *  A set of Long variable to capture the id's of all the nodes already visited. This is to
	 *  optimize navigation by minimizing the visit to already visited nodes.
	 */
	private Set<Long> seen = new LinkedHashSet<>();
	
	/**
	 * This is a stack of Long to create a stack of Node id's. This is to keep track of the sequence
	 * in which nodes have been visited to facilitate navigating in and out of a node sector.
	 */
	private Stack<Long> compass = new Stack<>();
	
	/**
	* This is the execution of the depth-first-search strategy
	**/
	@Override
	public void run(){
		
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
	         * This is the id of the next Node to be visited.
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
