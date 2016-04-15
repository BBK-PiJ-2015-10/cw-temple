package student;

import java.util.Objects;
import game.Node;

/**
 * @author YasserAlejandro
 * 
 * This is a class created to hold Metadata about a Node. It was created to facilitate the
 * implementation of the Djistra algorithm to find and navigate through the optimal path between two nodes.
 */
public class SuperNode {
	
	/*
	 *This is the Node whose Metadata we will capture
	 */
	private Node node;
	
	/*
	 *This field is to capture the distance between the Node field in this SuperNode and a Target Node
	 */
	private Integer distance;
	
	/*
	*This field captures the predecessor of the Node in this SuperNode. This predecessor node needs to
	*be visited before the Node in the SuperNode to go from the starting point to the target point.
	*/
	private Node predecessor;
	
	/*
	 *This is a getter method that returns the Node field in this SuperNode.
	 */
	public Node getNode(){
		return this.node;
	}
	
	/*
	 *This method returns the distance between this SuperNode Node and the starting node.
	 */
	public Integer getDistance(){
		return this.distance;
	}
	
	
	/*This getter method returns the Predecessor Node field associated to this SuperNode.
	 *
	 */
	public Node getPrede(){
		return this.predecessor;
	}
	
	/*
	 *This is a basic constructor.
	 */
	public SuperNode(){		
	}
	
	/*
	 *This is a more comprehensive constructor with all the fields in the class.
	 */
	public SuperNode(Node node, Integer distance, Node predecessor){
		this.setSuperNode(node,distance,predecessor);
	}
	
	/*
	 *This is a setter for node, distance, and predecessor fields.
	 */
	public void setSuperNode(Node node, Integer distance, Node predecessor){
		this.setNode(node);
		this.setDistance(distance);
		this.setPredecessor(predecessor);
	}
	
	/*
	 * This is a setter for node.
	 */
	public void setNode(Node node){
		this.node = node;
	}
	
	/*
	 *This is a setter for the distance.
	 */
	public void setDistance(Integer distance){
		this.distance= distance;
	}
	
	/*
	 *This is a setter for the predecessor.
	 */
	public void setPredecessor(Node predecessor){
		this.predecessor = predecessor;
	}
	
	/*
	 *This is a setter for distance and predecessor.
	 */
	public void setDistancePredecessor(Integer distance,Node predecessor){
		this.setDistance(distance);
		this.setPredecessor(predecessor);
	}
	
	
	/*
	 *A local implementation of the equals method done to facilitate Testing.
	 */
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
	
	/*
	 *A local implementation of the hashCode method done to facilitate Testing.
	 */
	@Override
    public int hashCode() {
        return Objects.hash(this.getNode().getId());
    }
	
	

}
