package student;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;

import game.ExplorationState;
import game.NodeStatus;



public class SmartExploration {
	
	private ExplorationState state;

	public SmartExploration(ExplorationState state){
		this.state = state;
	}
	
	private Set<Long> seen = new LinkedHashSet<>();
	
	private Stack<Long> compass = new Stack<>();
	
	//private Map<Long, Integer> visitsTracker = new HashMap<>();
	

	
	public void run(){
		

    while (state.getDistanceToTarget() != 0) {
        
  
    	
    	//if (!visitsTracker.containsKey(state.getCurrentLocation())){
    		//visitsTracker.put(state.getCurrentLocation(),1);
    	//}
    	
        compass.push(state.getCurrentLocation());
        seen.add(state.getCurrentLocation());
        
        //if (visitsTracker.containsKey(state.getCurrentLocation())){
        	//visitsTracker.put(state.getCurrentLocation(),visitsTracker.get(state.getCurrentLocation())+1);
        //}
        //else {
        	//visitsTracker.put(state.getCurrentLocation(),1);
        //}
        
        
           
        Predicate<NodeStatus> unvisited = p -> seen.contains(p.getId()) == false;
        
        //Predicate<NodeStatus> visited = p -> seen.contains(p.getId()) == true;
        
    
        Long next;
   
        Optional<NodeStatus> nextpossible = state.getNeighbours().stream().filter(unvisited).min((p1,p2)-> p1.compareTo(p2));
         
        if (nextpossible.isPresent()){
        	next = nextpossible.get().getId();
        }
        else {
        	
        	//state.getNeighbours().stream().forEach((n)-> System.out.println(seen.contains(n.getId())));
        	
        	//Optional<NodeStatus> otherpossible = state.getNeighbours().stream().filter(visited).min((p1,p2)-> visitsTracker.get(p1.getId()).compareTo(visitsTracker.get(p2.getId())));
        	//next = state.getNeighbours().stream().min((p1,p2)-> visitsTracker.get(p1.getId()).compareTo(visitsTracker.get(p2.getId()))).get().getId();
        	
        	//if (otherpossible.isPresent()){
            	//next = otherpossible.get().getId();
            	//compass.pop();
            //}
        	
        	//else {
        		compass.pop();
            	next = compass.pop();
        		
        	//}
        }
        
       // System.out.println("Moving to tile with id: " + next);
        //System.out.println("Moving from position: " + state.getCurrentLocation());
        state.moveTo(next);
        //System.out.println("\t to: " + state.getCurrentLocation());
        
       
    }
	
	}

}
