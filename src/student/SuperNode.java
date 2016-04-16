package student;

import java.util.Objects;
import game.Node;

/**
 * @author ypalac01
 * A class that holds Metadata about a node. It stores an encapsulated node,
 * the distance of that node to a target node, and the precessor of the encapsulated
 * node on its path to the target node.
 */
public class SuperNode {
	
	/**
	 * This is the node whose Metadata is being captured in this class.
	 */
	private Node node;
	
	/**
	 * This field is the distance between the node field in this class and a target node.
	 */
	private Integer distance;
	
	/**
	 * This field is the predecessor node of the node in this class.
	 */
	private Node predecessor;
	
	/**
	 * @return Node. Returns the node field of this class.
	 */
	public Node getNode(){
		return this.node;
	}
	
	/**
	 * @return Integer. Returns the distance field of this class.
	 */
	public Integer getDistance(){
		return this.distance;
	}
	
	
	/**
	 * @Return Node. Returns the predecessor node field of this class.
	 */
	public Node getPrede(){
		return this.predecessor;
	}
	
	/**
	 * This is a basic constructor.
	 */
	public SuperNode(){		
	}
	
	/**
	 * Comprehensive constructor with all the fields in the class.
	 * @param Node. The node encapsulated in this class.
	 * @param Integer. The distance between encapsulated node and a target node.
	 * @param Node. The predecessor of the encapsulated node.
	 */
	public SuperNode(Node node, Integer distance, Node predecessor){
		this.setSuperNode(node,distance,predecessor);
	}
	
	/**
	 * Sets the values for node, distance, and predecessor fields.
	 * @param Node. The node encapsulated in this class.
	 * @param Integer. The distance between encapsulated node and a target node.
	 * @param Node. The predecessor of the encapsulated node.
	 */
	public void setSuperNode(Node node, Integer distance, Node predecessor){
		this.setNode(node);
		this.setDistance(distance);
		this.setPredecessor(predecessor);
	}
	
	/**
	 * Sets the value for the node field in this class.
	 * @param Node. The node encapsulated in this class.
	 */
	public void setNode(Node node){
		this.node = node;
	}
	
	/**
	 * Sets the value for the distance field in this class.
	 * @param Integer. The distance between encapsulated node and a target node.
	 */
	public void setDistance(Integer distance){
		this.distance= distance;
	}
	
	/**
	 * Sets the value for the predecessor field in this class.
	 * @param Node. The predecessor of the encapsulated node in this class.
	 */
	public void setPredecessor(Node predecessor){
		this.predecessor = predecessor;
	}
	
	/**
	 * Sets the value for the distance and predecessor fields in this class.
	 * @param Integer. The distance between encapsulated node and a target node.
	 * @param Node. The predecessor of the encapsulated node in this class.
	 */
	public void setDistancePredecessor(Integer distance,Node predecessor){
		this.setDistance(distance);
		this.setPredecessor(predecessor);
	}
	
	
	/**
	 * A local implementation of the equals method done to facilitate testing.
	 * @return boolean. Returns true if the id of node encapsulated in the objects
	 * being compared are equal.
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
	
	/**
	 * A local implementation of the hashCode method done to facilitate testing.
	 * @return int. Returns the hashcode of the encapsulate node's id.
	 */
	@Override
    public int hashCode() {
        return Objects.hash(this.getNode().getId());
    }
	
	

}
