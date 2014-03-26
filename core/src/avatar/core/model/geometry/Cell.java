/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.geometry.Cell
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.model.geometry;

import java.util.HashMap;

import avatar.core.model.common.Edge;
import avatar.core.model.common.Vertex;

public class Cell {
	private int xNumber;
	private int yNumber;
	private Box box;
	private HashMap<Integer, Edge> roads;
	private HashMap<Integer, Vertex> crosses;

	public Cell() {
		this.roads = new HashMap<Integer, Edge> ();
		this.crosses = new HashMap<Integer, Vertex> ();
	}
	public int getxNumber() {
		return xNumber;
	}
	public void setxNumber(int xNumber) {
		this.xNumber = xNumber;
	}
	public int getyNumber() {
		return yNumber;
	}
	public void setyNumber(int yNumber) {
		this.yNumber = yNumber;
	}
	public Box getBox() {
		return box;
	}
	public void setBox(Box box) {
		this.box = box;
	}
	public HashMap<Integer, Edge> getRoads() {
		return roads;
	}
	public HashMap<Integer, Vertex> getCrosses() {
		return crosses;
	}
	
	public String toString() {
		return xNumber + "," + yNumber;
	}
}
