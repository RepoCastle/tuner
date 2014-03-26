/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.io.file.CSVConvertor
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.io.data.file;

import java.io.IOException;

import avatar.core.model.common.Point;
import avatar.core.model.mobject.Report;
import avatar.core.utils.common.Date;
import avatar.core.utils.common.Constant.DataSourceType;

public class CSVConvertor {
	private long szTID = 1;
	private DataSourceType srcType;

	public CSVConvertor(DataSourceType srcType) {
		this.srcType = srcType;
	}

	public Report line2rep(String line) throws IOException, Exception {
		Report report = null;
		String[] infos;
		if (null == line)
			infos = null;
		else
			infos = line.split(",");

		if (null != infos) {
			if (DataSourceType.SHTAXI.equals(srcType)) {
				report = new Report();
				report.setId(infos[0].trim());
				report.setLpn(infos[1].trim());
				double x = Double.parseDouble(infos[2].trim());
				double y = Double.parseDouble(infos[3].trim());
				report.setPoint(new Point(x, y));
				report.setSpeed(Short.parseShort(infos[4].trim()));
				report.setAngle(Short.parseShort(infos[5].trim()));
				report.setTimestamp(Date.str2mills(infos[6].trim()));
				report.setStatus(infos[7].trim().charAt(0));
			} else if (DataSourceType.SZTAXI.equals(srcType)) {
				report = new Report();
				report.setId((this.szTID++) + "");
				report.setLpn(infos[0].trim());
				report.setTimestamp(Date.str2mills(infos[1].trim()));
				double x = Double.parseDouble(infos[2].trim());
				double y = Double.parseDouble(infos[3].trim());
				report.setPoint(new Point(x, y));
				report.setSpeed(Short.parseShort(infos[4].trim()));
				report.setAngle(Short.parseShort(infos[5].trim()));
				int statuse = Integer.parseInt(infos[6].trim());
				int status = statuse & (byte) 0x07;
				char statusChar = '1';
				if (status == 0) {
					statusChar = '0';
				}
				report.setStatus(statusChar);
			} else if (DataSourceType.SHBUS.equals(srcType)) {
				report = new Report();
				report.setId(infos[1].trim() + "-" + infos[0].trim() + "-"
						+ infos[3].trim());
				report.setLpn(infos[2].trim());
				report.setTimestamp(Date.str2mills(infos[12].trim()));
				double x = Double.parseDouble(infos[6].trim());
				double y = Double.parseDouble(infos[7].trim());
				report.setPoint(new Point(x, y));
			} else if (DataSourceType.SZBUS.equals(srcType)) {
			}
		}
		return report;
	}

	public String rep2line(Report report) throws Exception {
		String line = "";
		if (report != null) {
			if (DataSourceType.SHTAXI.equals(srcType)) {
				line += report.getId() + ",";
				line += report.getLpn() + ",";
				line += report.getPoint().toString() + ",";
				line += report.getSpeed() + ",";
				line += report.getAngle() + ",";
				line += Date.mills2str(report.getTimestamp()) + ",";
				line += report.getStatus();
			} else if (DataSourceType.SZTAXI.equals(srcType)) {
			} else if (DataSourceType.SHBUS.equals(srcType)) {
				line += report.getId() + ",";
				line += report.getLpn() + ",";
				line += report.getPoint().toString() + ",";
				line += 0 + ",";
				line += 0 + ",";
				line += Date.mills2str(report.getTimestamp()) + ",";
				line += 0;
			} else if (DataSourceType.SZBUS.equals(srcType)) {
			}
		}
		return line;
	}
}
