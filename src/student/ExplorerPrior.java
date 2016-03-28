package student;

import game.Edge;
import game.EscapeState;
import game.ExplorationState;
import game.NodeStatus;
import game.Node;
import game.Tile;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

import java.util.stream.*;
import java.util.function.Predicate;
import java.util.Iterator;

public class ExplorerPrior {

    /**
     * Explore the cavern, trying to find the orb in as few steps as possible.
     * Once you find the orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb,
     * it will count as a failure.
     * <p>
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * <p>
     * At every step, you only know your current tile's ID and the ID of all
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles).
     * <p>
     * To get information about the current state, use functions
     * getCurrentLocation(),
     * getNeighbours(), and
     * getDistanceToTarget()
     * in ExplorationState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * <p>
     * Use function moveTo(long id) in ExplorationState to move to a neighboring
     * tile by its ID. Doing this will change state to reflect your new position.
     * <p>
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     *
     * @param state the information available at the current state
     */
    public void explore(ExplorationState state) {
        Set<Long> seen = new LinkedHashSet<>();
        Stack<Long> compass = new Stack<>();

        while (state.getDistanceToTarget() != 0) {
            
            // add current position to seen items
            compass.push(state.getCurrentLocation());
            seen.add(state.getCurrentLocation());
               
            Predicate<NodeStatus> unvisited = p -> seen.contains(p.getId()) == false;
            //Predicate<NodeStatus> lessmax = p -> p.getDistanceToTarget() < Integer.MAX_VALUE;
              
            Long next;
       
            //Optional<NodeStatus> nextpossible = state.getNeighbours().stream().filter(unvisited).filter(lessmax).min((p1,p2)-> p1.compareTo(p2));
            Optional<NodeStatus> nextpossible = state.getNeighbours().stream().filter(unvisited).min((p1,p2)-> p1.compareTo(p2));
            
                   
            
            if (nextpossible.isPresent()){
            	next = nextpossible.get().getId();
            }
            else {
            	compass.pop();
            	next = compass.pop();	
            }
            
           
            System.out.println("Moving to tile with id: " + next);
            System.out.println("Moving from position: " + state.getCurrentLocation());
            state.moveTo(next);
            System.out.println("\t to: " + state.getCurrentLocation());
            
           
        }
    }

    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * <p>
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
     * will return a collection of all nodes on the graph.
     * <p>
     * Note that time is measured entirely in the number of steps taken, and for each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * <p>
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * <p>
     * You will always have enough time to escape using the shortest path from the starting
     * position to the exit, although this will not collect much gold.
     *
     * @param state the information available at the current state
     */    
    public void escape(EscapeState state) {
    	
    	
    	SuperNode pratice = new SuperNode();
    	
    	
    	PriorityQueue<Node> frontier = new PriorityQueueImpl<>();
    	
    	Map<Long, Integer> pathWeights = new HashMap<>();
    	
    	Map<Node, Node> optimalpred = new HashMap<>();
    	
    	Map<Node, Node> optimalsucc = new HashMap<>();
    	
    	//Map<Long, Long> optimalpred = new HashMap<>();
    	
    	//Stack<Long> optimalpath = new Stack<>();
    	
    	Stack<Node> optimalpath = new Stack<>();
    	
    	
    	
    	//Predicate<Node> navigable = p -> p.getTile().getType().isOpen() == true;
    	  	
    	
    	Node current = state.getCurrentNode();
    	
    	
    	frontier.add(current, 0);
    	pathWeights.put(current.getId(), 0);
    	optimalpath.push(state.getExit());
    	
    	    	
    	while (frontier.size()!=0){
    		current = frontier.poll();
    		int cWeight = pathWeights.get(current.getId());
    		for (Edge e : current.getExits()){
    			Node other = e.getOther(current);
	    		if (other.getTile().getType().isOpen()) {
	    			int weightThroughOther = cWeight + e.length();
	    			Integer existingWeight = pathWeights.get(other.getId());
	    			if (existingWeight == null) {
	                    pathWeights.put(other.getId(), weightThroughOther);
	                    frontier.add(other, weightThroughOther);
	                    optimalpred.put(other, current);
	                    optimalsucc.put(current, other);
	                }
	    			else if (weightThroughOther < existingWeight) {
	    				pathWeights.put(other.getId(), weightThroughOther);
	                    frontier.updatePriority(other, weightThroughOther);
	                    optimalpred.put(other, current);
	                    optimalsucc.put(current, other);
	    			}
	    		}
    		}
    	}
    	
    
    	
    	
    	
    	
    	System.out.println("This seems to be working");
    
    
    	//Long variable = null;
    	
    	//while (variable != state.getCurrentNode().getId()) {
    		//variable = optimalpath.peek();
    		//optimalpath.push(optimalpred.get(variable));
    	//}
    	
    	
    	System.out.println("In theory I am starting from " +state.getCurrentNode());
    	
    	System.out.println("My ultimate goal is to get to " +state.getExit());
    	
    	
    	while (state.getCurrentNode()!= state.getExit() ) {
    		
    		System.out.println("I am moving out from " +state.getCurrentNode());
    		
    		
    		for (Node n:  state.getCurrentNode().getNeighbours()) {
        		System.out.println("A neihbgor of current is" +n);
        	}
    		
    		System.out.println("Its optimal successor is " +optimalsucc.get(state.getCurrentNode()));
        	
    		state.moveTo(optimalsucc.get(state.getCurrentNode()));
    		
    	}
    	
    	
    	
    	
    	//System.out.println("Its optimal successor is " +optimalsucc.get(state.getCurrentNode()));
    	
    	
    	//System.out.println("I am first testing the size of pathWeights " +pathWeights.size());
    	
    	//System.out.println("I am first testing the size of optimalpred " +optimalpred.size());
    	
    	//System.out.println("I am first testing the size of frontier " +frontier.size());
    	
    	//System.out.println("I am first testing the size of optimalpath " +optimalpath.size());
    	
    	//System.out.println("I am first testing the optimal predecessor for " +optimalpred.get(state.getExit().getId()));
    		
    	
    	//System.out.println("I am first the first element on " +optimalpath.peek());
    	
    	
    	//System.out.println("I first popped node is " +optimalpath.pop().getId());
    	
    	System.out.println("Is this working or not");
    	
    	//optimalpath.pop();
    	
       //while (!optimalpath.isEmpty()){
    		
        	//Long moving = optimalpath.pop();
    		//System.out.println("Moving out of " );
    		//state.moveTo(optimalpred.get(moving));
    		
    	//}
    	
    	
    	
    	//state.getVertices().stream().forEach();;
    	
     	
    	
        //TODO: Escape from the cavern before time runs out
    }
    
    
    public class SuperNode {
    	
