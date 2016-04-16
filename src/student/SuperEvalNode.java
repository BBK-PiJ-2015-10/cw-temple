package student;

import java.util.Objects;
import game.Node;

/**
 * @author ypalac01
 * 
 * A class that holds Metadata about a node. It stores an encapsulated node,
 * the minimal distance of that node to a target node, and the minimal path
 * trajectory from the encapsulated node to the target node.
 */
public class SuperEvalNode {
	
	/**
	 *  This is the node whose Metadata is being captured in this class.
	 */
	private Node node;
	
	/**
	 * This field is the minimal path distance between the node field in this class and a target node.
	 */
	private Integer distanceToExit;
	
	/**
	 * This is the OptimalPath trajectory between the from the node encapsulated in this class and a target node.
	 */
	private OptimalPath escapeMinPath;
	
	/**
	 * @return Node. Returns the node field of this class.
	 */
	public Node getNode(){
		return this.node;
	}
	
	/**
	 * @return Integer. Returns minimal distance between the encapsulated node in this class
	 * and a target node.
	 */
	public Integer getDistanceToExit(){
		return this.distanceToExit;
	}
	
	/**
	 * @return OptimalPath. Returns the minimal path trajectory between the from the node
	 * encapsulated in this class and a target node.
	 */
	public OptimalPath getEscapeMinPath(){
		return this.escapeMinPath;
	}
	
	/**
	 * Basic constructor.
	 */
	public SuperEvalNode(){
	}
	
	/**
	 * Comprehensive constructor.
	 * @param Node. The node encapsulated in this class.
	 * @param OptimalPath. The optimal path trajectory between the encapsulated node
	 * and a target node.
	 */
	public SuperEvalNode(Node node,OptimalPath path ){
		this.setNode(node);
		this.setOptimalFromToPath(path);
		this.setDistanceToExit(this.escapeMinPath.calculateminpath());
	}	
		
	/**
	 * Sets the value for the node field in this class.
	 * @param Node. The node encapsulated in this class.
	 */
	public void setNode(Node node){
		this.node = node;
	}
	
	/**
	 * Sets the value for the escapeMinPath field in this class.
	 * @param OptimalPath. The optimal path between the node
	 * encapsulated in this class and a target node.
	 */
	public void setOptimalFromToPath(OptimalPath escapeMinPath){
		this.escapeMinPath = escapeMinPath;
	}
	
	/**
	 * Sets the value for the distance field in this class.
	 * @param int. The minimal path distance between encapsulated node
	 * and a target node.
	 */
	public void setDistanceToExit(int distanceToExit){
		this.distanceToExit = distanceToExit;
	}
	
	
	/**
	 *A local implementation of the equal method done to facilitate Testing.
	 */
	@Override
    public boolean equals(Object ob) {
        if (ob == this) {
            return true;
        }
        if (!(ob instanceof SuperEvalNode)) {
            return false;
        }
        return this.getNode().getId() == ((SuperEvalNode) ob).getNode().getId();
    }
	
	/**
	 *A local implementation of the hashCode method done to facilitate Testing.
	 */
	@Override
    public int hashCode() {
        return Objects.hash(this.getNode().getId());
    }
	


}
