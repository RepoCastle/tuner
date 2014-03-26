/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.common.Vertex
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.model.common;

import java.util.ArrayList;

import avatar.core.utils.common.Measure;
import avatar.core.utils.route.Node;

public class Vertex {
    // FIXME: should node be moved to avatar.core.utils.route?
	public Node node;

	private int id;
	private Point point;
	private ArrayList<Edge> inRoads;
	private ArrayList<Edge> outRoads;

	public Vertex() {
		this.id = -1;
		this.point = new Point();
		this.inRoads = new ArrayList<Edge>();
		this.outRoads = new ArrayList<Edge>();
	}

	public Vertex(Point point) {
		this.id = -1;
		this.point = new Point(point.x, point.y);
		this.inRoads = new ArrayList<Edge>();
		this.outRoads = new ArrayList<Edge>();
	}

	public Vertex(int id, Point point) {
		this.id = id;
		this.point = new Point(point.x, point.y);
		this.inRoads = new ArrayList<Edge>();
		this.outRoads = new ArrayList<Edge>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public ArrayList<Edge> getInDegrees() {
		return inRoads;
	}

	public ArrayList<Edge> getOutDegrees() {
		return outRoads;
	}

	
	public void setInDegrees(ArrayList<Edge> inRoads) {
		this.inRoads = inRoads;
	}

	public void setOutDegrees(ArrayList<Edge> outRoads) {
		this.outRoads = outRoads;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Vertex) {
			Vertex vertex = (Vertex) obj;
			if (vertex.getId() != -1) {
				return vertex.getId() == this.id;
			} else {
				double deltaDist = Measure.distance(vertex.getPoint(), this.getPoint());
				System.out.println(deltaDist);
				if (deltaDist <= 2) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return super.equals(obj);
		}
	}
	
	public String toString() {
		String retStr = "";
		retStr += "Vertex " + id + " (" + point + ")" + "\n";
		retStr += "out: " + outRoads.size() + "\n";
		retStr += " in: " + inRoads.size();
		return retStr;
	}
}
