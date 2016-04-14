package student;

import game.Edge;
import game.EscapeState;
import game.Node;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Predicate;

/**
 * @author YasserAlejandro
 * An implementation of OptimalPath using Dijktra's algorithm
 */
public class OptimalPathDijkstraImpl implements OptimalPath {
	
	/**
	 * The state phase is injected in this class to be able to navigate and leverage
	 * its methods. 
	 */
	private EscapeState state;
	
	/**
	 * This is the starting/departure point(node).
	 */
	private Node startNode;
	
	/**
	 * This is the ending/target point(node).
	 */
	private Node targetNode;
	
	/**
	 * Constructor of the class. 
	 * @param state
	 * @param startNode
	 * @param targetNode
	 */
	public OptimalPathDijkstraImpl(EscapeState state, Node startNode, Node targetNode){
		this.state=state;
		this.startNode = startNode;
		this.targetNode = targetNode;
	}
	
	
	/**
	 * This PriorityQueue is the auxiliary PriorityQueue data structure being used to implement Djikstra
	 * solution. A SuperNode is class that contains Metadata about a Node. 
	 */
	PriorityQueue<SuperNode> frontier = new PriorityQueueImpl<>();
	
	/**
	 * This Map helps to stored Metadata information about every Node in the graph. The Long key is
	 * the unique id of every Node in the graph.
	 */
	Map<Long, SuperNode> mapper = new HashMap<>();
	
	/**
	 * This Map is used to register the distance between the startNode and every node in the graph.
	 */
	Map<Long, Integer> pathWeights = new HashMap<>();
	
	/**
	 * This Stack of Nodes is used to record the optimal path sequence trace of navigation of nodes. 
	 */
	Stack<Node> sequence = new Stack<>();
	
	/**
	 * This predicate serves to filter out any non-navigable Nodes.
	 */
	Predicate<Node> navigable = p -> p.getTile().getType().isOpen() == true;
	
	/**
	 * This is the implementation of the method from the OptimalPath interface. Its output is the 
	 * optimal minimal distance between the startNode and targetNode based on Djistra's optimal
	 * path algorithm.
	 */
	@Override
	public int calculateminpath(){
	
        /*
		 *The below collection manipulation creates a SuperNode Metadata for each Node in the Escape state and stores it
		 *in the mapper Map.
		 */  	
		state.getVertices().stream().filter(navigable).forEach((n) ->mapper.put(n.getId(),new SuperNode(n,null,null)));
		
		SuperNode starting = mapper.get(startNode.getId());
		starting.setDistance(0);
		frontier.add(starting,0);
		pathWeights.put(starting.getNode().getId(),0);
		
		/*
		 * The below section goes thru every Node and facilitates the metadata population of every SuperNode.
		 * As well as the calculation and update of the distance between every Node and the start Node. In
		 * addition, it updates the PriorityQueue frontier.
		 */
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
		
		return getDistance(targetNode);		
		
	}
	
	/**
	 * Implementation of run from OptimalPath interface.
	 * It leverages the Stack<Node> sequence and the mapper Map to navigate from
	 * the current Node to the Target Node. It picks up gold on its way.
	 */
	@Override
	public void run(){
		
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
	
	/**
	 * This method returns the distance between a particular Node entered as a parameter
	 * and the starting Node of this state.
	 */
	public int getDistance(Node node){
		return mapper.get(node.getId()).getDistance();
	}
		

}
