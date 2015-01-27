
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	public synchronized void listen() {		
		listening = true;
		Thread listenerThread = new Thread() {
			public void run() {
				Socket socket = null;
				ServerSocket listener = null;
				ObjectInputStream inputStream = null;
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
						inputStream.close();
						listener.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		};
		listenerThread.start();
	}
	
	public synchronized void stopListening() {		
		listening = false;
	}
	
	public synchronized void send(NodeMessage message) {
		// add sender and receiver information to message
		message.setSender(node.getNodeInfo());
		message.setReceiver(partner);
		// send message in new thread
		Thread senderThread = new Thread() {
			public void run() {
				try {
					Socket socket = new Socket(partner.getHost(), partner.getPort());
					ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
					// send message
					outputStream.writeObject(message);
					// close connections
					outputStream.close();
					socket.close();
				} catch (IOException e) {
					Node.log("node " + String.valueOf(partner.getId()) + " unreachable");
				}
			}
		};
		senderThread.start();
	}
	
	// return partner
	public NodeInfo getPartner() {
		return this.partner;
	}
	
	// return string representing this node
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nconnection(").append(partner).append(")");
		return sb.toString();
	}
}
