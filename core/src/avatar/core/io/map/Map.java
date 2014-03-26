/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.io.map.Map
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.io.map;

import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Iterator;

import avatar.core.model.common.Edge;
import avatar.core.model.common.Point;
import avatar.core.model.common.Vertex;
import avatar.core.model.geometry.Box;
import avatar.core.model.geometry.Cross;
import avatar.core.model.geometry.Road;
import avatar.core.utils.common.City;

public class Map {
	protected City city;
	protected Box box;
	protected HashMap<Integer, Edge> edges;
	protected HashMap<Integer, Vertex> vertices;

	public Map(City city) {
		this.city = city;
	}
	public void load(String mapfile) {
	}

	public void save(String filename) {
		try {
			RandomAccessFile file = new RandomAccessFile(filename, "rw");
			file.writeDouble(this.box.getXmin());
			file.writeDouble(this.box.getYmin());
			file.writeDouble(this.box.getXmax());
			file.writeDouble(this.box.getYmax());
			
			long nItems = this.vertices.size();
			file.writeLong(nItems);
			Iterator<Integer> it = this.vertices.keySet().iterator();
			while (it.hasNext()) {
				Integer key = it.next();
				Cross cross = (Cross) this.vertices.get(key);
				Point point = cross.getPoint();
				file.writeInt(cross.getId());
				file.writeDouble(point.getX());
				file.writeDouble(point.getY());
			}
			
			nItems = this.edges.size();
			file.writeLong(nItems);
			it = this.edges.keySet().iterator();
			while (it.hasNext()) {
				Integer key = it.next();
				Road road = (Road) this.edges.get(key);
				file.writeInt(road.getId());
				file.writeInt(road.getHead().getId());
				file.writeInt(road.getTail().getId());
				file.writeDouble(road.getWidth());
				file.writeDouble(road.getLength());
				file.writeDouble(road.getBox().getXmin());
				file.writeDouble(road.getBox().getYmin());
				file.writeDouble(road.getBox().getXmax());
				file.writeDouble(road.getBox().getYmax());
				
				Point[] points = road.getPoints();
				int pointCount = points.length;
				file.writeInt(pointCount);
				for (int j=0; j<pointCount; j++) {
					Point point = points[j];
					file.writeDouble(point.getX());
					file.writeDouble(point.getY());
				}
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCity(City city) {
		this.city = city;
	}
	public City getCity() {
		return city;
	}
	public Box getBox() {
		return box;
	}
	public void setBox(Box box) {
		this.box = box;
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
}
