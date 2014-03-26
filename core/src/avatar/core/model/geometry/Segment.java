/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.geometry.Segment
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.model.geometry;

import avatar.core.model.common.Point;
import avatar.core.utils.common.Constant;

public class Segment {
	private Point aPoint;
	private Point bPoint;

	public Segment(Point aPoint, Point bPoint) {
		this.aPoint = aPoint;
		this.bPoint = bPoint;
	}
	public Point getaPoint() {
		return aPoint;
	}
	public void setaPoint(Point aPoint) {
		this.aPoint = aPoint;
	}
	public Point getbPoint() {
		return bPoint;
	}
	public void setbPoint(Point bPoint) {
		this.bPoint = bPoint;
	}
	
	public Point nearestTo(Point point) {
		if (this.getaPoint().equals(point)) {
			return this.getaPoint();
		} else if (this.getbPoint().equals(point)) {
			return this.getbPoint();
		}
		
		Point cPoint = null;
		double ax, ay, bx, by;
		double cx, cy;
		double gx, gy;
		ax = this.aPoint.getX();
		ay = this.aPoint.getY();
		bx = this.bPoint.getX();
		by = this.bPoint.getY();
		gx = point.getX();
		gy = point.getY();
		
		if(0 == Double.compare(ax, bx)) {
			cx = ax;
			if (1==Double.compare(gy, ay) && 1==Double.compare(gy,by)) {
				if (1==Double.compare(ay, by)) {
					cPoint = this.getaPoint();
				} else {
					cPoint = this.getbPoint();
				}
			} else if (-1==Double.compare(gy, ay) && -1==Double.compare(gy,by)) {
				if (-1==Double.compare(ay, by)) {
					cPoint = this.getaPoint();
				} else {
					cPoint = this.getbPoint();
				}
			} else {
				cy = gy;
				cPoint = new Point(cx, cy);
			}
		} else if (0 == Double.compare(ay, by)) {
			cy = ay;
			if (1==Double.compare(gx, ax) && 1==Double.compare(gx,bx)) {
				if (1==Double.compare(ax, bx)) {
					cPoint = this.getaPoint();
				} else {
					cPoint = this.getbPoint();
				}
			} else if (-1==Double.compare(gx, ax) && -1==Double.compare(gx,bx)) {
				if (-1==Double.compare(ax, bx)) {
					cPoint = this.getaPoint();
				} else {
					cPoint = this.getbPoint();
				}
			} else {
				cx = gx;
				cPoint = new Point(cx, cy);
			}
		} else {
			double a = (by-ay)/(bx-ax);
			cx = (gx + a*(gy-ay) + a*a*ax)/(a*a+1);
			cy = a * (cx - ax) + ay;

			if ((ax-cx)*(bx-cx)>0) {
				if (Math.abs(ax - cx) >= Math.abs(bx - cx)) {
					cPoint = this.getbPoint();
				} else {
					cPoint = this.getaPoint();
				}
			} else {
				cPoint = new Point(cx, cy);
			}
		}

		return cPoint;
	}
	public boolean contain(Point point) {
		boolean has = false;

		double ax, ay, bx, by;
		double cx, cy;
		ax = this.aPoint.getX();
		ay = this.aPoint.getY();
		bx = this.bPoint.getX();
		by = this.bPoint.getY();
		cx = point.getX();
		cy = point.getY();
		
		if (0 == Double.compare(ax, bx)) {
			if ((0==Double.compare(ax, cx)) && 
				((-1!=Double.compare(ay, cy) && -1!=Double.compare(cy, by)) 
			  || (-1!=Double.compare(by, cy) && -1!=Double.compare(cy, ay)))) {
				has = true;
			}
		} else if ((-1!=Double.compare(ax, cx) && -1!=Double.compare(cx, bx))
				 ||(-1!=Double.compare(bx, cx) && -1!=Double.compare(cx, ax))) {
			if ((0==Double.compare(ax, cx) && 0==Double.compare(ay, cy))
			  ||(0==Double.compare(bx, cx) && 0==Double.compare(by, cy))) {
				has = true;
			} else {
				double a = (by-ay)/(bx-ax);
				double gx, gy;
				gx = (cx + a*(cy-ay) + a*a*ax)/(a*a+1);
				gy = a * (gx - ax) + ay;
				if (Math.abs(gx-cx)<Constant.MIN_LNG_DELTA 
				 && Math.abs(gy-cy)<Constant.MIN_LNG_DELTA) {
					has = true;
				}
			}
		}
		return has;
	}
}
