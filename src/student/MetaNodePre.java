package student;

import game.Node;

/**
 * @author ypalac01
 * A class that extends MetaNode. It stores the preceding node that needs to be traversed 
 * before the encapsulated node from MetaNode class to follow a trajectory
 * to a target node.
 */
public class MetaNodePre extends MetaNode {
		
	
	/**
	 * This field is the predecessor node of the encapsulated node in the MetaNode class.
	 */
	private Node predecessor;
	
	/**
	 * @return Node. Returns the predecessor node field of this class.
	 */
	public Node getPrede(){
		return this.predecessor;
	}
	
	/**
	 * This is a basic constructor.
	 */
	public MetaNodePre(){
		super();
	}
	
	/**
	 * Comprehensive constructor with all the fields in the class.
	 * @param node . The node encapsulated in the MetaNode class.
	 * @param distance . The distance between encapsulated node and a target node.
	 * @param predecessor . The predecessor of the encapsulated node.
	 */
	public MetaNodePre(Node node, Integer distance, Node predecessor){
		super(node,distance);
		setPredecessor(predecessor);
	}
	
	/**
	 * Sets the values for node, distance, and predecessor fields.
	 * @param node . The node encapsulated in the MetaNode class.
	 * @param distance . The distance between encapsulated node and a target node.
	 * @param predecessor . The predecessor of the encapsulated node.
	 */
	public void setSuperNode(Node node, Integer distance, Node predecessor){
		this.setMetaNode(node,distance);
		this.setPredecessor(predecessor);
	}
	
	/**
	 * Sets the value for the predecessor field in this class.
	 * @param predecessor . The predecessor of the encapsulated node in the MetaNode class.
	 */
	public void setPredecessor(Node predecessor){
		this.predecessor = predecessor;
	}
	
	/**
	 * Sets the value for the distance and predecessor fields in this class.
	 * @param distance . The distance between encapsulated node and a target node.
	 * @param predecessor . The predecessor of the encapsulated node in the MetaNode class.
	 */
	public void setDistancePredecessor(Integer distance,Node predecessor){
		this.setDistance(distance);
		this.setPredecessor(predecessor);
	}
	
	
}
