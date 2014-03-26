/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.common.Point
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:06
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.model.common;

import avatar.core.model.geometry.Box;
import avatar.core.utils.common.Constant;

public class Point {
	public double x;
	public double y;

	public Point() {
		x = -1;
		y = -1;
	}

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point(Point point) {
		this.x = point.x;
		this.y = point.y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public boolean inBox(Box box) {
		boolean isIn = false;
		if (this.x <= box.getXmax() && this.x >= box.getXmin()
				&& this.y <= box.getYmax() && this.y >= box.getYmin())
			isIn = true;
		return isIn;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Point) {
			Point pt = (Point) obj;
			return (Double.compare(x, pt.x) == 0)
					&& (Double.compare(y, pt.y) == 0);
		} else {
			return super.equals(obj);
		}
	}
	
//	public int hashCode() {  
//        final int prime = 31;  
//        int result = 1;
//        Integer xpart = (int)(x*100000);
//        Integer ypart = (int)(y*100000);
//        result = prime * result + xpart.hashCode();  
//        result = prime * result + ypart.hashCode();
//        return result;  
//    } 
//	public int hashCode() {
//        long bits = java.lang.Double.doubleToLongBits(getX());
//        bits ^= java.lang.Double.doubleToLongBits(getY()) * 31;
//        return (((int) bits) ^ ((int) (bits >> 32)));
//    }

	public String toString() {
		String line = null;
		line = Constant.gpsDec.format(x) + "," + Constant.gpsDec.format(y);
		return line;
	}
}
