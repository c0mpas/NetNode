import java.io.Serializable;


public class NodeMessage implements Serializable {

	private static final long serialVersionUID = 6960679654671161531L;
	
	public static final int MSG_TYPE_INFO	= 1;
	public static final int MSG_TYPE_ECHO	= 2;
	public static final int MSG_TYPE_RUMOUR	= 3;
	public static final int MSG_TYPE_QUIT	= 9;
	
	private int type;
	private String message;
	private NodeInfo sender;
	private NodeInfo receiver;
	
	
	public NodeMessage(int type) {
		setType(type);
	}
	
	public NodeMessage(int type, String message) {
		setType(type);
		setMessage(message);
	}

	
	private boolean isValid(int t) {
		if (t==1 || t==2 || t==3 || t==9) return true;
		return false;
	}
	
	
	private void setType(int type) {
		if (!isValid(type)) throw new RuntimeException("invalid message type");
		this.type = type;
	}

	public void setMessage(String message) {
		if ((message == null) || (message.isEmpty())) throw new RuntimeException("message invalid");
		this.message = message;
	}

	
	public int getType() {
		return this.type;
	}
	
	public String getTypeText() {
		String text = null;
		switch (this.type) {
			case MSG_TYPE_INFO :	text = "info";
									break;
			case MSG_TYPE_ECHO :	text = "echo";
									break;
			case MSG_TYPE_RUMOUR :	text = "rumour";
									break;
			case MSG_TYPE_QUIT :	text = "quit";
									break;
			default :				text = "unknown";
									break;
		}
		return text;
	}

	public String getMessage() {
		return this.message;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NodeMessage(type=").append(type);
		sb.append("(").append(getTypeText()).append(")");
		sb.append("|message=").append(message);
		sb.append("|sender=").append(sender);
		sb.append("|receiver=").append(receiver);
		sb.append(")");
		return sb.toString();
	}

}
