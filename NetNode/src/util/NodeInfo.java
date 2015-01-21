package util;

public class NodeInfo {
	private int id;
	private String ip;
	private int port;
	
	private NodeInfo() {
		this.id = 0;
		this.ip = ("0.0.0.0");
		this.port = 0;
	}
	
	public NodeInfo(int id, String ip, int port) {
		if (id < 1) throw new RuntimeException("ID invalid");
		if ((ip == null) || (ip.isEmpty())) throw new RuntimeException("IP invalid");
		if (port < 1) throw new RuntimeException("Port invalid");
		this.id = id;
		this.ip = ip;
		this.port = port;
	}

	public int getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NodeInfo(ID=").append(id);
		sb.append("|IP=").append(ip);
		sb.append("|Port=").append(port);
		sb.append(")");
		return sb.toString();
	}

}
