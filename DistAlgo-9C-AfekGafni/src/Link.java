import java.io.Serializable;


public class Link implements Serializable, Comparable<Link>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3444926704065654748L;
	public int pointMe;
	public int pointTarget;
	
	public Link(int me, int target) {
		this.pointMe = me;
		this.pointTarget = target;
	}

	@Override
	public int compareTo(Link o) {
		if(this.pointMe == o.pointMe && this.pointTarget == o.pointTarget) return 0;
		if(this.pointMe == o.pointTarget && this.pointTarget == o.pointMe) return 0;
		if(this.pointMe == o.pointMe) return pointTarget - o.pointTarget;
		else return this.pointMe - o.pointMe;
	}
	
	public Link switchPerspective() {
		return new Link(pointTarget, pointMe);
	}
	
	@Override
	public String toString() {
		return "<"+pointMe+","+pointTarget+">";
	}
}
