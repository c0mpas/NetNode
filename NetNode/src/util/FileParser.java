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
		nodeInfoList.add(new NodeInfo(1,"127.0.0.1",7001));
		nodeInfoList.add(new NodeInfo(2,"127.0.0.2",7002));
		nodeInfoList.add(new NodeInfo(3,"127.0.0.3",7003));
		return nodeInfoList;
	}
}
