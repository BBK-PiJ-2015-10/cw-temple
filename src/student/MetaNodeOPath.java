package student;

import game.Node;

/**
 * @author ypalac01
 * 
 * A class that extends MetaNode. It stores the optimal path between the
 * encapsulated node in the MetaNode class and a target node.
 */
public class MetaNodeOPath extends MetaNode {
	
	
	/**
	 * This is the OptimalPath trajectory between the from the node encapsulated in the
	 * MetaNode class and a target node.
	 */
	private OptimalPath optimalPath;
	
	/**
	 * @return OptimalPath . Returns the optimal path trajectory between the node
	 * encapsulated in the MetaNode class and a target node.
	 */
	public OptimalPath getEscapeMinPath(){
		return this.optimalPath;
	}
	
	/**
	 * Basic constructor.
	 */
	public MetaNodeOPath(){
		super();
	}
	
	/**
	 * Comprehensive constructor.
	 * @param node . The node encapsulated in the MetaNode class.
	 * @param optimalPath . The optimal path trajectory between the encapsulated node
	 * and a target node.
	 */
	public MetaNodeOPath(Node node,OptimalPath optimalPath ){
		this.setNode(node);
		this.setOptimalFromToPath(optimalPath);
		this.setDistance(this.optimalPath.calculateoptimalpath());
	}	
		
	/**
	 * Sets the value for the escapeMinPath field in this class.
	 * @param optimalPath . The optimal path between the node
	 * encapsulated in the MetaNode class and a target node.
	 */
	public void setOptimalFromToPath(OptimalPath optimalPath){
		this.optimalPath = optimalPath;
	}
	

}
