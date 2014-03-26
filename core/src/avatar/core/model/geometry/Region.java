/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.geometry.Region
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.model.geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import avatar.core.io.map.Map;
import avatar.core.io.map.Reg;
import avatar.core.model.common.Edge;
import avatar.core.model.common.Graph;
import avatar.core.model.common.Point;
import avatar.core.model.common.Vertex;
import avatar.core.utils.common.Constant;
import avatar.core.utils.common.Logging;
import avatar.core.utils.common.Measure;

public class Region extends Graph {
	private Map map;
	private Box box;
	private double cellSize, cellLength;
	private int vCellCnt, hCellCnt;
	private Cell[] mesh;

	public Region(Reg map, double cellLength) {
		if (null == map || null == map.getCrosses()
				|| 0 == map.getCrosses().size() || 0 == map.getRoads().size()) {
			return;
		}
		this.map = map;
		this.box = new Box(map.getBox());
		this.vertices = map.getCrosses();
		this.edges = map.getRoads();
		this.cellLength = cellLength;
		this.cellSize = this.map.getCity().distance2size(cellLength);
		this.meshCells();
		this.linkRoads();
		this.linkCrosses();
	}

	public Cell locate(Point point) {
		Cell cell = null;
		int i, j;
		if (this.mesh != null) {
			i = (int) ((point.getX() - this.box.getXmin()) / cellSize);
			j = (int) ((point.getY() - this.box.getYmin()) / cellSize);

			int hCells = (int) ((this.box.getXmax() - this.box.getXmin()) / cellSize) + 1;
			int vCells = (int) ((this.box.getYmax() - this.box.getYmin()) / cellSize) + 1;
			if (i >= 0 && i < hCells && j >= 0 && j < vCells)
				cell = mesh[i * vCells + j];
		}
		return cell;
	}
/*
	public String getPicIDInLevel(Point point, int level) {
		String picID = null;
		double cellSize = Constant.getCellSizeUnderLevel(level);
		int i = (int) ((point.getX() - this.box.getXmin()) / cellSize);
		int j = (int) ((point.getY() - this.box.getYmin()) / cellSize);

		picID = "L"+level+"-X"+i+"-Y"+j+".png";
		
		return picID;
	}

	public HashSet<String> getPicIDInLevel(Box box, int level) {
		HashSet<String> picIDs = new HashSet<String>();
		double cellSize = Constant.getCellSizeUnderLevel(level);
		double minx = box.getXmin() > this.box.getXmin() ?  box.getXmin() : this.box.getXmin();
		double miny = box.getYmin() > this.box.getYmin() ?  box.getYmin() : this.box.getYmin();
		double maxx = box.getXmax() < this.box.getXmax() ?  box.getXmax() : this.box.getXmax();
		double maxy = box.getYmax() < this.box.getYmax() ?  box.getYmax() : this.box.getYmax();
		int minI = (int) ((minx - this.box.getXmin()) / cellSize);
		int minJ = (int) ((miny - this.box.getYmin()) / cellSize);

		int maxI = (int) ((maxx - this.box.getXmin()) / cellSize);
		int maxJ = (int) ((maxy - this.box.getYmin()) / cellSize);
		
		for (int i=minI; i<=maxI; i++) {
			for (int j=minJ; j<=maxJ; j++) {
				String picID = "L"+level+"-X"+i+"-Y"+j+".png";
				picIDs.add(picID);
			}
		}
		return picIDs;
	} */