    	private Node node;
    	
    	private int distance;
    	
    	
    	private Node predecessor;
    	
    	
    	public Node getNode(){
    		return this.node;
    	}
    	
    	public int getDistance(){
    		return this.distance;
    	}
    	
    	public Node getPrede(){
    		return this.predecessor;
    	}
    	
    	
    	
    	public SuperNode(){
    		
    	}
    	
    	public SuperNode(Node node, int distance, Node predecessor){
    		this.setSuperNode(node,distance,predecessor);
    	}
    	
    	
    	public void setSuperNode(Node node, int distance, Node predecessor){
    		this.setNode(node);
    		this.setDistance(distance);
    		this.setPredecessor(predecessor);
    	}
    	
    	public void setNode(Node node){
    		this.node = node;
    	}
    	
    	
    	public void setDistance(int distance){
    		this.distance= distance;
    	}
    	
    	public void setPredecessor(Node predecessor){
    		this.predecessor = predecessor;
    	}
    	
    	
    	
    	
    	@Override
        public boolean equals(Object ob) {
            if (ob == this) {
                return true;
            }
            if (!(ob instanceof SuperNode)) {
                return false;
            }
            return this.getNode().getId() == ((SuperNode) ob).getNode().getId();
        }
    	
    	
    	@Override
        public int hashCode() {
            return Objects.hash(this.getNode().getId());
        }
    
    	
    	
    }
    
    
}
