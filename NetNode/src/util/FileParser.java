package util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class FileParser {
	
	public static ArrayList<NodeInfo> parse(String file) throws FileNotFoundException {
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
		
	public static ArrayList<NodeInfo> getDummy() {
		ArrayList<NodeInfo> nodeInfoList = new ArrayList<NodeInfo>();
		nodeInfoList.add(new NodeInfo(1,"isl-s-01",5000));
		nodeInfoList.add(new NodeInfo(2,"isl-s-01",5001));
		nodeInfoList.add(new NodeInfo(3,"127.0.0.1",2712));
		nodeInfoList.add(new NodeInfo(4,"localhost",5003));
		return nodeInfoList;
	}
}
