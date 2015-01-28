
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class FileParser {
	
	/**
	 * parse a node information file and create an array list of node information objects
	 * 
	 * @param file	input file
	 * @return		an array list of node information objects
	 * @throws FileNotFoundException
	 */
	public static ArrayList<NodeInfo> parseInfo(String file) throws FileNotFoundException {
		ArrayList<NodeInfo> nodeInfoList = new ArrayList<NodeInfo>();
		// parse file
		File input = new File(file);
	    Scanner scanner = new Scanner(new FileReader(input));
	    NodeInfo current;
	    int id;
	    String host;
	    int port;
	    String line;
	    String[] parts;
	    
	    try {
	        while (scanner.hasNextLine()) {
	            line = scanner.nextLine();
	            // parse line and create NodeInfo
	            parts = line.split(" ");
	            id = Integer.parseInt(parts[0]);
	            parts = parts[1].split(":");
	            host = parts[0];
	            port = Integer.parseInt(parts[1]);
	            current = new NodeInfo(id, host, port);
	            // add NodeInfo to ArrayList
	            nodeInfoList.add(current);
	        }
	    } finally {
	        scanner.close();
	    }
		return nodeInfoList;
	}

	/**
	 * parse a graph file and create an array list of node pairs representing the graph
	 * 
	 * @param file	input file
	 * @return		an array list of node pairs representing the graph
	 * @throws FileNotFoundException
	 */
	public static ArrayList<Pair> parseGraph(String file) throws FileNotFoundException {
		ArrayList<Pair> graph = new ArrayList<Pair>();
		// parse file
		File input = new File(file);
	    Scanner scanner = new Scanner(new FileReader(input));
	    Pair current;
	    int left;
	    int right;
	    String line;
	    String[] parts;
	    
	    try {
	        while (scanner.hasNextLine()) {
	            line = scanner.nextLine();
	            // check for beginning or end
	            if (line.contains("--")) {
	            	// remove semicolon
	            	line = line.replace(";", "");
	            	// parse line and create Pair
	            	parts = line.split(" -- ");
	            	left = Integer.parseInt(parts[0]);
	            	right = Integer.parseInt(parts[1]);
	            	current = new Pair(left, right);
	            	graph.add(current);
	            }
	        }
	    } finally {
	        scanner.close();
	    }
		return graph;
	}

}
