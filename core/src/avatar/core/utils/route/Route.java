/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.route.Route
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.route;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import avatar.core.model.common.Edge;
import avatar.core.model.common.Graph;
import avatar.core.model.common.Vertex;
import avatar.core.model.geometry.Path;
import avatar.core.utils.common.Constant.RouteType;

public class Route {
	protected Graph graph;
	protected RouteType routeType;

	public Route(Graph graph) {
		this.graph = graph;
		this.routeType = RouteType.values()[0];
	}

	public Route(Graph graph, RouteType routeType) {
		this.graph = graph;
		this.routeType = routeType;
	}

	
	public Path getPath(Vertex start, Vertex end, double maxLength) {
		if (start == null || end == null) {
			return null;
		}
		if (null == this.graph || null == this.graph.getVertices()) {
			return null;
		}
		if (start.equals(end)) {
			Path path = new Path();
			path.setLength(0);
			path.setEdges(new ArrayList<Edge>());
			return path;
		}

		Node thisNode, nextNode, sNode;

		sNode = new Node(start, start, end, this.routeType);
		sNode.pastCost = 0;
		sNode.from = null;

		PriorityQueue<Node> nodeQueue = new PriorityQueue<Node>();
		nodeQueue.add(sNode);
		while (!nodeQueue.isEmpty()) {
			thisNode = nodeQueue.poll();
			thisNode.checked = true;
			if (thisNode.pastCost > maxLength) {
				return null;
			}
			if (end.equals(thisNode.vertex)) {
				end.node = thisNode;
				break;
			}
			ArrayList<Edge> outDegrees = thisNode.vertex.getOutDegrees();
			for (int i = 0; i < outDegrees.size(); i++) {
				Edge edge = outDegrees.get(i);
				Vertex nextVertex = edge.getTail();
				if (nextVertex.node != null) {
					nextNode = nextVertex.node;
				} else {
					nextNode = new Node(nextVertex, start, end, this.routeType);
				}
				if (!start.equals(nextNode.start)
						|| !end.equals(nextNode.end)
						|| nextNode.routeType != this.routeType) {
					nextNode.reset(this.routeType, start, end);
				}
				if (nextNode.checked == true) {
					continue;
				}
				double distanceThroughU = thisNode.pastCost + edge.getLength();
				if (distanceThroughU < nextNode.pastCost) {
					nodeQueue.remove(nextNode);
					nextNode.pastCost = distanceThroughU;
					nextNode.from = edge;
					nodeQueue.add(nextNode);
				}
			}
		}

		return genPath(start, end);
	}
	public Path getPath(Edge startEdge, Edge endEdge, double maxLength) {
		if (startEdge==null || endEdge==null) return null;
		
		Path path = null;
		if (startEdge.equals(endEdge)) {
			path = new Path();
			path.setLength(startEdge.getLength());
			ArrayList<Edge> edges = new ArrayList<Edge>();
			edges.add(startEdge);
			path.setEdges(edges);
		} else {
			path = new Path();
			path.setLength(startEdge.getLength());
			ArrayList<Edge> edges = new ArrayList<Edge>();
			edges.add(startEdge);
			path.setEdges(edges);
			Path path1 = getPath(startEdge.getTail(), endEdge.getHead());
			if (path1 != null) {
				path.append(path1);
				path.append(endEdge);
			} else {
				path = null;
			}
			if (path==null || path.getLength()>maxLength) {
				path = null;
			}
		}
		return path;
	}

	public Path getPath(Vertex start, Vertex end) {
		Path path = getPath(start, end, Double.POSITIVE_INFINITY);
		return path;
	}
	public Path getPath(Edge startEdge, Edge endEdge) {
		Path path = getPath(startEdge, endEdge, Double.POSITIVE_INFINITY);
		return path;
	}
	public Path getPath(Vertex start, Vertex end, List<Vertex> midVertices) {
		Path path = new Path();
		if (start==null || end==null) {
			return null;
		}
		Vertex prevVertex = start;
		
		Path subPath = null;
		for (int i=0; i< midVertices.size(); i++) {
			Vertex thisVertex = midVertices.get(i);
			subPath = getPath(prevVertex, thisVertex, Double.POSITIVE_INFINITY);
			if (subPath == null) {
				return null;
			}
			path.append(subPath);
			prevVertex = thisVertex;
		}
		subPath = getPath(prevVertex, end, Double.POSITIVE_INFINITY);
		if (subPath == null) {
			return null;
		}
		path.append(subPath);
		
		return path;
	}
	public Path getPath(ArrayList<Edge> edges, double maxLength) {
		Path path = new Path();
		if (edges.size() <= 1) {
			return null;
		}
		for (int i=1; i<edges.size(); i++) {
			Edge prevEdge = edges.get(i-1);
			Edge thisEdge = edges.get(i);
			Path thisPath = getPath(prevEdge, thisEdge);
			path.append(thisPath);
		}
		return path;
	}
	
	public Path randomWalk(Vertex start, Vertex end, int mediaPointNum) {
		Path path = new Path();
		Random generator = new Random();
		Vertex startCross = start;
		Vertex endCross = null;

		for (int i=0; i<mediaPointNum; i++) {
			int crossID = generator.nextInt(graph.getVertexNum()-1);
			while ((endCross = graph.getVertices().get(crossID))==null) {
				crossID = generator.nextInt(graph.getVertexNum()-1);
			}
			path.append(this.getPath(startCross, endCross));
			startCross = endCross;
		}
		path.append(this.getPath(startCross, end));
		return path;
	}

	protected Path genPath(Vertex start, Vertex end) {
		Path path = null;
		Node sNode = start.node;
		Node dNode = end.node;
		
		if (dNode!=null && dNode.checked==true && sNode!=null) {
			double length = 0;
			ArrayList<Edge> edges = new ArrayList<Edge>();
			Node thisNode = dNode;
			while (!sNode.equals(thisNode)) {
				thisNode.start = null;
				thisNode.end = null;
				thisNode.checked = false;
				if (thisNode.from == null) {
					break;
				} else {
					edges.add(0, thisNode.from);
					length += thisNode.from.getLength();
					thisNode = thisNode.from.getHead().node;
				}
			}
			if (sNode.equals(thisNode)) {
				path = new Path();
				path.setLength(length);
				path.setEdges(edges);
			}
		}
		return path;
	}
}
