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
 * @author ypalac01
 * 
 * An implementation of the EscapeStratety interface. It is characterize as being: 
 * - Informed: Since it defines and uses four data structures (Maps) to gather as much information
 * as possible about all the nodes in the cavern and leverage this information in the navigation process.
 * - Gold-seek oriented: Since it searches for as much gold as possible until it knows that it
 * needs to head to the Exit node to avoid being trapped in the cavern.
 *  - Exit-Optimizer: In the sense that once it knows that it needs to head to the Exit node, it uses
 * the minimal path strategy to reach that Exit node.
 */
public class EscapeInformedGoldSeekExitOpt implements EscapeStrategy {

	/**
	 * This is the EscapeState state injected to provide application context into this class.
	 */
	private EscapeState state;
	
	/**
	 * Constructor, takes an escape state as an argument.
	 * @param state. An escape state providing application context to this class.
	 */
	public EscapeInformedGoldSeekExitOpt(EscapeState state) {
		this.state = state;
	}
	
	/**
	 * Map that keeps track of the each node in the cavern and their current gold level at any point in time.
	 * The key is the node, the value is the gold level at any point in time. 
	 */
	Map<Node, Integer> nodeGoldMap = new HashMap<>();
	
	/**
	 *  Map that contains each node in the graph and its associated navigable neighbors. 
	 *  Use for navigation and to improve algorithm performance of this class.  
	 */
	Map<Node, Collection<Node> > neighborsMap = new HashMap<>();
	
	/**
	 * Map of every node and metadata about each node. The metadata is a MetaNodeOPath 
	 * (refer to @MetaNodeOPath class for more details) that contains information about a particular Node. 
	 * This is being used to store information about a node's minimum distance to the exit node, 
	 * and the optimal path based on Dijkstra's algorithm between any node and the exit node.
	 */
	Map<Node, MetaNodeOPath> superEvalNodeMap = new HashMap<>();
	
	/**
	 * Map that keeps track on how many times a node has been visited. It is used to facilitate
	 * and optimize navigation.
	 */
	Map<Node, Integer> visitedNodeMap = new HashMap<>();
	
	
	/**
	 * Predicate use to filter out any Nodes that are non-traversable or not open.
	 */
	Predicate<Node> navigable = p -> p.getTile().getType().isOpen() == true;
	
	
	/**
	 * Predicate that compares the time remaining in the cavern vs. the time that will take from the current node to 
	 * a particular node and from that particular node to the exit. It is use to filter out any nodes whose location
	 * won't allow to reach the exit node on-time.
	 */
	Predicate<Node> reachable = p -> superEvalNodeMap.get(p).getDistance() < (state.getTimeRemaining()-getDistanceToNode(p));

	/**
	* Returns the distance between the state's current node and a particular node. If the node is
	* a neighbor it returns the length of the edge between them.
	* @param inputNode. A node whose distance we want to estimate from the state's current node.
	* @return int. The distance between the state's current node and the node specified as a parameter.
	* If the parameter node is a neighbor, it returns the length of the edge between them. 
	* If the parameter node is not a neighbor, it returns an infinitive value (Integer.MAX_VALUE).
	*/
	public int getDistanceToNode (Node inputNode){
		for (Edge e : state.getCurrentNode().getExits()){
			Node other = e.getOther(state.getCurrentNode());
			if (other == inputNode){
				return e.length;
			}
		}
		return Integer.MAX_VALUE;	
	}
	
	
	/**
	 * Provides all the navigable neighbors of node. Navigable means any node that is open.
	 * This method significantly improves the performance of the EscapeStrategy. 
	 * @param node. Any node whose neighbor's are to be determined.
	 * @return a collection of nodes. A collection of nodes that represents  all the neighbor's
	 * of the node specified as a parameter.
	 */
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
	
	
	
	
	/**
	 * Populates all the different data structures being used in the class.
	 */
	public void populateMaps(){
		
		/*
		 * This stream excludes any non-navigable nodes. It populates a map of each node and the original
		 * amount of gold in that node. This map is to be leveraged in the seek gold navigation process.
		 */
		state.getVertices().stream().filter(navigable).forEach((n) ->nodeGoldMap.put(n,n.getTile().getOriginalGold()));
		
		/*
		 * This stream excludes any non-navigable nodes. It populates a map of each node and its neighbors. This is being
		 * used in the seek gold navigation process to get a hold of any node's neighbors and evaluate them.
		 */
		state.getVertices().stream().filter(navigable).forEach((n) ->neighborsMap.put(n,getNeighbors(n)));
		
	
		/*
		 * This stream excludes any non-navigable nodes. It populates a map of each node and its metadata node. 
		 */
		state.getVertices().stream().filter(navigable).forEach((n) ->superEvalNodeMap.put(n,new MetaNodeOPath(n,new OptimalPathDijkstra(state,n,state.getExit()))));
		
		/*
		 * This stream excludes any non-navigable nodes. It populates a nap of each node and a counter of the visits to that
		 * node. Initially, it is set up to zero for each node.
		 */
		state.getVertices().stream().filter(navigable).forEach((n) ->visitedNodeMap.put(n,0));
		
					
	}
	
	
	/**
	 * Implementation of method from EscapeStrategy interface. Description of procedures:
	 * - Ensures that there is enough time to reach the exit node before moving to any new node. 
	 * - Compares two nodes based on their visited frequency. Favors movement to less visited nodes.
	 * - If none of the current node's neighbors are reachable, retrieves the metadata of the current 
	 * node to find optimal path to exit and executes path. 
	 */
	@Override
	public void execute(){
		
		
		populateMaps();
		/*
		 * While statement being leveraged to ensure there is enough time to keep looping through the cavern.
		 */
		while (state.getTimeRemaining() >= superEvalNodeMap.get(state.getCurrentNode()).getDistance() ){
			
			/*
			 * Returns an optional node who is a neighbor node of the state's current node, that is reachable
			 * (refer to the reachable predicate for definition) and selects the node that has been visited
			 * the least in the past. This is to minimize navigating through a node that has been visited.  
			 */
			Optional<Node> nextNode = neighborsMap.get(state.getCurrentNode()).stream().filter(reachable)
			.min((p1,p2)-> visitedNodeMap.get(p1).compareTo(visitedNodeMap.get(p2)));
						
			
			/*
			 * If a neighbor node is reachable. Moves to that node, updates visitedNodeMap data structure, 
			 * collects gold if any, updates goldMap. If not, it heads to the exit node by following the
			 * optimal minimal path.
			 */
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
