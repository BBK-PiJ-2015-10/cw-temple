package student;

import game.Edge;
import game.EscapeState;
import game.Node;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Predicate;


public class OptimalFromToPath {
	
	private EscapeState state;
	
	private Node startNode;
	
	private Node targetNode;
	
	public OptimalFromToPath(EscapeState state, Node startNode, Node targetNode){
		this.state=state;
		this.startNode = startNode;
		this.targetNode = targetNode;
	}
	
	
	
	PriorityQueue<SuperNode> frontier = new PriorityQueueImpl<>();
	
	Map<Long, SuperNode> mapper = new HashMap<>();
	
	Map<Long, Integer> pathWeights = new HashMap<>();
	
	Stack<Node> sequence = new Stack<>();
	
	Predicate<Node> navigable = p -> p.getTile().getType().isOpen() == true;
	
	
	public int calculateminpath(){
		
		state.getVertices().stream().filter(navigable).forEach((n) ->mapper.put(n.getId(),new SuperNode(n,null,null)));
		
		SuperNode starting = mapper.get(startNode.getId());

		starting.setDistance(0);
		frontier.add(starting,0);
		pathWeights.put(starting.getNode().getId(),0);
		
		while (frontier.size()!=0){
    		SuperNode current = frontier.poll();
    		int cWeight = pathWeights.get(current.getNode().getId());
    		for (Edge e : current.getNode().getExits()){
    			Node other = e.getOther(current.getNode());
    			if (other.getTile().getType().isOpen()) {
    				SuperNode superother = mapper.get(other.getId());
    				int weightThroughOther = cWeight + e.length();
    				Integer existingWeight = pathWeights.get(other.getId());
    				if (existingWeight == null) {
    					superother.setDistancePredecessor(weightThroughOther,current.getNode());
    					pathWeights.put(other.getId(), weightThroughOther);
    					frontier.add(superother, weightThroughOther);
    					mapper.put(other.getId(), superother);
    				}
    				else if (weightThroughOther < existingWeight){
    					superother.setDistancePredecessor(weightThroughOther,current.getNode());
    					pathWeights.put(other.getId(), weightThroughOther);
    					frontier.updatePriority(superother, weightThroughOther);
    					mapper.put(other.getId(), superother);
    				}
    			}
    		}
    	}
		
		return +getDistance();		
		
	}
	
	
	
	
	
	public void run(){
		
		//calculateminpath();
	    sequence.push(targetNode);	
	    SuperNode temp = mapper.get(targetNode.getId());
	    while (temp != mapper.get(state.getCurrentNode().getId()) )
		      {
			      sequence.push(temp.getPrede());
			      temp = mapper.get(temp.getPrede().getId());
		      }
	      
	        
	     sequence.pop();
	      
	    
	     while (!sequence.isEmpty()){
	    	  Node mover = sequence.pop();
	    	  System.out.println("I am moving to " +mover);
	    	  state.moveTo(mover);
	    	  if (state.getCurrentNode().getTile().getOriginalGold() > 0) {
	    		  state.pickUpGold();
	    	  }
	     }		
		 
		
	}
	
	public int getDistance(){
		return mapper.get(targetNode.getId()).getDistance();
	}
	
	public int getDistanceToExit(Node node){
		return mapper.get(node.getId()).getDistance();
	}
	
/*	
	public void run(){
		
		state.getVertices().stream().filter(navigable).forEach((n) ->mapper.put(n.getId(),new SuperNode(n,null,null)));
		
		SuperNode starting = mapper.get(state.getCurrentNode().getId());

		starting.setDistance(0);
		frontier.add(starting,0);
		pathWeights.put(starting.getNode().getId(),0);
		
		while (frontier.size()!=0){
    		SuperNode current = frontier.poll();
    		int cWeight = pathWeights.get(current.getNode().getId());
    		for (Edge e : current.getNode().getExits()){
    			Node other = e.getOther(current.getNode());
    			if (other.getTile().getType().isOpen()) {
    				SuperNode superother = mapper.get(other.getId());
    				int weightThroughOther = cWeight + e.length();
    				Integer existingWeight = pathWeights.get(other.getId());
    				if (existingWeight == null) {
    					superother.setDistancePredecessor(weightThroughOther,current.getNode());
    					pathWeights.put(other.getId(), weightThroughOther);
    					frontier.add(superother, weightThroughOther);
    					mapper.put(other.getId(), superother);
    				}
    				else if (weightThroughOther < existingWeight){
    					superother.setDistancePredecessor(weightThroughOther,current.getNode());
    					pathWeights.put(other.getId(), weightThroughOther);
    					frontier.updatePriority(superother, weightThroughOther);
    					mapper.put(other.getId(), superother);
    				}
    			}
    		}
    	}
		
		//System.out.println("The time available is " +getDistance());
		
	    sequence.push(state.getExit());	
	    SuperNode temp = mapper.get(state.getExit().getId());
	    while (temp != mapper.get(state.getCurrentNode().getId()) )
		      {
			      sequence.push(temp.getPrede());
			      temp = mapper.get(temp.getPrede().getId());
		      }
	      
	        
	     sequence.pop();
	      
	    
	     while (!sequence.isEmpty()){
	    	  Node mover = sequence.pop();
	    	  System.out.println("I am moving to " +mover);
	    	  state.moveTo(mover);
	    	  if (state.getCurrentNode().getTile().getOriginalGold() > 0) {
	    		  state.pickUpGold();
	    	  }
	     }		
		 
		
	}	
	
*/	
	
	
	
	

}
