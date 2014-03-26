/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.mobject.Trajectory
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.model.mobject;

import avatar.core.model.geometry.Path;

public class Trajectory {
	private Trace trace;
	private Path path;
	
	private String trajID;
	private String basedID;
	private String userID;
	private String traceID;
	private Long stime;
	private Long etime;
	
	public Trajectory(Trace trace, Path path) {
		this.trace = trace;
		this.path = path;
	}
	public Trace getTrace() {
		return trace;
	}
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
	public String getTrajID() {
		return trajID;
	}
	public void setTrajID(String trajID) {
		this.trajID = trajID;
	}
	public String getBasedID() {
		return basedID;
	}
	public void setBasedID(String basedID) {
		this.basedID = basedID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getTraceID() {
		return traceID;
	}
	public void setTraceID(String traceID) {
		this.traceID = traceID;
	}
	public Long getStime() {
		return stime;
	}
	public void setStime(Long stime) {
		this.stime = stime;
	}
	public Long getEtime() {
		return etime;
	}
	public void setEtime(Long etime) {
		this.etime = etime;
	}
}
