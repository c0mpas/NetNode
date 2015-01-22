
public class NodeInfo {
	
	private int id;
	private String host;
	private int port;
	
	
	public NodeInfo(int id) {
		setId(id);
	}
	
	public NodeInfo(int id, String host, int port) {
		setId(id);
		setHost(host);
		setPort(port);
	}
	
	
	private void setId(int id) {
		if (id < 1) throw new RuntimeException("id invalid");
		this.id = id;
	}

	public void setHost(String host) {
		if ((host == null) || (host.isEmpty())) throw new RuntimeException("host invalid");
		this.host = host;
	}

	public void setPort(int port) {
		if (port < 1) throw new RuntimeException("port invalid");
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
