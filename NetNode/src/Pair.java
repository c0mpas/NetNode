
public class Pair {
	
	public int left;
	public int right;
	public Pair(int left, int right) {
		this.left = left;
		this.right = right;
	}
	
	public boolean valid() {
		if ((left < 1) || (right < 1) || (this.left == this.right)) return false;
		return true;
	}
	
	public boolean equals(Pair p) {
		if ( ((p.left == this.left) && (p.right == this.right)) || ((p.left == this.right) && (p.right == this.left)) ) return true;
		return false;
	}
	
	public boolean isBiggerThan(Pair p) {
		if (this.left > p.left) {
			return true;
		} else {
			if (this.left == p.left) {
				if (this.right > p.right) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	
	public String toString() {
		return "(" + this.left + ":" + this.right + ")";
	}
	
}
