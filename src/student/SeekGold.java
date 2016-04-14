package student;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import game.Edge;
import game.EscapeState;
import game.Node;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;


/**
 * @author YasserAlejandro
 * A class that provides an algorithmic strategy for the search of Gold on an escape state.
 *
 */
public class SeekGold {

	/*
	 * This is the EscapeState injected in the class that will facilitate the navigation and whose
	 *methods are leveraged different sections of this class.
	 */
	private EscapeState state;
	
	/*
	 *This is a simple Constructor.
	 */
	public SeekGold(EscapeState state) {
		this.state = state;
	}
	
	/*
	 * This is a Map that keeps track of the each Node and its gold level at a particular point in time.
	 * Not the original gold of each node, but the actual gold level.  
	 */
	Map<Node, Integer> nodeGoldMap = new HashMap<>();
	
	/*
	 *  This is a map that contains each Node in the graph and its associated neighbors. It is used to 
	 *  navigate thru the graph.  
	 */
	Map<Node, Collection<Node> > neighborsMap = new HashMap<>();
	
	/*
	 *
	 */
	Map<Node, SuperEvalNode> superEvalNodeMap = new HashMap<>();
	
	/*
	 * This is a Map that keeps track on how many times a Node has been visited. It is used to facilitate
	 * and optimize navigation.
	 */
	Map<Node, Integer> visitedNodeMap = new HashMap<>();
	
	
	
	
	 
	Predicate<Node> navigable = p -> p.getTile().getType().isOpen() == true;
	
	Predicate<Node> reachable = p -> superEvalNodeMap.get(p).getDistanceToExit() < (state.getTimeRemaining()-getDistanceToNode(p));
	

	

	
	public int getDistanceToNode (Node node2){
		for (Edge e : state.getCurrentNode().getExits()){
			Node other = e.getOther(state.getCurrentNode());
			if (other == node2){
				return e.length;
			}
		}
		return Integer.MAX_VALUE;	
	}
	
	
	
	public Collection<Node> getNeighbors(Node node){
		Collection<Node> neighbors = new ArrayList<>();
		for (Edge e : node.getExits()){
			Node other = e.getOther(node);
			if (other.getTile().getType().isOpen()) {
				neighbors.add(other);
			}
		}	
		return neighbors;
	}
	
		
	
	public void populateMaps(){
		
		state.getVertices().stream().filter(navigable).forEach((n) ->nodeGoldMap.put(n,n.getTile().getOriginalGold()));
		
		state.getVertices().stream().filter(navigable).forEach((n) ->neighborsMap.put(n,getNeighbors(n)));
		
		state.getVertices().stream().filter(navigable).forEach((n) ->superEvalNodeMap.put(n,new SuperEvalNode(n,new OptimalPathDijkstraImpl(state,n,state.getExit()))));
		
		state.getVertices().stream().filter(navigable).forEach((n) ->visitedNodeMap.put(n,0));
		
					
	}
	
	
	public void seek(){
		
		
		while (state.getTimeRemaining() >= superEvalNodeMap.get(state.getCurrentNode()).getDistanceToExit() ){
			
			Optional<Node> nextNode = neighborsMap.get(state.getCurrentNode()).stream().filter(reachable)
			.min((p1,p2)-> visitedNodeMap.get(p1).compareTo(visitedNodeMap.get(p2)));
						
			
			if (nextNode.isPresent()){
				state.moveTo(nextNode.get());
				visitedNodeMap.put(state.getCurrentNode(),visitedNodeMap.get(state.getCurrentNode())+1);
				if (nodeGoldMap.get(state.getCurrentNode()) > 0) {
		    		state.pickUpGold();
		    		nodeGoldMap.put(state.getCurrentNode(),0);
		    	 }
			}
			
			else {
				superEvalNodeMap.get(state.getCurrentNode()).getEscapeMinPath().run();
				
			}		
		}
		
	
		
		
	}
	
	
	
	
}
