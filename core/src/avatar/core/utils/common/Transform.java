/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.common.MD5
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
 *************************************************************/
package avatar.core.utils.common;

import java.util.ArrayList;

import avatar.core.model.common.Edge;
import avatar.core.model.common.Point;
import avatar.core.model.geometry.Path;
import avatar.core.model.geometry.Road;
import avatar.core.model.geometry.Segment;
import avatar.core.utils.common.Constant.OrientType;

public class Transform {
	private City city;
	private double width;
	private OrientType oType;

	public Transform(City city) {
		this.city = city;
		width = city.distance2size(Constant.DEFAULT_ROAD_WIDTH /2);
		oType = Constant.DEFAULT_ORIENT_TYPE;
	}
	
	public Transform(City city, OrientType oType, double width) {
		this.width = city.distance2size(width/2);
		this.oType = oType;
	}
	public void setWidth(double width) {
		this.width = city.distance2size(width/2);
	}

	public double getWidth() {
		return city.distance2size(this.width) * 2;
	}

	public Segment segment(Segment segment) {
		Segment newSegment = null;
		Point pointA = segment.getaPoint();
		Point pointB = segment.getbPoint();

		double deltaX = pointB.x - pointA.x;
		double deltaY = pointB.y - pointA.y;
		double deltaA2B = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		int orientFactor = 0;
		if (OrientType.Right.equals(this.oType)) {
			orientFactor = 1;
		} else if (OrientType.Left.equals(this.oType)) {
			orientFactor = -1;
		}

		if (deltaA2B >= Constant.MIN_LNG_DELTA) {
			double newPointDeltaX = orientFactor * this.width * deltaY / deltaA2B;
			double newPointDeltaY = -orientFactor * this.width * deltaX / deltaA2B;
			Point newPointA = new Point(pointA.x + newPointDeltaX, pointA.y + newPointDeltaY);
			Point newPointB = new Point(pointB.x + newPointDeltaX, pointB.y + newPointDeltaY);

			newSegment = new Segment(newPointA, newPointB);
		}
		return newSegment;
	}

	private Point point(Point point, Point prevPoint, Point nextPoint) {
		Point newPoint = null;
		double prevSegAngle = Measure.angleInRadian(prevPoint, point);
		double nextSegAngle = Measure.angleInRadian(point, nextPoint);

		int orientFactor = 0;
		if (OrientType.Right.equals(this.oType)) {
			orientFactor = 1;
		} else if (OrientType.Left.equals(this.oType)) {
			orientFactor = -1;
		}
		
		// alpha is the angle of the vector of (point->newPoint);
		// beta is the angle between the two vectors (prevPoint->point and point->newPoint)
		double alpha = (nextSegAngle+prevSegAngle)/2 - orientFactor*Math.PI / 2;
		double beta = (nextSegAngle - prevSegAngle)/2;
		
		double deltaX = 0;
		double deltaY = 0;
		if (Math.abs(Math.cos(beta)) <= Constant.MIN_LNG_DELTA) {
			// Bug exists here.
			// We need to add a Point 
//			newPoints[0] = this.segment(new Segment(prevPoint, point)).getbPoint();
//			newPoints[1] = this.segment(new Segment(point, nextPoint)).getaPoint();
		} else {
			deltaX = Math.cos(alpha) * this.width / Math.cos(beta);
			deltaY = Math.sin(alpha) * this.width / Math.cos(beta);
		}
		newPoint = new Point(point.x+deltaX, point.y+deltaY);
		return newPoint;
	}

	public Point[] points(Point[] points) {
		int pointCnt = points.length;
		if (pointCnt <= 1) {
			return null;
		}
		
		Point[] newPoints = new Point[pointCnt];
		newPoints[0] = this.segment(new Segment(points[0], points[1])).getaPoint();
		newPoints[pointCnt - 1] = this.segment(new Segment(points[pointCnt - 2], points[pointCnt - 1])).getbPoint();
		for (int i = 1; i < pointCnt - 1; i++) {
			newPoints[i] = this.point(points[i], points[i - 1], points[i + 1]);
		}
		return newPoints;
	}

	public Road road(Road road) {
		Point[] points = this.points(road.getPoints());
		Road newRoad = Road.composeFromPoints(points);
		// actually, the angle, length, box would be changed.
		newRoad.setWidth(road.getWidth());
		newRoad.setAngle(road.getAngle());
		newRoad.setBox(road.getBox());
		newRoad.setId(road.getId());
		newRoad.setHead(road.getHead());
		newRoad.setTail(road.getTail());
		newRoad.setLength(road.getLength());
		return newRoad;
	}

	public Path path(Path path) {
		ArrayList<Edge> edges = path.getEdges();
		Road tempRoad = this.road((Road)edges.get(0));
		Point startPoint = tempRoad.getPoints()[0];
		
		ArrayList<Edge> newRoads = new ArrayList<Edge>();
		for (int i=1; i<edges.size(); i++) {
			Road prevRoad = (Road) edges.get(i-1);
			Road thisRoad = (Road) edges.get(i);
			Point[] prevPoints = prevRoad.getPoints();
			Point[] thisPoints = thisRoad.getPoints();
			Point prevPoint = prevPoints[prevPoints.length-2];
			Point thisPoint = thisPoints[0];
			Point nextPoint = thisPoints[1];
			
			Point point = this.point(thisPoint, prevPoint, nextPoint);
			
			Road newRoad = this.road((Road)edges.get(i-1));
			Point[] newRoadPoints = newRoad.getPoints();
			newRoadPoints[0] = new Point(startPoint);
			newRoadPoints[newRoadPoints.length-1] = new Point(point);
			newRoads.add(newRoad);
			startPoint = point;
		}
		Road lastRoad = (Road)edges.get(edges.size()-1);
		lastRoad = this.road(lastRoad);
		lastRoad.getPoints()[0] = new Point(startPoint);
		newRoads.add(lastRoad);

		path.setEdges(newRoads);
		
		return path;
	}
	 
	public static void main(String[] args) {
//		String regionFile = "etc/resources/shanghai.region";
//		City city = City.get("shanghai");
//		Reg map = new Reg(city);
//		map.load(regionFile);
//		Transform transform = new Transform(city);
//
//		for (Edge edge : map.getRoads().values()) {
//			Road road = (Road) edge;
//			Road newRoad = transform.road(road);
//			road.getPoints();
//			newRoad.getPoints();
//		}
	}
}

