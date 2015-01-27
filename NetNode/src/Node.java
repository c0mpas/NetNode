import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Node {
	
	private NodeInfo node;
	private String nodefile;
	private String graphfile;
	private ArrayList<NodeConnection> connections;
	private NodeConnection incoming;
	private static PrintStream out = System.out;
	private Scanner in;
	private ArrayList<Rumour> rumours;
	private boolean active;
	

	/*
	 * main method
	 */
	
	public static void main(String[] args) {
		log("=== NetNode ===");
		
		// create the command line parser
		CommandLineParser parser = new BasicParser();
		// get command line options
		Options options = CommandLineOptions.getDefaultOptions();
		
		int id = 0;
		String nodefile = null;
		String graphfile = null;
		
		try {
		    // parse the command line arguments
		    CommandLine line = parser.parse(options, args);
		    // check id
		    if (line.hasOption("id")) {
		        id = Integer.parseInt(line.getOptionValue("id"));
		    } else {
		    	throw new RuntimeException("missing id");
		    }
		    // check input file
		    if (line.hasOption("file")) {
		        nodefile = line.getOptionValue("file");
		    } else {
		    	throw new RuntimeException("input file");
		    }
		    // check graph file
		    if (line.hasOption("graph")) {
		        graphfile = line.getOptionValue("graph");
		    } else {
		    	throw new RuntimeException("graph file");
		    }
		    // create node
		    new Node(id, nodefile, graphfile).init();
		} catch (ParseException e) {
		    log("parse error: " + e.getMessage());
		} catch (RuntimeException e) {
		    log("parameter error: " + e.getMessage());
		    e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/*
	 * constructors
	 */
	
	public Node(int id, String nodefile, String graphfile) {
		if (id < 1) throw new RuntimeException("id invalid (must be > 0)");
		if ((nodefile == null) || (nodefile.isEmpty())) throw new RuntimeException("nodefile invalid");
		if ((graphfile == null) || (graphfile.isEmpty())) throw new RuntimeException("graphfile invalid");
		this.node = new NodeInfo(id);
		this.nodefile = nodefile;
		this.graphfile = graphfile;
	}
	

	/*
	 * control functions
	 */
	
	public void init() {
		this.active = true;
		this.connections = new ArrayList<NodeConnection>();
		ArrayList<NodeInfo> nodeInfoList = parseNodeInfo(this.nodefile);
		ArrayList<Pair> graph = parseGraph(this.graphfile);
		
		// find yourself in node list
		boolean found = false;
		for (NodeInfo current : nodeInfoList) {
			if (current.getId() == this.node.getId()) {
				this.node = current;
				found = true;
				break;
			}
		}
		if (!found) throw new RuntimeException("node is not part of the node network");
		
		// connect to neighbours
		for (Pair p : graph) {
			if (p.left == this.node.getId()) {
				// get info for this neighbour
				NodeInfo current = null;
				for (NodeInfo node : nodeInfoList) {
					if (node.getId() == p.right) current = node;
				}
				// establish connection to this neighbour
				connectTo(current);
			}
			if (p.right == this.node.getId()) {
				// get info for this neighbour
				NodeInfo current = null;
				for (NodeInfo node : nodeInfoList) {
					if (node.getId() == p.left) current = node;
				}
				// establish connection to this neighbour
				connectTo(current);
			}
		}
		
		// listen to incoming messages
		this.incoming = new NodeConnection(this, this.node);
		listen();
		
		log(this.toString());
		log("ready");
		
		// do stuff
		run();
		
		// finish
		quit();
		
	}

	private void listen() {
		// open server socket and listen
		this.incoming.listen();
	}
	
	private void run() {
		boolean menu = true;
		String input = null;
		in = new Scanner(System.in);
		int choice = 0;
		
		while (menu) {
			// show menu
			showMenu();
			// wait for user input
			
			try {
				input = in.nextLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// handle user input
			try {
				choice = Integer.parseInt(input);
			} catch (Exception e) {
				choice = 0;
			}
			
			switch (choice) {
				case 1 :	sendMessageInfo();
							break;
				case 2 :	// todo
							break;
				case 3 :	sendMessageRumour();
							break;
				case 4 :	sendMessageQuit();
							menu = false;
							waitForIt(1);
							break;
				default :	// do nothing
							break;
			}
		}
		
		in.close();
	}
	
	// stop this node
	public void quit() {
		log("shutting down");
		this.incoming.stopListening();
		log("node " + this.node.getId() + " terminated");
		waitForIt(1);
		System.exit(0);
	}
	
	// parse info from file to arraylist
	private ArrayList<NodeInfo> parseNodeInfo(String filename) {
		ArrayList<NodeInfo> result = new ArrayList<NodeInfo>();
		try {
			result = FileParser.parseInfo(filename);
		} catch (FileNotFoundException e) {
			log("unable to parse input file");
			quit();
		} catch (Exception e) {
			e.printStackTrace();
			quit();
		}
		// result = FileParser.getDummy();
		return result;
	}

	// parse graph from file to arraylist
	private ArrayList<Pair> parseGraph(String filename) {
		ArrayList<Pair> result = new ArrayList<Pair>();
		try {
			result = FileParser.parseGraph(filename);
		} catch (FileNotFoundException e) {
			log("unable to parse input file");
			quit();
		} catch (Exception e) {
			e.printStackTrace();
			quit();
		}
		// result = FileParser.getDummy();
		return result;
	}
	
	private void connectTo(NodeInfo node) {
		// establish connection to node
		NodeConnection connection = new NodeConnection(this, node);
		this.connections.add(connection);
	}

	
	/*
	 * getter methods
	 */
	
	// returns a random number [min;max]
	public static int getRand(int min, int max) {
		return (int) (Math.random() * (++max - min) + min);
	}
	
	// returns the NodeInfo of this node
	public NodeInfo getNodeInfo() {
		return this.node;
	}
	
	
	/*
	 * incoming message handling
	 */
	
	// process incoming message
	public synchronized void processMessage(NodeMessage message) {
		Thread processMessageThread = new Thread() {
			public void run() {
				switch (message.getType()) {
					case NodeMessage.MSG_TYPE_INFO :	processMessageInfo(message);
														break;
					case NodeMessage.MSG_TYPE_ECHO :	processMessageEcho(message);
														break;
					case NodeMessage.MSG_TYPE_RUMOUR :	processMessageRumour(message);
														break;
					case NodeMessage.MSG_TYPE_ID :		processMessageId(message);
														break;
					case NodeMessage.MSG_TYPE_QUIT :	processMessageQuit(message);
														break;
					default :							break;
				}
			}
		};
		processMessageThread.start();
	}

	public synchronized void processMessageInfo(NodeMessage message) {
		Thread thread = new Thread() {
			public void run() {
				log("received message from " + message.getSender().getId() + ": " + message.getMessage());
				waitForIt(2);
				sendMessageId();
			}
		};
		thread.start();
	}

	public synchronized void processMessageEcho(NodeMessage message) {
		// todo
	}

	public synchronized void processMessageRumour(NodeMessage message) {
		receivedRumour(message);
		Rumour rumour;
		// check if rumour already known
		int index = isKnown(message);
		if (index >= 0) {
			// increase rumour
			rumour = this.rumours.get(index);
			if (rumour.isTrusted()) {
				// already trusted, do nothing
				return;
			} else {
				// increase rumour credibility
				rumour.heard();
				if (rumour.isTrusted()) {
					// log that this rumour is trusted now
					log("i believe that " + rumour.getTopic());
				} else {
					// forward rumour
					forwardRumour(message);
				}
			}
		} else {
			// new rumour
			rumour = new Rumour(message.getMessage());
			rumours.add(rumour);
			// forward rumour
			forwardRumour(message);
		}
	}

	public synchronized void processMessageId(NodeMessage message) {
		log("received message from " + message.getSender().getId() + ": " + message.getMessage());
	}

	public synchronized void processMessageQuit(NodeMessage message) {
		log("received shutdown message");
		sendMessageQuit();
		waitForIt(2);
		quit();
	}

	public synchronized void forwardMessage(NodeMessage message) {
		// todo
	}

	
	/*
	 * outgoing message handling
	 */
	
	// send info message with text
	private synchronized void sendMessageInfo() {
		out.println(" ");
		out.println("# message #");
		String msg = null;
		
		try {
			msg = in.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (msg != null && !msg.isEmpty()) {
			NodeMessage message = new NodeMessage(NodeMessage.MSG_TYPE_INFO, msg);
			sendMessage(message);
		}
	}

	// send rumour message with text
	private synchronized void sendMessageRumour() {
		out.println(" ");
		out.println("# rumour #");
		String msg = null;
		
		try {
			msg = in.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (msg != null && !msg.isEmpty()) {
			NodeMessage message = new NodeMessage(NodeMessage.MSG_TYPE_RUMOUR, msg);
			message.setSender(this.node);
			sendMessage(message);
		}
	}
	
	// forward rumour message
	private synchronized void forwardRumour(NodeMessage message) {
		NodeInfo sender = message.getSender();
		// new sender
		message.setSender(this.node);
		// send rumour to all neighbours except sender
		for (NodeConnection connection : this.connections) {
			if (connection.getPartner().getId() != sender.getId()) {
				synchronized (this) {
					connection.send(message);
				}
			}
		}
	}
	
	// send quit message to terminate all nodes
	private synchronized void sendMessageQuit() {
		NodeMessage message = new NodeMessage(NodeMessage.MSG_TYPE_QUIT);
		sendMessage(message);
	}

	// send id to all neighbours
	private synchronized void sendMessageId() {
		String msg = "ID=" + String.valueOf(this.node.getId());
		NodeMessage message = new NodeMessage(NodeMessage.MSG_TYPE_ID, msg);
		sendMessage(message);
		log(message.getMessage());
	}

	// send message to all neighbours
	private synchronized void sendMessage(NodeMessage message) {
		for (NodeConnection connection : this.connections) {
			synchronized (this) {
				connection.send(message);
			}
		}
	}


	/*
	 * miscellaneous
	 */

	// wait for it
	private void waitForIt(int sec) {
		try {
			Thread.sleep(1000 * sec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// log received rumour
	private void receivedRumour(NodeMessage message) {
		log("received rumour from " + message.getSender().getId() + " : " + message.getMessage());
	}
	
	// return pos of rumour (-1 if unknown)
	private int isKnown(NodeMessage message) {
		if (this.rumours.size() < 1) return -1;
		for (Rumour r : this.rumours) {
			if (r.getTopic().equals(message.getMessage())) {
				return rumours.indexOf(r);
			}
		}
		return -1;
	}
	
	// show menu
	private void showMenu() {
		out.println(" ");
		out.println("########## Node ##########");
		out.println("#  1  Send Message        ");
		out.println("#  2  Send Echo           ");
		out.println("#  3  Start Rumour        ");
		out.println("#  4  Terminate all Nodes ");
		out.println("##########################");
	}
	
	// print message with timestamp
	public static void log(String message) {
		if ((message == null) || (message.isEmpty())) message = "";
		java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
		timestamp.setNanos(0);
		out.println(timestamp + " " + message);
	}
	
	// return string representing this node
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("node(\n").append(this.node);
		sb.append("|file=").append(this.nodefile);
		sb.append("|connections(").append(this.connections);
		sb.append(")");
		return sb.toString();
	}

}