	public Cell[] getAllCells() {
		return this.mesh;
	}
	public ArrayList<Cell> cellsArround(Point point) {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		int i, j;
		if (this.mesh != null) {
			i = (int) ((point.getX() - this.box.getXmin()) / this.cellSize);
			j = (int) ((point.getY() - this.box.getYmin()) / this.cellSize);
			if (i - 1 >= 0 && i - 1 < this.hCellCnt) {
				if (j - 1 >= 0 && j - 1 < this.vCellCnt) {
					cells.add(mesh[(i - 1) * this.vCellCnt + (j - 1)]);
				}
				if (j >= 0 && j < this.vCellCnt) {
					cells.add(mesh[(i - 1) * this.vCellCnt + j]);
				}
				if (j + 1 >= 0 && j + 1 < this.vCellCnt) {
					cells.add(mesh[(i - 1) * this.vCellCnt + (j + 1)]);
				}
			}
			if (i + 1 >= 0 && i + 1 < this.hCellCnt) {
				if (j - 1 >= 0 && j - 1 < this.vCellCnt) {
					cells.add(mesh[(i + 1) * this.vCellCnt + (j - 1)]);
				}
				if (j >= 0 && j < this.vCellCnt) {
					cells.add(mesh[(i + 1) * this.vCellCnt + j]);
				}
				if (j + 1 >= 0 && j + 1 < this.vCellCnt) {
					cells.add(mesh[(i + 1) * this.vCellCnt + (j + 1)]);
				}
			}
			if (i >= 0 && i < this.hCellCnt) {
				if (j - 1 >= 0 && j - 1 < this.vCellCnt) {
					cells.add(mesh[i * this.vCellCnt + (j - 1)]);
				}
				if (j + 1 >= 0 && j + 1 < this.vCellCnt) {
					cells.add(mesh[i * this.vCellCnt + (j + 1)]);
				}
				if (j >= 0 && j < this.vCellCnt) {
					cells.add(mesh[i * this.vCellCnt + j]);
				}
			}
		}
		return cells;
	}

	public ArrayList<Cell> cellsArroundInRange(Point point, double radix) {
		ArrayList<Cell> cells = null;
		double minx = Math.max(point.x - radix, this.box.getXmin());
		double miny = Math.max(point.y - radix, this.box.getYmin());
		double maxx = Math.min(point.x + radix, this.box.getXmax());
		double maxy = Math.min(point.y + radix, this.box.getYmax());
		cells = cellsArround(new Box(minx, miny, maxx, maxy));
		return cells;
	}

	public ArrayList<Cell> cellsArround(Box box) {
		ArrayList<Cell> cells = new ArrayList<Cell>();

		int xminIndex = (int) ((box.getXmin() - this.box.getXmin()) / this.cellSize);
		int yminIndex = (int) ((box.getYmin() - this.box.getXmin()) / this.cellSize);
		int xmaxIndex = (int) ((box.getXmax() - this.box.getYmin()) / this.cellSize);
		int ymaxIndex = (int) ((box.getYmax() - this.box.getYmin()) / this.cellSize);

		for (int x = xminIndex; x <= xmaxIndex; x++) {
			for (int y = yminIndex; y <= ymaxIndex; y++) {
				cells.add(mesh[x * this.vCellCnt + y]);
			}
		}
		return cells;
	}

	public Road nearestRoad(Point point) {
		return nearestRoad(point, Double.MAX_VALUE);
	}

