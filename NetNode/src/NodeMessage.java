import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NodeMessage implements Serializable {

	private static final long serialVersionUID = 6960679654671161531L;
	
	public static final int MSG_TYPE_INFO	= 1;
	public static final int MSG_TYPE_ECHO	= 2;
	public static final int MSG_TYPE_RUMOUR	= 3;
	public static final int MSG_TYPE_ID		= 4;
	public static final int MSG_TYPE_QUIT	= 9;
	
	private int type;
	private String message;
	private NodeInfo sender;
	private NodeInfo receiver;
	private String hash;
	
	
	/**
	 * constructor
	 * 
	 * @param type	message type
	 */
	public NodeMessage(int type) {
		setType(type);
		this.hash = getHash();
	}
	
	/**
	 * constructor
	 * 
	 * @param type		message type
	 * @param message	message text
	 */
	public NodeMessage(int type, String message) {
		setType(type);
		setMessage(message);
		this.hash = getHash();
	}

	
	/**
	 * check if message type is valid
	 * 
	 * @param t	the message type to check
	 * @return	true, if message type is valid
	 */
	private boolean isValid(int t) {
		if (t==1 || t==2 || t==3 || t==4 || t==9) return true;
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

	
	public NodeInfo getSender() {
		return sender;
	}

	public void setSender(NodeInfo sender) {
		this.sender = sender;
	}

	public NodeInfo getReceiver() {
		return receiver;
	}

	public void setReceiver(NodeInfo receiver) {
		this.receiver = receiver;
	}

	
	public int getType() {
		return this.type;
	}
	
	/**
	 * return text representing the message type
	 * 
	 * @return	text representing the message type
	 */
	public String getTypeText() {
		String text = null;
		switch (this.type) {
			case MSG_TYPE_INFO :	text = "info";
									break;
			case MSG_TYPE_ECHO :	text = "echo";
									break;
			case MSG_TYPE_RUMOUR :	text = "rumour";
									break;
			case MSG_TYPE_ID :		text = "rumour";
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
	
	/**
	 * generates a hash for identification of the message
	 * 
	 * @return	a hash
	 */
	public static String getHash() {
		// get current Time for hash base
		String time = String.valueOf(System.currentTimeMillis());
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException ex) {
				e.printStackTrace();
			}
		}
		md.update(time.getBytes());
		byte byteData[] = md.digest();
		//convert the byte to hex format
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
	
	/* 
	 * return string representation of this message
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NodeMessage(type=").append(type);
		sb.append("(").append(getTypeText()).append(")");
		sb.append("|message=").append(message);
		sb.append("|sender=").append(sender);
		sb.append("|receiver=").append(receiver);
		sb.append("|hash=").append(hash);
		sb.append(")");
		return sb.toString();
	}

}
