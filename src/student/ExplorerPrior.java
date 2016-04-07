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
        
              
            Long next;
       
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
    	
    	OptimalPath optimal = new OptimalPath(state);
    	optimal.run();
    	
    	/*
    	
    	PriorityQueue<SuperNode> frontier = new PriorityQueueImpl<>();
    	
    	Map<Long, SuperNode> mapper = new HashMap<>();
    	
    	Map<Long, Integer> pathWeights = new HashMap<>();
    	
    	Stack<Node> sequence = new Stack<>();
    	
    	
    	Predicate<Node> navigable = p -> p.getTile().getType().isOpen() == true;
    	
    	
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
    
    	
      sequence.push(state.getExit());	
      SuperNode temp = mapper.get(state.getExit().getId());
      while (temp != mapper.get(state.getCurrentNode().getId()) )
	      {
		      sequence.push(temp.getPrede());
		      temp = mapper.get(temp.getPrede().getId());
	      }
      
      
      System.out.println("I need to reach " +state.getExit());
      
      System.out.println("It's predecessor is " +mapper.get(state.getExit().getId()).getPrede());
    	
      System.out.println("I am starting from " +state.getCurrentNode());
      
      for (Node n:  state.getCurrentNode().getNeighbours()) {
  		System.out.println("My neighbours are " +n);
  	  }
    
      sequence.pop();
      
      //System.out.println("I am popping" +sequence.pop());
    
     while (!sequence.isEmpty()){
    	  Node mover = sequence.pop();
    	  System.out.println("I am moving to " +mover);
    	  state.moveTo(mover);
    	  if (state.getCurrentNode().getTile().getOriginalGold() > 0) {
    		  state.pickUpGold();
    	  }
     }
      
    	
    	System.out.println("This is going to work");
    	
    	*/
    	
    	
    }
    
    

    
    
}

