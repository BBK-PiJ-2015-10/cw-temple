package student;

import java.util.Objects;

import game.Node;


public class SuperNode {
	
	private Node node;
	
	private Integer distance;
	
	
	private Node predecessor;
	
	
	public Node getNode(){
		return this.node;
	}
	
	public Integer getDistance(){
		return this.distance;
	}
	
	public Node getPrede(){
		return this.predecessor;
	}
	
	
	
	public SuperNode(){
		
	}
	
	public SuperNode(Node node, Integer distance, Node predecessor){
		this.setSuperNode(node,distance,predecessor);
	}
	
	
	public void setSuperNode(Node node, Integer distance, Node predecessor){
		this.setNode(node);
		this.setDistance(distance);
		this.setPredecessor(predecessor);
	}
	
	public void setNode(Node node){
		this.node = node;
	}
	
	
	public void setDistance(Integer distance){
		this.distance= distance;
	}
	
	public void setPredecessor(Node predecessor){
		this.predecessor = predecessor;
	}
	
	public void setDistancePredecessor(Integer distance,Node predecessor){
		this.setDistance(distance);
		this.setPredecessor(predecessor);
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
