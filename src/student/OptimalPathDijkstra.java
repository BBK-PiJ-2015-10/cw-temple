package student;

import game.Edge;
import game.EscapeState;
import game.Node;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Predicate;

/**
 * @author ypalac01
 * An implementation of OptimalPath using Dijktra's algorithm that provides the minimal
 * distance between two paths.
 */
public class OptimalPathDijkstra implements OptimalPath {
	
	/**
	 * An EscapeState field injected in this class to provide all application the context 
	 * on which the class is to operate.  
	 */
	private EscapeState state;
	
	/**
	 * The starting or departure node.
	 */
	private Node startNode;
	
	/**
	 * The ending or target node.
	 */
	private Node targetNode;
	
	/**
	 * Constructor of the class. 
	 * @param state. The state to provide context to this class.
	 * @param startNode. The starting node.
	 * @param targetNode. The ending/target node.
	 */
	public OptimalPathDijkstra(EscapeState state, Node startNode, Node targetNode){
		this.state=state;
		this.startNode = startNode;
		this.targetNode = targetNode;
	}
	
	
	/**
	 * A PriorityQueu data structure being use to implement Djikstra optimization solution. 
	 * A MetaNodePre is class that contains metadata about a node, for more information
	 * refer to @MetaNodePre class. 
	 */
	PriorityQueue<MetaNodePre> frontier = new PriorityQueueImpl<>();
	
	/**
	 * A map to store metadata information about every node in the graph. The long key is
	 * the unique id of every node in the graph. The MetaNodePre contains metadata about the
	 * the node corresponding to the key. 
	 */
	Map<Long, MetaNodePre> mapper = new HashMap<>();
	
	/**
	 * A map used to register the distance between the starting node and every node in the graph.
	 */
	Map<Long, Integer> pathWeights = new HashMap<>();
	
	/**
	 * A stack of nodes use to record the optimal path navigation sequence from the starting node 
	 * to the target node. 
	 */
	Stack<Node> sequence = new Stack<>();
	
	/**
	 * A predicate to filter out any node that is not open (navigable).
	 */
	Predicate<Node> navigable = p -> p.getTile().getType().isOpen() == true;
	
	/**
	 * Implementation of the method from the OptimalPath interface. It outputs is the 
	 * optimal minimal distance between the starting node and target node based on 
	 * Djistra's optimal path algorithm.
	 * @return int. The optimal distance between two nodes based on Dijstra's algorithm.
	 */
	@Override
	public int calculateoptimalpath(){
	
        /*
		 *The below collection creates a MetaNodePre metadata for each node in the escape state and stores it
		 *in a map.
		 */  	
		state.getVertices().stream().filter(navigable).forEach((n) ->mapper.put(n.getId(),new MetaNodePre(n,null,null)));
		
		MetaNodePre starting = mapper.get(startNode.getId());
		starting.setDistance(0);
		frontier.add(starting,0);
		pathWeights.put(starting.getNode().getId(),0);
		
		/*
		 * The below section goes thru every node and facilitates the metadata population of every MetaNodePre.
		 * As well as the calculation and update of the distance between every node and the starting node. In
		 * addition, it updates the PriorityQueue frontier.
		 */
		while (frontier.size()!=0){
    		MetaNodePre current = frontier.poll();
    		int cWeight = pathWeights.get(current.getNode().getId());
    		for (Edge e : current.getNode().getExits()){
    			Node other = e.getOther(current.getNode());
    			if (other.getTile().getType().isOpen()) {
    				MetaNodePre superother = mapper.get(other.getId());
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
	 * Implementation of run method from OptimalPath interface.
	 * It leverages the stack of Node sequence and the mapper Map data structures
	 * to navigate from the starting node to the target node. It picks up gold on its way.
	 */
	@Override
	public void run(){
		
	    sequence.push(targetNode);	
	    MetaNodePre temp = mapper.get(targetNode.getId());
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
	 * Returns the distance between a particular node entered as a parameter
	 * and the starting node.
	 * @param node. The node whose distance from the starting node we wish to
	 * retrieve.
	 * @return int. The distance between the starting node and the node entered as
	 * a parameter.
	 */
	public int getDistance(Node node){
		return mapper.get(node.getId()).getDistance();
	}
		

}
