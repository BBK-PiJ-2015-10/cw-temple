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


public class SeekGold {

	private EscapeState state;
	
	public SeekGold(EscapeState state) {
		this.state = state;
	}
	
	Map<Node, Integer> nodeGoldMap = new HashMap<>();
	
	Map<Node, Collection<Node> > neighborsMap = new HashMap<>();
	
	Map<Node, SuperEvalNode> superEvalNodeMap = new HashMap<>();
	
	Map<Node, Integer> visitedNodeMap = new HashMap<>();
	
	
	 
	Predicate<Node> navigable = p -> p.getTile().getType().isOpen() == true;
	
	Predicate<Node> reachable = p -> superEvalNodeMap.get(p).getDistanceToExit() < (state.getTimeRemaining()-getDistanceToNode(p));
	
	
	Predicate<Node> hasGold = p -> nodeGoldMap.get(p) > 0;
	

	
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
		
		state.getVertices().stream().filter(navigable).forEach((n) ->superEvalNodeMap.put(n,new SuperEvalNode(n,new OptimalFromToPath(state,n,state.getExit()))));
		
		state.getVertices().stream().filter(navigable).forEach((n) ->visitedNodeMap.put(n,0));
		
					
	}
	
	
	public void seek(){
		
		System.out.println("I have this extra time: " +(state.getTimeRemaining() - superEvalNodeMap.get(state.getCurrentNode()).getDistanceToExit()));
		
		//int counter=0;
		
		while (state.getTimeRemaining() >= superEvalNodeMap.get(state.getCurrentNode()).getDistanceToExit() ){
			
			
			//neighborsMap.get(state.getCurrentNode()).stream().forEach((n) -> System.out.println(superEvalNodeMap.get(n).getDistanceToExit()));;
			
			Optional<Node> nextNode = neighborsMap.get(state.getCurrentNode()).stream().filter(reachable)
			.min((p1,p2)-> visitedNodeMap.get(p1).compareTo(visitedNodeMap.get(p2)));
		    //.max((p1,p2)-> nodeGoldMap.get(p1).compareTo(nodeGoldMap.get(p2)))		
			//.max((p1,p2)-> nodeGoldMap.get(p1).compareTo(nodeGoldMap.get(p2)));
			if (nextNode.isPresent()){
				state.moveTo(nextNode.get());
				visitedNodeMap.put(state.getCurrentNode(),visitedNodeMap.get(state.getCurrentNode())+1);
				//counter ++;
				//System.out.println("This is my time inside the if : " +counter);
				if (nodeGoldMap.get(state.getCurrentNode()) > 0) {
		    		state.pickUpGold();
		    		nodeGoldMap.put(state.getCurrentNode(),0);
		    	 }
			}
			else {
				superEvalNodeMap.get(state.getCurrentNode()).getEscapeMinPath().run();
				
			}
			
			//superEvalNodeMap.get(state.getCurrentNode()).getEscapeMinPath().run();
			
					
		}
		
	
		
		
	}
	
	
	
	
}