	public Road nearestRoad(Point point, double maxDist) {
		Road nearest = null;
		Cell cell = this.locate(point);
		HashMap<Integer, Edge> roads = cell.getRoads();
		double mindist = Double.POSITIVE_INFINITY;
		for (Integer key : roads.keySet()) {
			Road road = (Road) roads.get(key);
			try {
				double dist = Measure.distance(point, road);
				if (dist < mindist) {
					nearest = road;
					mindist = dist;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (mindist > maxDist) {
			nearest = null;
		}
		return nearest;
	}

	public Road nearestRoadToDirection(Point point, double maxDist, double directionAngle) {
		Road nearest = null;
		Cell cell = this.locate(point);
		HashMap<Integer, Edge> roads = cell.getRoads();
		double mindist = Double.POSITIVE_INFINITY;
		for (Integer key : roads.keySet()) {
			Road road = (Road) roads.get(key);
			try {
				double dist = Measure.distance(point, road);
				int roadAngle = Measure.angle(road);
				double deltaAngleCos =  Math.cos(Math.abs(roadAngle-directionAngle)*Math.PI/180);
				if (dist < mindist && deltaAngleCos>=0) {
					nearest = road;
					mindist = dist;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (mindist > maxDist) {
			nearest = null;
		}
		return nearest;
	}

	public HashSet<Road> getNearbyRoadsInRange(Point point, double radius) {
		HashSet<Road> roads = new HashSet<Road> ();
		HashSet<Road> nearbyRoads = null;
		
		if (radius < Constant.CELL_UNIT_LENGTH) {
			nearbyRoads = getNearbyRoads(point);
		} else {
			ArrayList<Cell> cells = cellsArroundInRange(point, radius);
			nearbyRoads = new HashSet<Road>();
			for (Cell cell: cells) {
				for (Edge edge: cell.getRoads().values()) {
					Road road = (Road) edge;
					nearbyRoads.add(road);
				}
			}
		}
		for (Road road: nearbyRoads) {
			try {
				double dist = Measure.distance(point, road);
				if (dist < radius) {
					roads.add(road);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return roads;
	}

	public Cross nearestCross(Point point) {
		Cross nearest = null;
		Cell cell = this.locate(point);
		if (cell != null) {
			HashMap<Integer, Vertex> crosses = cell.getCrosses();
			double mindist = Double.POSITIVE_INFINITY;
			for (Integer key : crosses.keySet()) {
				Cross cross = (Cross) crosses.get(key);
				try {
					double dist = Measure.distance(point, cross.getPoint());
					if (dist < mindist) {
						nearest = cross;
						mindist = dist;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// if the point is too far away from the cross, shall we return it?
			// if (mindist >= THRESHOLD) {
			//     return null;
			// }
		}

		return nearest;
	}

	public HashSet<Road> getNearbyRoads(Point point) {
		HashSet<Road> nearbyRoads = new HashSet<Road>();
		List<Cell> surCells = this.cellsArround(point);
		for (Cell cell : surCells) {
			Iterator<Edge> roads = cell.getRoads().values().iterator();
			while (roads.hasNext()) {
				Road road = (Road) roads.next();
				if (nearbyRoads.contains(road)) {
					continue;
				} else {
					nearbyRoads.add(road);
				}
			}
		}
		return nearbyRoads;
	}

	public int addCross(Point point) {
		int id = this.getVertexNum() + Constant.VER_EDGE_ID_OFFSET;
		Vertex cross = new Cross(id, point);
		super.addVertex(cross);

		return id;
	}
	
	public int addCross(Point point, int roadID) {
		int crossID = this.addCross(point);
		Cross newCross = (Cross) this.getCrosses().get(crossID);
		Road road = (Road) this.getRoads().get(roadID);
		this.delRoad(road.getId());

		Point[] midPoints;
		try {
			// seperate the road into two roads;
			
			// FIXME: set the midPoints for the head part of the road
			midPoints = null;
			this.addRoad((Cross) road.getHead(), newCross, midPoints);
			// FIXME: set the midPoints for the tail part of the road
			midPoints = null;
			this.addRoad(newCross, (Cross) road.getTail(), midPoints);
		} catch (Exception e) {
			Logging.warn("FIXME: Can't find the road");
		}
		// change the road related informations
		Logging.warn("Region::addCross(Point, int) not implemented!");
		
		return crossID;
	}

	public Cross delCross(int crossID) {
		Cross cross = (Cross) this.vertices.get(crossID);
		Cell cell = locate(cross.getPoint());
		cell.getCrosses().remove(cross).getId();
		ArrayList<Edge> inRoads = cross.getInDegrees();
		ArrayList<Edge> outRoads = cross.getOutDegrees();

		for (Edge road : inRoads) {
			this.delRoadFromCells((Road) road);
		}
		for (Edge road : outRoads) {
			this.delRoadFromCells((Road) road);
		}
		super.delVertex(cross.getId());
		return cross;
	}

	public int addRoad(Cross head, Cross tail, Point[] midPoints) {
		Road road = new Road();
		double width = Constant.MAX_WIDTH;
		Point[] points = null;
		if (midPoints == null) {
			points = new Point[2];
			points[0] = head.getPoint();
			points[1] = tail.getPoint();
		} else {
			int pointCnt = midPoints.length + 2;
			points = new Point[pointCnt];
			points[0] = head.getPoint();
			for (int i = 0; i < midPoints.length; i++) {
				points[i + 1] = new Point(midPoints[i]);
			}
			points[pointCnt - 1] = tail.getPoint();
		}

		road.setId(this.getEdgeNum() + Constant.VER_EDGE_ID_OFFSET);
		road.setHead(head);
		road.setTail(tail);
		road.setWidth(width);
		road.setPoints(points);

		super.addEdge(road);

		return road.getId();
	}

	public Road delRoad(int roadID) {
		Road road = (Road) this.edges.get(roadID);
		this.delRoadFromCells(road);
		super.delEdge(road.getId());
		return road;
	}

	private void delRoadFromCells(Road road) {
		Box box = road.getBox();
		ArrayList<Cell> cells = cellsArround(box);
		for (Cell cell : cells) {
			cell.getRoads().remove(road.getId());
		}
	}

	private void meshCells() {
		int i, j;
		Cell aCell;

		if (this.cellSize != 0) {
			this.vCellCnt = (int) ((this.box.getYmax() - this.box.getYmin()) / this.cellSize) + 1;
			this.hCellCnt = (int) ((this.box.getXmax() - this.box.getXmin()) / this.cellSize) + 1;
			this.mesh = new Cell[this.vCellCnt * this.hCellCnt];
			for (i = 0; i < this.hCellCnt; i++)
				for (j = 0; j < this.vCellCnt; j++) {
					this.mesh[i * this.vCellCnt + j] = new Cell();
					aCell = this.mesh[i * this.vCellCnt + j];
					aCell.setxNumber(i);
					aCell.setyNumber(j);
					
					double xmin = this.box.getXmin() + i * this.cellSize;
					double ymin = this.box.getYmin() + j * this.cellSize;
					double xmax = this.box.getXmin() + (i + 1) * this.cellSize;
					double ymax = this.box.getYmin() + (j + 1) * this.cellSize;
					aCell.setBox(new Box(xmin, ymin, xmax, ymax));
				}
		} else {
			this.vCellCnt = 0;
			this.hCellCnt = 0;
			this.mesh = null;
		}
	}
	
	public double length2degree(double length) {
		double degree = this.cellSize * length / this.cellLength;
		return degree;
	}
	
	public double degree2length(double degree) {
		double length = degree * this.cellLength / this.cellSize;
		return length;
	}

	private void linkRoads() {
		int m1, m2, n1, n2, i, j;
		Cell aCell;

		if (this.mesh != null) {
			Iterator<Edge> it = this.getEdges().values().iterator();
			while (it.hasNext()) {
				Road road = (Road) it.next();
				m1 = (int) ((road.getBox().getXmin() - this.box.getXmin()) / this.cellSize);
				m2 = (int) ((road.getBox().getXmax() - this.box.getXmin()) / this.cellSize);
				n1 = (int) ((road.getBox().getYmin() - this.box.getYmin()) / this.cellSize);
				n2 = (int) ((road.getBox().getYmax() - this.box.getYmin()) / this.cellSize);

				for (i = m1; i <= m2; i++) {
					for (j = n1; j <= n2; j++) {
						aCell = this.mesh[i * this.vCellCnt + j];
						aCell.getRoads().put(road.getId(), road);
					}
				}
			}
		}
	}

	private void linkCrosses() {
		Cell aCell;
		if (this.mesh != null) {
			Iterator<Vertex> it = this.getVertices().values().iterator();
			while (it.hasNext()) {
				Cross cross = (Cross) it.next();
				aCell = this.locate(cross.getPoint());
				aCell.getCrosses().put(cross.getId(), cross);
			}
		}
	}
	
	public void saveToRegionFormat(String filename) {
		Map map = new Map(this.map.getCity());
		map.setBox(this.box);
		map.setRoads(this.edges);
		map.setCrosses(this.vertices);
		map.save(filename);
	}
	
	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Box getBox() {
		return box;
	}

	public double getCellSize() {
		return cellSize;
	}

	public int getvCellCnt() {
		return vCellCnt;
	}

	public int gethCellCnt() {
		return hCellCnt;
	}

	public Cell[] getMesh() {
		return mesh;
	}

	public HashMap<Integer, Edge> getRoads() {
		return edges;
	}

	public void setRoads(HashMap<Integer, Edge> roads) {
		this.edges = roads;
	}

	public HashMap<Integer, Vertex> getCrosses() {
		return vertices;
	}

	public void setCrosses(HashMap<Integer, Vertex> crosses) {
		this.vertices = crosses;
	}

	public static void main(String args[]) {

	}
}
