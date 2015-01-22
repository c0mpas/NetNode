package util;

public class NodeConnection {
	
	private NodeInfo node;
	
	
	private NodeConnection() {}
	
	public NodeConnection(NodeInfo node) {
		this.node = node;
	}
	
	// return string representing this node
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nconnection(").append(node).append(")");
		return sb.toString();
	}
}
