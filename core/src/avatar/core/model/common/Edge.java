/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.common.Edge
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.model.common;

public class Edge {
	private int id;
	private Vertex head;
	private Vertex tail;
	private double length;

	public void setHead(Vertex head) {
		this.head = head;
	}
	public void setTail(Vertex tail) {
		this.tail = tail;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public Vertex getHead() {
		return head;
	}
	public Vertex getTail() {
		return tail;
	}
	public double getLength() {
		return length;
	}
}
