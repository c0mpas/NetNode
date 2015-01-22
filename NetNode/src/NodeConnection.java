
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NodeConnection {
	
	private Node node;
	private NodeInfo partner;
	boolean listening;
	
	public NodeConnection(Node node, NodeInfo partner) {
		this.node = node;
		this.partner = partner;
	}
	
	public void listen() {		
		listening = true;
		Thread listenerThread = new Thread() {
			public void run() {
				Socket socket;
				ServerSocket listener;
				ObjectInputStream inputStream;
				try {
					listener = new ServerSocket(node.getNodeInfo().getPort());
					try {
					    while (listening) {
					        socket = listener.accept();
					        inputStream = new ObjectInputStream(socket.getInputStream());
					        NodeMessage message = (NodeMessage) inputStream.readObject();
					        // process message
					        node.processMessage(message);
					    }
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} finally {
						listener.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		listenerThread.start();
	}
	
	public void send() {}
	
	// return string representing this node
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nconnection(").append(partner).append(")");
		return sb.toString();
	}
}
