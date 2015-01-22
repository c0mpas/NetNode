package util;

public class NodeInfo {
	
	private int id;
	private String host;
	private int port;
	
	private NodeInfo() {}
	
	public NodeInfo(int id, String ip, int port) {
		if (id < 1) throw new RuntimeException("id invalid");
		if ((ip == null) || (ip.isEmpty())) throw new RuntimeException("host invalid");
		if (port < 1) throw new RuntimeException("port invalid");
		this.id = id;
		this.host = ip;
		this.port = port;
	}

	public int getId() {
		return id;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NodeInfo(id=").append(id);
		sb.append("|host=").append(host);
		sb.append("|port=").append(port);
		sb.append(")");
		return sb.toString();
	}

}
