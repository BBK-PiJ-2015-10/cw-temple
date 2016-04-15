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
 * 
 * A class that provides an implementation of an EscapeStrategy.
 * It is informed in the sense that it has four data structures (Maps) to gather as much information
 * as possible about all the nodes in the cavern and leverage this information in the navigation process.
 * It is GoldSeek oriented in the sense that it looks for as much gold as possible until it knows that it
 * needs to head to the Exit node to avoid being trapped in the cavern.
 * It is ExitOptimizer in the sense that once it knows that it needs to head to the Exit node, it uses
 * the minimal path strategy to reach that Exit node.
 */
public class EscapeInformedGoldSeekExitOpt implements EscapeStrategy {

	/**
	 * This is the EscapeState state injected in the class that will facilitate the navigation and whose
	 *methods are leveraged different sections of this class.
	 */
	private EscapeState state;
	
	/**
	 *This is the Constructor, takes an EscapteState as an argument.
	 */
	public EscapeInformedGoldSeekExitOpt(EscapeState state) {
		this.state = state;
	}
	
	/**
	 * This is a Map that keeps track of the each Node and its gold level at a particular point in time. 
	 */
	Map<Node, Integer> nodeGoldMap = new HashMap<>();
	
	/**
	 *  This is a map that contains each Node in the graph and its associated neighbors. It is used to 
	 *  navigate thru the graph and be able to easily retrieve all the neighbor's of a node.  
	 */
	Map<Node, Collection<Node> > neighborsMap = new HashMap<>();
	
	/**
	 * This is a Map of every node and Metadata about that particular node. The Metadata is a SuperEvalNode 
	 * (refer to @SuperEvalNode class for more details) that contains information about a particular Node. 
	 * This is  being used to store information about a Node's minimum distance to the Exit node, 
	 * the optimal path based on Dijkstra's algorithm between any node and the state exit's node.
	 */
	Map<Node, SuperEvalNode> superEvalNodeMap = new HashMap<>();
	
	/**
	 * This is a Map that keeps track on how many times a Node has been visited. It is used to facilitate
	 * and optimize navigation; by minimizing the revisiting of nodes already visited based on their
	 * frequency of visits.
	 */
	Map<Node, Integer> visitedNodeMap = new HashMap<>();
	
	
	/**
	 * This navigable predicate is used to filter out any Nodes that are non-traversable (open).
	 */
	Predicate<Node> navigable = p -> p.getTile().getType().isOpen() == true;
	
	
	/**
	 * This reachable predicate compares the time remaining, vs. the time that will take from the current Node to 
	 * a particular node and from that particular Node to the Exit. It is leveraged in this class to determine
	 * if a Node can be reached and still allow time to escape on time.
	 */
	Predicate<Node> reachable = p -> superEvalNodeMap.get(p).getDistanceToExit() < (state.getTimeRemaining()-getDistanceToNode(p));
	

	/**
	* This method returns a distance between the state's current node and a particular Node. If the node is
	* a neighbor it returns the length of the edge between them. If the node is not a neighbor it returns
	* an infinitive value (Integer.MAX_VALUE). The latter is the express that the node is not directly 
	* connected to the current state node.
	*  @param node whose distance we want to estimate from the state's current node.
	*  @return the distance between the state's current node and the node specified in the param.
	*/
	public int getDistanceToNode (Node node2){
		for (Edge e : state.getCurrentNode().getExits()){
			Node other = e.getOther(state.getCurrentNode());
			if (other == node2){
				return e.length;
			}
		}
		return Integer.MAX_VALUE;	
	}
	
	
	/**
	 * This method is used to gather and provide all the navigable neighbors of any Node.
	 * Navigable means any node that is open.
	 * I could have used the getNeighbors method from the Node class and filter to Navigable ones.
	 * However, the improvement in the performance by the use of this method is significant in the
	 * gold collection process. 
	 * @param any node whose neighbor's we wish to determine
	 * @return a Collection of Nodes with all the neighbor's of the node specified on @param.
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
	 * This method is to populate all the different data structures being used in this class.
	 */
	public void populateMaps(){
		
		/*
		 * This stream excludes any non-navigable nodes. It populates a Map of each Node and the original
		 * amount of gold in that Node. This Map is to be leveraged in the seek gold navigation process.
		 */
		state.getVertices().stream().filter(navigable).forEach((n) ->nodeGoldMap.put(n,n.getTile().getOriginalGold()));
		
		/*
		 * This stream excludes any non-navigable nodes. It populates a Map of each Node and its neighbors. This is being
		 * used in the seek gold navigation process to get a hold of any node's neighbors and evaluate them.
		 */
		state.getVertices().stream().filter(navigable).forEach((n) ->neighborsMap.put(n,getNeighbors(n)));
		
	
		/*
		 * This stream excludes any non-navigable nodes. It populates a Map of each Node and its Metadata Node. 
		 */
		state.getVertices().stream().filter(navigable).forEach((n) ->superEvalNodeMap.put(n,new SuperEvalNode(n,new OptimalPathDijkstra(state,n,state.getExit()))));
		
		/*
		 * This streams excludes any non-navigable nodes. It populates a Map of each Node and a counter of the visits to that
		 * node. Initially, it is set up to zero.
		 */
		state.getVertices().stream().filter(navigable).forEach((n) ->visitedNodeMap.put(n,0));
		
					
	}
	
	
	/**
	 * This is the actual execution of the strategy of this class. 
	 * It first ensures that there is enough time to reach the exit node before moving to any new node. 
	 * It compares two nodes based on their visited frequency. It favors the movement into non-visited 
	 * and less visited nodes. If none of the neighbor's nodes of the current state's node is reachable
	 * (meaning, no enough time to reach it and reach the exit node), then the method evokes the Metadata of
	 * the current node to find the optimal path to the exit Node and follows that path. 
	 */
	@Override
	public void execute(){
		
		
		populateMaps();
		/*
		 * While statement being leveraged to ensure there is enough time to keep looping through the cavern.
		 */
		while (state.getTimeRemaining() >= superEvalNodeMap.get(state.getCurrentNode()).getDistanceToExit() ){
			
			/**
			 * Returns an Optional<Node> neighbor node of the state's current Node, that is reachable
			 * (refer to the reachable predicate for definition) and select the Node that has been visited
			 * the least in the past. This is to avoid navigating through a node that has been visited (no gold left)
			 * also avoid being stuck on revisiting already visited nodes.   
			 */
			Optional<Node> nextNode = neighborsMap.get(state.getCurrentNode()).stream().filter(reachable)
			.min((p1,p2)-> visitedNodeMap.get(p1).compareTo(visitedNodeMap.get(p2)));
						
			
			/*
			 * If a neighbor node is reachable. Moves to node, updates visitedNodeMap data structure, 
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
