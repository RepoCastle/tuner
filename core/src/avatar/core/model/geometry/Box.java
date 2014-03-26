/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.geometry.Box
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.model.geometry;

import avatar.core.model.common.Point;

public class Box {
	private double xmin;
	private double ymin;
	private double xmax;
	private double ymax;

	public Box() {
		this.xmin = 0.0;
		this.ymin = 0.0;
		this.xmax = 0.0;
		this.ymax = 0.0;
	}

	public Box(double xmin, double ymin, double xmax, double ymax) {
		if (xmin > xmax) {
			double temp = xmin;
			xmin = xmax;
			xmax = temp;
		}
		if (ymin > ymax) {
			double temp = ymin;
			ymin = ymax;
			ymax = temp;
		}
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}

	public Box(Box box) {
		this.xmin = box.xmin;
		this.ymin = box.ymin;
		this.xmax = box.xmax;
		this.ymax = box.ymax;
	}
	
	public Box(Box box1, Box box2) {
		this.xmin = Math.min(box1.xmin, box2.xmin);
		this.ymin = Math.min(box1.ymin, box2.ymin);
		this.xmax = Math.max(box1.xmax, box2.xmax);
		this.ymax = Math.max(box1.ymax, box2.ymax);
	}

	public double getXmin() {
		return xmin;
	}

	public void setXmin(double xmin) {
		this.xmin = xmin;
	}

	public double getYmin() {
		return ymin;
	}

	public void setYmin(double ymin) {
		this.ymin = ymin;
	}

	public double getXmax() {
		return xmax;
	}

	public void setXmax(double xmax) {
		this.xmax = xmax;
	}

	public double getYmax() {
		return ymax;
	}

	public void setYmax(double ymax) {
		this.ymax = ymax;
	}

	public boolean inBox(Box box) {
		return (1 != Double.compare(this.xmin, box.xmin)
				&& 1 != Double.compare(this.ymin, box.ymin)
				&& -1 != Double.compare(this.xmax, box.xmax) && -1 != Double
				.compare(this.ymax, box.ymax));
	}

	public boolean contains(Point point) {
		double px, py;
		px = point.x;
		py = point.y;
		return (1 != Double.compare(this.xmin, px)
				&& 1 != Double.compare(this.ymin, py)
				&& -1 != Double.compare(this.xmax, px) && -1 != Double.compare(
				this.ymax, py));
	}

	public String toString() {
		return "(" + xmin + ", " + ymin + ", " + xmax + ", " + ymax + ")";
	}
}
