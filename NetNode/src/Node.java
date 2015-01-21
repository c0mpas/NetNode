import org.apache.commons.cli.*;
import util.*;
import java.util.*;

public class Node {
	
	private int id;
	private String file;
	private ArrayList connections; // add type info

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
		    	throw new RuntimeException("Missing ID");
		    }
		    // check input file
		    if (line.hasOption("file")) {
		        file = line.getOptionValue("file");
		    } else {
		    	throw new RuntimeException("Input File");
		    }
		    // create node
		    new Node(id, file).run();
		} catch (ParseException e) {
		    log("Parse Error: " + e.getMessage());
		} catch (RuntimeException e) {
		    log("Parameter Error: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Node() {
		this.id = 0;
		this.file = "";
	}
	
	public Node(int id, String file) {
		if (id < 1) throw new RuntimeException("ID invalid (must be > 0)");
		if ((file == null) || (file.isEmpty())) throw new RuntimeException("Filename invalid");
		
		this.id = id;
		this.file = file;
	}
	
	public void run() {
		ArrayList<NodeInfo> nodeInfoList = parseNodeInfo(file);
		log(nodeInfoList.toString());
		
		// do stuff
		
		log("Node " + this.id + "ready");
		
		// do stuff
		
	}

	public void quit() {
		// close connections?
		log("Node " + this.id + "terminated");
		System.exit(0);
	}
	
	private ArrayList<NodeInfo> parseNodeInfo(String filename) {
		ArrayList<NodeInfo> result = new ArrayList<NodeInfo>();
		result = FileParser.parse(filename);
		return result;
	}
	
	private static void log(String message) {
		if ((message == null) || (message.isEmpty())) message = "";
		java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
		timestamp.setNanos(0);
		System.out.println(timestamp + " " + message);
	}

}
