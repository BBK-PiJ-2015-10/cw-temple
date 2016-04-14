package student;

import java.util.Objects;
import game.Node;

/**
 * @author YasserAlejandro
 * A class to hold Metadata information about a particular Node. It stores the distance of the Node
 * to the Exit node of the graph and its OptimalPath to that Exit node. 
 */
public class SuperEvalNode {
	
	/*
	 * This is the Node associated with this Metadata class.
	 */
	private Node node;
	
	/*
	 * This is the distance between the Node from this SuperEvalNode and the Exit Node of the graph.
	 */
	private Integer distanceToExit;
	
	/*
	 * This is the OptimalPath between the from the Node associated with this SuperEvalNode and the Exit Node of the graph.
	 */
	private OptimalPath escapeMinPath;
	
	/*
	 * This is a getter method that returns the Node associated to this SuperEvalNode.
	 */
	public Node getNode(){
		return this.node;
	}
	
	/*
	 * This returns the distance between this Node associated with this SuperEvalNode and the Exit Node of the graph.
	 */
	public Integer getDistanceToExit(){
		return this.distanceToExit;
	}
	
	/*
	 * This returns the OptimalPath between the from the Node associated with this SuperEvalNode and the Exit Node of the graph.
	 */
	public OptimalPath getEscapeMinPath(){
		return this.escapeMinPath;
	}
	
	/*
	 * This is a basic constructor.
	 */
	public SuperEvalNode(){
	}
	
	/*
	 * This is a constructor that takes a Node and an OptimalPath.
	 * It sets the distance based value of the calculateminpath of the OptimalPath.
	 */
	public SuperEvalNode(Node node,OptimalPath path ){
		this.setNode(node);
		this.setOptimalFromToPath(path);
		this.setDistanceToExit(this.escapeMinPath.calculateminpath());
	}	
		
	/*
	 * This is a setter method for the node.
	 */
	public void setNode(Node node){
		this.node = node;
	}
	
	/*
	 * This is a setter for the escapeMinPath.
	 */
	public void setOptimalFromToPath(OptimalPath escapeMinPath){
		this.escapeMinPath = escapeMinPath;
	}
	
	/*
	 * This is a setter for the distanceToExit.
	*/
	public void setDistanceToExit(int distanceToExit){
		this.distanceToExit = distanceToExit;
	}
	
	
	/*
	 *A local implementation of the hashCode method done to facilitate Testing.
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
