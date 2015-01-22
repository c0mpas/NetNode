import java.io.FileNotFoundException;
import java.io.IOException;
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
	
	private String file;
	private ArrayList<NodeConnection> connections;
	private NodeConnection incoming;
	
	private static final int neighbourCount = 3;
	
	static PrintStream out = System.out;


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
		String file = "";
		
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
		        file = line.getOptionValue("file");
		    } else {
		    	throw new RuntimeException("input file");
		    }
		    // create node
		    new Node(id, file).init();
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
	
	private Node() {}
	
	public Node(int id, String file) {
		if (id < 1) throw new RuntimeException("id invalid (must be > 0)");
		if ((file == null) || (file.isEmpty())) throw new RuntimeException("filename invalid");
		this.node = new NodeInfo(id);
		this.file = file;
	}
	

	/*
	 * control functions
	 */
	
	public void init() {
		connections = new ArrayList<NodeConnection>();
		ArrayList<NodeInfo> nodeInfoList = parseNodeInfo(file);
		
		// find yourself in node list
		boolean found = false;
		int index = -1;
		for (NodeInfo current : nodeInfoList) {
			if (current.getId() == this.node.getId()) {
				this.node = current;
				found = true;
				index = nodeInfoList.indexOf(current);
				break;
			}
		}
		if (!found) throw new RuntimeException("node is not part of the node network");
		
		// determine possible neighbours (remove yourself from list)
		nodeInfoList.remove(index);
		
		// choose neighbours and connect to them
		for (int i = 1; i <= neighbourCount; i++) {
			int pos = getRand(0,nodeInfoList.size()-1);
			NodeInfo current = nodeInfoList.get(pos);
			// establish connection to this neighbour
			connectTo(current);
			// remove this neighbour from list
			nodeInfoList.remove(pos);
		}
		
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
	}
	
	private void run() {
		boolean menu = true;
		String input = null;
		Scanner in = new Scanner(System.in);
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
				case 2 :	break;
				case 3 :	break;
				case 4 :	break;
				default :	break;
			}
			// todo
		}
	}
	
	// stop this node
	public void quit() {
		// close connections?
		log("node " + this.node.getId() + " terminated");
		System.exit(0);
	}
	
	// parse info from file to arraylist
	private ArrayList<NodeInfo> parseNodeInfo(String filename) {
		ArrayList<NodeInfo> result = new ArrayList<NodeInfo>();
		try {
			result = FileParser.parse(filename);
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
		connections.add(connection);
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
	public void processMessage(NodeMessage message) {
		log(message.toString());
		switch (message.getType()) {
			case NodeMessage.MSG_TYPE_INFO :	processMessageInfo(message);
												break;
			case NodeMessage.MSG_TYPE_ECHO :	processMessageEcho(message);
												break;
			case NodeMessage.MSG_TYPE_RUMOUR :	processMessageRumour(message);
												break;
			case NodeMessage.MSG_TYPE_QUIT :	processMessageQuit(message);
												break;
			default :							break;
		}
	}

	public void processMessageInfo(NodeMessage message) {
		log(message.toString());
	}

	public void processMessageEcho(NodeMessage message) {
		// todo
	}

	public void processMessageRumour(NodeMessage message) {
		// todo
	}

	public void processMessageQuit(NodeMessage message) {
		// todo
	}

	public void forwardMessage(NodeMessage message) {
		// todo
	}

	
	/*
	 * outgoing message handling
	 */
	
	private void sendMessageInfo() {
		// todo
	}
	

	/*
	 * miscellaneous
	 */

	// show menu
	private void showMenu() {
		out.println("########## Node ##########");
		out.println("# 1  Send Message ");
		out.println("# 2  Send Echo ");
		out.println("# 3  Start Rumour ");
		out.println("# 4  Quit (all) ");
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
		sb.append("node(\n").append(node);
		sb.append("|file=").append(file);
		sb.append("|connections(").append(connections);
		sb.append(")");
		return sb.toString();
	}

}
