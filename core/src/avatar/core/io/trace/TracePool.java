/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.io.trace.TracePool
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.io.trace;

//import inc.thrift.TraceSource;
//import inc.thrift.TrajSource;
import java.util.ArrayList;

import avatar.core.model.mobject.Report;
import avatar.core.utils.common.Constant.TracePoolType;
import avatar.core.utils.common.Constant.TraceType;
import avatar.core.utils.common.Date;
import avatar.core.utils.common.Logging;

/*
 * TracePool is used to hide the detail of the storage of original raw trajectory data.
 * Fetching trace from "DB" and "FS" are both supported in this version.
 */
public class TracePool {

	TraceReader traceReader;

	public TracePool(TracePoolType srcType) throws Exception {
		/* if (TracePoolType.DB.equals(srcType)) {
			// FIXME: set the queryPool from the properties
			String url = Constant.dbUrl;
			String user = Constant.dbUser;
			String passwd = Constant.dbPasswd;
			QueryPool queryPool = new QueryPool(url, user, passwd);

			this.traceReader = new DBTraceReader(queryPool);
		} else */ if (TracePoolType.FS.equals(srcType)) {
			this.traceReader = new FSTraceReader();
		} else {
			Exception e = new Exception("Unsupported source type: " + srcType);
			throw e;
		}
	}

	public ArrayList<Report> get(String lpn, long stime, long etime) {
		ArrayList<Report> reports = null;
		try {
			reports = this.traceReader.get(lpn, stime, etime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reports;
	}

	public ArrayList<Report> get(String lpn, String stime, String etime) {
		ArrayList<Report> reports = null;
		try {
			long sltime = -1, eltime = -1;
			try {
				sltime = Date.str2mills(stime);
				eltime = Date.str2mills(etime);
			} catch (Exception e) {
				if (sltime == -1)
					sltime = 0;
				if (eltime == -1)
					eltime = Date.getTimeInMills();
			}
			reports = this.traceReader.get(lpn, sltime, eltime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reports;
	}

	public ArrayList<Report> get(String csvFile) {
		ArrayList<Report> reports = null;
		try {
			reports = ((FSTraceReader)this.traceReader).get(csvFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reports;
	}

	public static void main(String[] args) {
		try {
			TracePool tracePool = new TracePool(TracePoolType.FS);
			ArrayList<Report> reports = tracePool.get("109", "2007-11-01 00:00:00", "2007-11-01 23:59:59");
			Logging.info("" + reports.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
