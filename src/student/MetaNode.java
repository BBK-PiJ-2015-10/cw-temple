package student;

import java.util.Objects;

import game.Node;

/**
 * @author ypalac01
 * An abstract class that holds metadata about a node. It stores an encapsulated node,
 * and the distance of that node to another node.
 */
public abstract class MetaNode {
	
	/**
	 * This is the node whose metadata is being captured in this class.
	 */
	private Node node;
	
	/**
	 * This field is the distance between the node field in this class and an another node.
	 */
	private Integer distance;
	
	
	/**
	 * @return Node. Returns the node field of this class.
	 */
	public Node getNode(){
		return this.node;
	}
	
	/**
	 * @return Integer . Returns the distance field of this class.
	 */
	public Integer getDistance(){
		return this.distance;
	}
	
	
	/**
	 * This is a basic constructor.
	 */
	public MetaNode(){		
	}
	
	/**
	 * Comprehensive constructor with all the fields in the class.
	 * @param node . The node encapsulated in this class.
	 * @param distance . The distance between encapsulated node and another node.
	 */
	public MetaNode(Node node, Integer distance){
		this.setMetaNode(node,distance);
	}
	
	/**
	 * Sets the values for node, distance, and predecessor fields.
	 * @param node . The node encapsulated in this class.
	 * @param distance . The distance between encapsulated node and another node.
	 */
	public void setMetaNode(Node node, Integer distance){
		this.setNode(node);
		this.setDistance(distance);
	}
	
	/**
	 * Sets the value for the node field in this class.
	 * @param node . The node encapsulated in this class.
	 */
	public void setNode(Node node){
		this.node = node;
	}
	
	/**
	 * Sets the value for the distance field in this class.
	 * @param distance . The distance between encapsulated node and another node.
	 */
	public void setDistance(Integer distance){
		this.distance= distance;
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
        if (!(ob instanceof MetaNodePre)) {
            return false;
        }
        return this.getNode().getId() == ((MetaNode) ob).getNode().getId();
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
