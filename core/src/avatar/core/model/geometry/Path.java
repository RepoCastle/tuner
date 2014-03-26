/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.geometry.Path
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:08
 * Email: huajianmao@gmail.com
 *************************************************************/
package avatar.core.model.geometry;

import java.util.ArrayList;
import java.util.HashSet;

import avatar.core.model.common.Edge;
import avatar.core.model.common.Point;
import avatar.core.utils.common.Measure;

public class Path {
	private double length;
	private ArrayList<Edge> edges;

	public Path() {
		length = -1;
		edges = new ArrayList<Edge>();
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	public double getLength() {
		return length;
	}
	
	public Point getPointAt(double length) {
		int edgeCnt = this.edges.size();
		Point point = new Point();
		
		double startLen = 0;
		double endLen = this.length;

		if (Measure.isTwoDistancesSimilar(length, startLen)) {
			Road road = (Road) this.edges.get(0);
			Point roadPoint = road.getPoints()[0];
			point.x = roadPoint.x;
			point.y = roadPoint.y;
		} else if (Measure.isTwoDistancesSimilar(length, endLen)) {
			Road road = (Road) this.edges.get(this.edges.size()-1);
			Point roadPoint = road.getPoints()[road.getPoints().length-1];
			point.x = roadPoint.x;
			point.y = roadPoint.y;
		} else {
			int edgeIdx = -1;
			double pastLen = 0;
			
			for (int i=0; i<edgeCnt; i++) {
				double thisLen = this.edges.get(i).getLength();
				if (pastLen<=length && length<thisLen+pastLen) {
					edgeIdx = i;
					break;
				} else {
					pastLen += thisLen;
				}
			}
			
			if (edgeIdx == -1) {
				point = null;
			} else {
				double leftLen = length - pastLen;
				Road road = (Road) this.edges.get(edgeIdx);
				Point roadPoint = road.getPointAt(leftLen);
				if (roadPoint == null) {
					System.out.println(road.getId());
				}
				point.x = roadPoint.x;
				point.y = roadPoint.y;
			}
		}
		return point;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public void append(Path path) {
		if (path != null && path.length != -1) {
			this.edges.addAll(path.edges);
			this.length += path.length;
		}
	}

	public void append(Edge edge) {
		if (edge != null) {
			this.edges.add(edge);
			this.length += edge.getLength();
		}
	}

	public void append() {
		this.edges.add(null);
	}
	
	public void format() {
		int edgeSize = edges.size();
		if (edgeSize <= 1)
			return;

		Edge thisEdge = null;
		Edge prevEdge = edges.get(0);

		for (int i = 1; i < edgeSize; i++) {
			thisEdge = edges.get(i);
			if (thisEdge.equals(prevEdge)) {
				edges.remove(i);
				edgeSize--;
				i--;
				// Notice: As in the calculation of path length,
				// the length is precisely added, so it MUST not be deleted from
				// the length of path.
				// length -= thisEdge.getLength();
			} else {
				prevEdge = thisEdge;
			}
		}
	}
	
	public String toString() {
		String pathStr = "";
		int edgeCnt = edges.size();
		for (int i=0; i<edgeCnt; i++) {
			pathStr += edges.get(i).getId(); // + "*" + edges.get(i).getLength();
			if (i != edgeCnt-1) {
				pathStr += ", ";
			}
		}
		return pathStr;
	}
	
	public HashSet<Integer> getUniqRoadIDs() {
		HashSet<Integer> edgeIDs = new HashSet<Integer>();
		for (Edge edge:edges) {
			edgeIDs.add(edge.getId());
		}
		return edgeIDs;
	}
}
