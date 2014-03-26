/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.mobject.Trace
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:06
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.model.mobject;

import java.util.ArrayList;

import avatar.core.model.common.Point;
import avatar.core.utils.common.Constant;
import avatar.core.utils.common.Measure;

public class Trace {
	private String traceID;
	private String lpn;
	private ArrayList<Report> reports;
	private int count;

	private ArrayList<Report> dedupReports(ArrayList<Report> reports) {
		if (reports == null || reports.size() == 0) return reports;
		
		int repCnt = reports.size();
		Report prevRep = null;
		Report thisRep = null;
		prevRep = reports.get(0);
		for (int i=1; i<repCnt; i++) {
			thisRep = reports.get(i);
			if (Measure.distance(thisRep.getPoint(), prevRep.getPoint()) <= Constant.MIN_REPORT_DISTANCE) {
				reports.remove(i);
				i--;
				repCnt--;
			} else {
				prevRep = thisRep;
			}
		}
		return reports;
	}

	public Trace() {
		this.lpn = "";
		this.reports = new ArrayList<Report>();
		this.count = 0;
	}

	public Trace(String lpn, ArrayList<Report> reports) {
		this.lpn = lpn;
		this.reports = dedupReports(reports);
		this.count = 0;
	}

	public void setLpn(String lpn) {
		this.lpn = lpn;
	}

	public String getLpn() {
		return lpn;
	}

	public long getStime() {
		long stime = -1;
		if (reports != null && reports.size() > 0) {
			stime = this.reports.get(0).getTimestamp();
		}
		return stime;
	}

	public long getEtime() {
		long etime = -1;
		if (reports != null && reports.size() > 0) {
			int last = this.reports.size() - 1;
			etime = this.reports.get(last).getTimestamp();
		}
		return etime;
	}

	public ArrayList<Report> getReports() {
		return reports;
	}

	public void setReports(ArrayList<Report> reports) {
		this.reports = dedupReports(reports);
	}
	public String getTraceID() {
		return traceID;
	}

	public void setTraceID(String traceID) {
		this.traceID = traceID;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public void setReports(ArrayList<Point> points, int timeInterval, short speed) {
		ArrayList<Report> reports = new ArrayList<Report>();
		int pointCnt = points.size();

		long timemills = 0;
		for (int i=0; i<pointCnt; i++) {
			Point point = points.get(i);
			Report report = new Report();
			report.setId(Integer.toString(i));
			report.setLpn(this.lpn);
			report.setPoint(point);
			report.setSpeed(speed);
			report.setAngle((short)0);
			report.setTimestamp(timemills);
			timemills += timeInterval*1000;
			report.setStatus('u');
			reports.add(report);
		}
		this.reports = dedupReports(reports);
	}
	
	public String toString() {
		String traceStr = "";
		int repCnt = reports.size();
		for (int i=0; i<repCnt; i++) {
			Report report = reports.get(i);
			traceStr += report.toString() + "\n";
		}
		return traceStr;
	}
	
	public static  void main(String[] args) {
		Trace trace = new Trace();
		ArrayList<Point> points = new ArrayList<Point>();
		Point point = null;
		point = new Point(121.5, 31.5);
		points.add(point);
		point = new Point(121.5, 31.5);
		points.add(point);
		point = new Point(121.5, 31.5);
		points.add(point);
		point = new Point(121.5, 31.5);
		points.add(point);
		trace.setReports(points, 15, (short)10);
		System.out.println(trace.getReports().size());
	}
}
