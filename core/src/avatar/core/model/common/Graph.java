/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.common.Graph
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.model.common;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
	protected HashMap<Integer, Vertex> vertices;
	protected HashMap<Integer, Edge> edges;

	public HashMap<Integer, Vertex> getVertices() {
		return vertices;
	}

	public HashMap<Integer, Edge> getEdges() {
		return edges;
	}

	public int getVertexNum() {
		int size = 0;
		if (vertices != null) {
			size = vertices.size();
		}
		return size;
	}

	public int getEdgeNum() {
		int size = 0;
		if (edges != null) {
			size = edges.size();
		}
		return size;
	}

/*
	public void addVertex(Point point) {
		int id = this.getVertexNum() + Constant.VER_EDGE_ID_OFFSET;
		Vertex vertex = new Vertex(id, point);
		this.vertices.put(id, vertex);
	}

	public void addEdge(Vertex head, Vertex tail, double length) {
		Edge edge = new Edge();
		edge.setId(this.getEdgeNum() + Constant.VER_EDGE_ID_OFFSET);
		edge.setHead(head);
		edge.setTail(tail);
		edge.setLength(length);
		addEdge(edge);
	}
*/
	public void addVertex(Vertex vertex) {
		if (vertex != null) {
			int id = vertex.getId();
			this.vertices.put(id, vertex);
		}
	}

	public void delVertex(int vertexID) {
		Vertex vertex = this.vertices.remove(vertexID);
		if (vertex != null) {
			ArrayList<Edge> inDegrees = vertex.getInDegrees();
			for (Edge edge: inDegrees) {
				delEdge(edge.getId());
			}
			ArrayList<Edge> outDegrees = vertex.getOutDegrees();
			for (Edge edge: outDegrees) {
				delEdge(edge.getId());
			}
		}
	}

	public void addEdge(Edge edge) {
		if (edge != null) {
			int id = edge.getId();
			this.edges.put(id, edge);
			Vertex head = edge.getHead();
			Vertex tail = edge.getTail();
			head.getOutDegrees().add(edge);
			tail.getInDegrees().add(edge);
		}
	}

	public void delEdge(int edgeID) {
		Edge edge = this.edges.get(edgeID);
		this.edges.remove(edge);
		Vertex head = edge.getHead();
		Vertex tail = edge.getTail();
		head.getOutDegrees().remove(edge);
		tail.getInDegrees().remove(edge);
	}
}
