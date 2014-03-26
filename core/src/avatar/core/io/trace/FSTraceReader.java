/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.io.FSTraceReader
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
 *************************************************************/
package avatar.core.io.trace;

import java.util.ArrayList;

import avatar.core.io.data.file.CSVReader;
import avatar.core.model.mobject.Report;
import avatar.core.utils.common.Constant;
import avatar.core.utils.common.Constant.DataSourceType;

public class FSTraceReader implements TraceReader {

	public ArrayList<Report> get(String lpn, long stime, long etime)
			throws Exception {
		ArrayList<Report> reports = null;
		String tracePoolDir = Constant.tracePoolDir;
		CSVReader csv = new CSVReader(tracePoolDir + "/" + lpn + ".csv",
				DataSourceType.SHTAXI);

		reports = new ArrayList<Report>();
		Report report;
		while ((report = csv.next()) != null) {
			long time = report.getTimestamp();

			if (time < stime) {
				continue;
			}
			if (time > etime) {
				break;
			}
			reports.add(report);
//			// Delete the reports out of the region of Shanghai.
//			if (report.getPoint().inBox(Constant.REGION_BOX)) {
//				reports.add(report);
//			} else {
//				Logging.warn("" + report.getPoint().x);
//				Logging.warn("" + report.getPoint().y);
//			}
		}

		csv.close();

		return reports;
	}

	public ArrayList<Report> get(String csvFile) throws Exception {
		ArrayList<Report> reports = null;
		CSVReader csv = new CSVReader(csvFile, DataSourceType.SHTAXI);

		reports = new ArrayList<Report>();
		Report report;
		while ((report = csv.next()) != null) {
			reports.add(report);
//			if (report.getPoint().inBox(Constant.REGION_BOX)) {
//				reports.add(report);
//			} else {
//				Logging.warn("" + report.getPoint().x);
//				Logging.warn("" + report.getPoint().y);
//			}
		}

		csv.close();

		return reports;
	}
}
