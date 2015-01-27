import java.io.Serializable;

public class Rumour implements Serializable {

	private static final long serialVersionUID = 2574470343724385272L;
	
	private static final int TRUST_THRESHOLD = 3;
	
	private boolean trusted;
	private int heardFrom;
	private String topic;

	public Rumour(String topic) {
		this.heardFrom = 0;
		this.trusted = false;
		if (topic == null) {
			this.topic = "";
		} else {
			this.topic = topic;
		}
	}
	
	public void heard() {
		this.heardFrom++;
		if (this.heardFrom > Rumour.TRUST_THRESHOLD) this.trusted = true;
	}
	
	public boolean isTrusted() {
		return this.trusted;
	}
	
	public String getTopic() {
		return this.topic;
	}
}
