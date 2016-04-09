package student;

import java.util.Objects;

import game.Node;


public class SuperEvalNode {
	
	private Node node;
	
	private Integer distanceToExit;
		
	private OptimalPath escapeMinPath;
		
	public Node getNode(){
		return this.node;
	}
	
	public Integer getDistanceToExit(){
		return this.distanceToExit;
	}
	
	public OptimalPath getEscapeMinPath(){
		return this.escapeMinPath;
	}
	
	
	public SuperEvalNode(){
		
	}
	
	public SuperEvalNode(Node node,OptimalPath path ){
		this.setNode(node);
		this.setOptimalFromToPath(path);
		this.setDistanceToExit(this.escapeMinPath.calculateminpath());
		
	}	
		
	
	public void setNode(Node node){
		this.node = node;
	}
	
		
	public void setOptimalFromToPath(OptimalPath escapeMinPath){
		this.escapeMinPath = escapeMinPath;
	}
	
	public void setDistanceToExit(int distanceToExit){
		this.distanceToExit = distanceToExit;
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
