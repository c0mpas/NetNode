
public class Pair {
	
	public int left;
	public int right;
	
	/**
	 * constructor
	 * 
	 * @param left	id of left node
	 * @param right	id of right node
	 */
	public Pair(int left, int right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * check if a pair is valid
	 * 
	 * @return	true, if pair is valid
	 */
	public boolean valid() {
		if ((left < 1) || (right < 1) || (this.left == this.right)) return false;
		return true;
	}

	/**
	 * check two pairs for equality
	 * 
	 * @param p the pair to check versus this
	 * @return	true, if the two pairs are equal
	 */
	public boolean equals(Pair p) {
		if ( ((p.left == this.left) && (p.right == this.right)) || ((p.left == this.right) && (p.right == this.left)) ) return true;
		return false;
	}

	/**
	 * check if a given pair is bigger (higher node id) than this (for sorting)
	 * 
	 * @param p	the pair to check
	 * @return	true, if the given pair is bigger (higher node id)
	 */
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

	/* 
	 * return string representation of this object
	 */
	public String toString() {
		return "(" + this.left + ":" + this.right + ")";
	}
	
}
