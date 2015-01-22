import org.apache.commons.cli.*;

import util.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Node {
	
	private int id;
	private String host;
	private int port;
	
	private String file;
	private ArrayList<NodeConnection> connections;
	
	private static final int neighbourCount = 3;

	
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
	
	
	private Node() {}
	
	public Node(int id, String file) {
		if (id < 1) throw new RuntimeException("id invalid (must be > 0)");
		if ((file == null) || (file.isEmpty())) throw new RuntimeException("filename invalid");
		this.id = id;
		this.file = file;
	}
	

	public void init() {
		connections = new ArrayList<NodeConnection>();
		ArrayList<NodeInfo> nodeInfoList = parseNodeInfo(file);
		
		// find yourself in node list
		boolean found = false;
		int index = -1;
		for (NodeInfo current : nodeInfoList) {
			if (current.getId() == this.id) {
				this.host = current.getHost();
				this.port = current.getPort();
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
		log(this.toString());
		// wait for user input
		try{
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// stop this node
	public void quit() {
		// close connections?
		log("node " + this.id + " terminated");
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
		NodeConnection connection = new NodeConnection(node);
		connections.add(connection);
	}
	
	// returns a random number [min;max]
	public static int getRand(int min, int max) {
		return (int) (Math.random() * (++max - min) + min);
	}
	
	// print message with timestamp
	public static void log(String message) {
		if ((message == null) || (message.isEmpty())) message = "";
		java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
		timestamp.setNanos(0);
		System.out.println(timestamp + " " + message);
	}
	
	// return string representing this node
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("node(id=").append(id);
		sb.append("|host=").append(host);
		sb.append("|port=").append(port);
		sb.append("|file=").append(file);
		sb.append("|connections(").append(connections);
		sb.append(")");
		return sb.toString();
	}

}
