/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.model.mobject.Report
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.model.mobject;

import avatar.core.model.common.Point;
import avatar.core.utils.common.Date;

public class Report {
	protected String id;
	protected String lpn;
	protected Point point;
	protected short speed;
	protected short angle;
	protected long timestamp;
	protected char status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLpn() {
		return lpn;
	}

	public void setLpn(String lpn) {
		this.lpn = lpn;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point gpoint) {
		this.point = gpoint;
	}

	public short getSpeed() {
		return speed;
	}

	public void setSpeed(short speed) {
		this.speed = speed;
	}

	public short getAngle() {
		return angle;
	}

	public void setAngle(short angle) {
		this.angle = angle;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}
	
	public String toString() {
		String line = "";
		line += this.getId() + ",";
		line += this.getLpn() + ",";
		line += this.getPoint().toString() + ",";
		line += this.getSpeed() + ",";
		line += this.getAngle() + ",";
		line += Date.mills2str(this.getTimestamp()) + ",";
		line += this.getStatus();
		return line;
	}
}
