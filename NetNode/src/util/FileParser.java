package util;
import java.util.ArrayList;

public class FileParser {
	public static ArrayList<NodeInfo> parse(String filename) {
		ArrayList<NodeInfo> nodeInfoList = new ArrayList<NodeInfo>();
		// parse file
		// add each NodeInfo to ArrayList
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
