/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.common.Measure
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.common;

import avatar.core.model.common.Point;
import avatar.core.model.geometry.Road;
import avatar.core.model.geometry.Segment;

public class Measure {
	private static City city = Constant.defaultCity;
	private static Transform roadDistTransform = new Transform(city, Constant.DEFAULT_ORIENT_TYPE, 0.1);
	
	public static double distance(Point aPoint, Point bPoint) {
		if (aPoint==null || bPoint==null) return -1;
		double x, y;
		double lng1, lat1, lng2, lat2;
		lng1 = aPoint.getX();
		lat1 = aPoint.getY();
		lng2 = bPoint.getX();
		lat2 = bPoint.getY();
		x = (lng2 - lng1) / 180 * Math.PI * Math.cos(((lat1 + lat2) / 2) / 180 * Math.PI) * city.getEarthRadius();
		y = (lat2 - lat1) / 180 * Math.PI * city.getEarthRadius();
		return Math.sqrt(x * x + y * y);
	}
	public static double distance(Point[] points) {
		double dist = -1;
		Point point1, point2;
		if (points!=null && points.length>0) {
			dist = 0;
			int i=0;
			point1 = points[i];
			while (point1 == null && i+1<points.length) {
				point1 = points[i+1];
				i++;
			}
			for (; i<points.length; i++) {
				point2 = points[i];
				while (point2==null && i+1<points.length) {
					point2 = points[i+1];
					i++;
				}
				if (point2 != null) {
					dist += Measure.distance(point1, point2);
				}
				point1 = point2;
			}
		}
		return dist;
	}
	public static double distance(Point[] points1, Point[] points2) {
		double dist = Double.MAX_VALUE;
		if (points1!=null && points2!=null) {
			for (int i=0; i<points1.length; i++) {
				Point point1 = points1[i];
				for (int j=0; j<points2.length; i++) {
					Point point2 = points2[j];
					double thisDist = Measure.distance(point1, point2);
					if (thisDist < dist) {
						dist = thisDist;
					}
				}
			}
		}
		return dist;
	}
	public static double distance(Point point, Road road) {
		if (null==point || (double)-1==point.getX() || (double)-1==point.getY() || null==road || road.getPoints().length < 1) {
			Exception e = new Exception("point or road is not assigned!");
			e.printStackTrace();
		}
		
		// to distiguate left and right road
		Road origRoad = road;
		road = roadDistTransform.road(origRoad);
		// =============================
		
		double dist = -1;
		double mindist = Double.MAX_VALUE;
		Point[] points = road.getPoints();
		
		Point cpoint = points[0];
		mindist = Measure.distance(point, cpoint);
		
		Point prevPoint = points[0];
		for (int i = 1; i < points.length; i++) {
			Point thisPoint = points[i];
			
			Segment seg = new Segment(prevPoint, thisPoint);
			Point tempCPoint = seg.nearestTo(point);

			dist = Measure.distance(point, tempCPoint);
			if (dist < mindist) {
				mindist = dist;
			}
			prevPoint = thisPoint;
		}

		// to distiguate left and right road
		road = origRoad;
		// =============================
		
		return mindist;
	}

	public static int angle(Point aPoint, Point bPoint) {
		int angle = -1;
		if (null!=aPoint && null!=bPoint) {
			double x1 = aPoint.getX();
			double y1 = aPoint.getY();
			double x2 = bPoint.getX();
			double y2 = bPoint.getY();
			int x1x2 = Double.compare(x1, x2);
			int y1y2 = Double.compare(y1, y2);
			
			if (0==x1x2) {
				if (0==y1y2) {
					angle = -1;
				} else if (-1==y1y2) {
					angle = 90;
				} else if (1==y1y2) {
					angle = 270;
				}
			} else if (-1==x1x2) {
				double tan = (y2-y1) / (x2-x1);
				angle = (int)(Math.atan(tan) / Math.PI * 180);
				if (angle<0) {
					angle = 360+angle;
				}
			} else if (1==x1x2) {
				double tan = (y2-y1) / (x2-x1);
				angle = (int)(Math.atan(tan) / Math.PI * 180);
				angle = 180+angle;
			}
		}
		
		return angle;
	}

	public static double angleInRadian(Point aPoint, Point bPoint) {
		double angle = -1;
		if (null!=aPoint && null!=bPoint) {
			double x1 = aPoint.getX();
			double y1 = aPoint.getY();
			double x2 = bPoint.getX();
			double y2 = bPoint.getY();
			int x1x2 = Double.compare(x1, x2);
			int y1y2 = Double.compare(y1, y2);
			
			if (0==x1x2) {
				if (0==y1y2) {
					angle = -1;
				} else if (-1==y1y2) {
					angle = Math.PI / 2;
				} else if (1==y1y2) {
					angle = 3 * Math.PI / 2;
				}
			} else if (-1==x1x2) {
				double tan = (y2-y1) / (x2-x1);
				angle = Math.atan(tan);
				if (angle<0) {
					angle = 2*Math.PI+angle;
				}
			} else if (1==x1x2) {
				double tan = (y2-y1) / (x2-x1);
				angle = Math.atan(tan);
				angle = Math.PI+angle;
			}
		}
		
		return angle;
	}
	public static int angle(Point[] points) {
		int angle = -1;
		double len = distance(points);
		if (points.length>=2) {
			Point aPoint, bPoint;
			double dangle = 0;
			aPoint = points[0];
			for (int i=1; i<points.length; i++) {
				bPoint = points[i];
				int tempAng = angle(aPoint, bPoint);
				if (tempAng==-1) {
					if (bPoint!=null) {
						aPoint = bPoint;
					}
					continue;
				}
				angle = 0;
				dangle += (tempAng*distance(aPoint, bPoint)/len);
				aPoint = bPoint;
			}
			if (0==angle) {
				angle = (int) dangle; 
			}
		}
		return angle;
	}
	public static int angle(Road road) {
		int angle = -1;
		if (null!=road && road.getPoints().length>=2) {
			Point[] points = road.getPoints();
			angle = angle(points);
		}
		return angle;
	}

	public static boolean isAngleSimilar(int angle1, int angle2, int threshold) {
		boolean similar = true;
		
		if (angle2==-1 || angle1==-1) return similar;
		
		int delta = Math.abs(angle1-angle2);
		if (delta>180) delta = 360 - delta;
		if (delta>threshold) similar = false;
		
		return similar;
	}
	public static boolean isTwoPointsNear(Point point1, Point point2, double threshold) {
		boolean isNear = false;
		if (Measure.distance(point1, point2) < threshold) {
			isNear = true;
		} else {
			System.out.println(Measure.distance(point1, point2));
		}
		return isNear;
	}
	public static boolean isTwoPointsNear(Point point1, Point point2) {
		return isTwoPointsNear(point1, point2, Constant.MAX_TWO_POINT_DISTANCE_SHIFT);
	}
	public static boolean isTwoDistancesSimilar(double length1, double length2) {
		return Measure.isTwoDistancesSimilar(length1, length2, Constant.MAX_TWO_POINT_DISTANCE_SHIFT);
	}
	public static boolean isTwoDistancesSimilar(double length1, double length2, double threshold) {
		boolean isNear = false;
		if (Math.abs(length1-length2) < threshold) {
			isNear = true;
		}
		return isNear;
	}
	public static boolean isTwoDistanceSimilarInPercent(double length1, double length2) {
		boolean isNear = false;
		if (Math.abs(2*(length1-length2)/(length1+length2)) < Constant.MAX_TWO_DISTANCE_SHIFT_PERCENT) {
			isNear = true;
		}
		return isNear;
	}
}
