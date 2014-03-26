/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.io.map.Reg
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:06
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.io.map;

import java.io.RandomAccessFile;
import java.util.HashMap;

import avatar.core.model.common.Edge;
import avatar.core.model.common.Point;
import avatar.core.model.common.Vertex;
import avatar.core.model.geometry.Box;
import avatar.core.model.geometry.Cross;
import avatar.core.model.geometry.Road;
import avatar.core.utils.common.City;

public class Reg extends Map {
	public Reg(City city) {
		super(city);
	}
	public static Reg fromRawMap(Map map) {
		Reg reg = new Reg(map.city);
		reg.box = map.box;
		reg.vertices = map.getCrosses();
		reg.edges = map.getRoads();
		
		for (Edge edge : reg.edges.values()) {
			Cross head = (Cross) edge.getHead();
			head.getOutDegrees().add(edge);
			Cross tail = (Cross) edge.getTail();
			tail.getInDegrees().add(edge);
		}
		return reg;
	}
	public void load() {
		try {
            String mapfile = this.city.getMapfile();
			RandomAccessFile file = new RandomAccessFile(mapfile, "r");
			Box box = new Box(file.readDouble(),
							  file.readDouble(),
							  file.readDouble(),
							  file.readDouble());
			this.setBox(box);
			
			HashMap<Integer, Vertex> crosses = new HashMap<Integer, Vertex>();
			long nItems = file.readLong();
			for (int i=0; i<nItems; i++) {
				Vertex cross = new Cross();
				cross.setId(file.readInt());
				cross.setPoint(new Point(file.readDouble(),
										 file.readDouble()));
				crosses.put(cross.getId(), cross);
			}
			this.setCrosses(crosses);
			
			HashMap<Integer, Edge> roads = new HashMap<Integer, Edge>();
			nItems = file.readLong();
			for (int i=0; i<nItems; i++) {
				Road road = new Road();
				road.setId(file.readInt());
				
				int headID = file.readInt();
				int tailID = file.readInt();
				Cross head = (Cross) this.getCrosses().get(headID);
				head.getOutDegrees().add(road);
				Cross tail = (Cross) this.getCrosses().get(tailID);
				tail.getInDegrees().add(road);
				road.setHead(head);
				road.setTail(tail);
				
				road.setWidth(file.readDouble());
				road.setLength(file.readDouble());
				
				box = new Box(file.readDouble(),
						  	  file.readDouble(),
						  	  file.readDouble(),
						  	  file.readDouble());
				road.setBox(box);

				int pointCount = file.readInt();
				Point[] points = new Point[pointCount];
				for (int j=0; j<pointCount; j++) {
					Point point = new Point();
					point.setX(file.readDouble());
					point.setY(file.readDouble());
					points[j] = point;
				}
				road.setPoints(points);
				roads.put(road.getId(), road);
			}
			this.setRoads(roads);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
