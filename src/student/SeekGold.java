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

/*Performance Improvements:
 *  - I am going to add a Map where each Node will have how much golds have its neighbors and make that a priority.
 *  - Need to figure out how to use two max. Maybe on gets the max and the second filters on the max.
 *  - Hence, I can filter for max gold and then for unvisited.
 */


public class SeekGold {

	private EscapeState state;
	
	public SeekGold(EscapeState state) {
		this.state = state;
	}
	
	Map<Node, Integer> nodeGoldMap = new HashMap<>();
	
	Map<Node, Collection<Node> > neighborsMap = new HashMap<>();
	
	Map<Node, SuperEvalNode> superEvalNodeMap = new HashMap<>();
	
	Map<Node, Integer> visitedNodeMap = new HashMap<>();
	
	//Map<Node, Integer> nodeNeighborhoodGoldMap = new HashMap<>();
	
	
	 
	Predicate<Node> navigable = p -> p.getTile().getType().isOpen() == true;
	
	Predicate<Node> reachable = p -> superEvalNodeMap.get(p).getDistanceToExit() < (state.getTimeRemaining()-getDistanceToNode(p));
	
	//Predicate<Node> hasGold = p -> nodeGoldMap.get(p) > 0;
	

	
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
	
		
	
	//public int calculateNeighboorHoodGold(Node node){
		//Collection<Node> neighbors = node.getNeighbours();
		//return neighbors.stream().mapToInt((n)-> nodeGoldMap.get(n)).sum() + nodeGoldMap.get(node);
	//}
	
	
	public void populateMaps(){
		
		state.getVertices().stream().filter(navigable).forEach((n) ->nodeGoldMap.put(n,n.getTile().getOriginalGold()));
		
		state.getVertices().stream().filter(navigable).forEach((n) ->neighborsMap.put(n,getNeighbors(n)));
		
		state.getVertices().stream().filter(navigable).forEach((n) ->superEvalNodeMap.put(n,new SuperEvalNode(n,new OptimalPathDijkstraImpl(state,n,state.getExit()))));
		
		state.getVertices().stream().filter(navigable).forEach((n) ->visitedNodeMap.put(n,0));
		
		//state.getVertices().stream().filter(navigable).forEach((n) ->nodeNeighborhoodGoldMap.put(n,calculateNeighboorHoodGold(n)));
					
	}
	
	
	public void seek(){
		
		//populateMaps();
		
		//System.out.println("I have this extra time: " +(state.getTimeRemaining() - superEvalNodeMap.get(state.getCurrentNode()).getDistanceToExit()));
		
		while (state.getTimeRemaining() >= superEvalNodeMap.get(state.getCurrentNode()).getDistanceToExit() ){
			
			Optional<Node> nextNode = neighborsMap.get(state.getCurrentNode()).stream().filter(reachable)
			.min((p1,p2)-> visitedNodeMap.get(p1).compareTo(visitedNodeMap.get(p2)));
			
			//Optional<Node> nextNode = neighborsMap.get(state.getCurrentNode()).stream().filter(reachable)
			//.max((p1,p2)-> nodeNeighborhoodGoldMap.get(p1).compareTo(nodeNeighborhoodGoldMap.get(p2)));
			
			
			
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
