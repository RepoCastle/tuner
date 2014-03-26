/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.route.Node
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.route;

import avatar.core.model.common.Edge;
import avatar.core.model.common.Vertex;
import avatar.core.utils.common.Measure;
import avatar.core.utils.common.Constant.RouteType;

public class Node implements Comparable<Node> {
	public Vertex vertex;
	
	public Vertex start;
	public Vertex end;
	public boolean checked;
	public double pastCost;
	public double hcost;
	public Edge from;
	public RouteType routeType;
	
	public Node() {
		vertex = null;
		RouteType routeType = RouteType.values()[0];
		reset(routeType, null, null);
	}
	
	public Node(RouteType routeType) {
		vertex = null;
		reset(routeType, null, null);
	}
	
	public Node(Vertex start, Vertex end, RouteType routeType) {
		vertex = null;
		reset(routeType, start, end);
	}
	
	public Node(Vertex vertex, Vertex start, Vertex end, RouteType routeType) {
		this.vertex = vertex;
		if (vertex != null) {
			vertex.node = this;
		}
		reset(routeType, start, end);
	}

	public void reset(RouteType routeType, Vertex start, Vertex end) {
		this.start = start;
		this.end = end;
		this.checked = false;
		this.pastCost = Double.POSITIVE_INFINITY;
		this.hcost = Double.POSITIVE_INFINITY;
		this.routeType = routeType;
	}
	
	private double getHcost() {
		if (Double.POSITIVE_INFINITY == hcost) {
			hcost = this.heuristicDistance();
		}
		return hcost;
	}
	
	private double heuristicDistance() {
		double hcost = Double.POSITIVE_INFINITY;
		if (RouteType.AStar.equals(this.routeType)) {
			if (this.start!=null && this.end!=null) {
				hcost = Measure.distance(this.vertex.getPoint(), this.end.getPoint());
			}
		} else if (RouteType.Dijkstra.equals(this.routeType)) {
			hcost = 0;
		} else {
			hcost = 0;
		}
		
		return hcost;
	}
	
	public int compareTo(Node other)
	{
		return Double.compare(this.pastCost+this.getHcost(), other.pastCost+other.getHcost());
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			Node node = (Node) obj;
			return this.vertex.getId() == node.vertex.getId();
		} else {
			return super.equals(obj);
		}
	}
	
	public String toString() {
		String node = "";
		node += this.vertex.getId() + ",";
		node += this.pastCost + ",";
		node += this.hcost + ",";
		node += this.checked;
		return node;
	}
}