/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.utils.runner.MapMatching
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.utils.runner;

import java.util.ArrayList;

import avatar.core.io.trace.TracePool;
import avatar.core.io.map.Reg;
import avatar.core.model.geometry.Region;
import avatar.core.model.mobject.Report;
import avatar.core.model.mobject.Trace;
import avatar.core.model.mobject.Trajectory;
import avatar.core.utils.common.Constant.TracePoolType;
import avatar.core.utils.common.City;
import avatar.core.utils.common.Constant;
import avatar.core.utils.common.Logging;
import avatar.core.utils.common.Measure;
import avatar.core.utils.mapmatching.MMIface;
import avatar.core.utils.mapmatching.algorithm.SPM;
import avatar.core.utils.route.AStar;
import avatar.core.utils.route.Route;

public class MapMatching {
	
	public void temp () {
	}
	public static void main(String[] args) {
		Reg map = new Reg(City.get("shanghai"));
		map.load();
		Region region = new Region(map, Constant.CELL_UNIT_LENGTH);
		Route route = new AStar(region);
		
		Trace trace = null;
		
		String lpn = "null";
		String stime = "2007-11-01 00:44:44";
		String etime = "2007-11-01 23:45:13";
		MMIface mapmatching = new SPM(region, route);
		
		try {
			TracePool tracePool = new TracePool(TracePoolType.FS);
			ArrayList<Report> reports = tracePool.get(lpn, stime, etime);

			double dist = 0;
			for (int i=1; i<reports.size(); i++) {
				Report prevRep = reports.get(i-1);
				Report thisRep = reports.get(i);
				dist += Measure.distance(prevRep.getPoint(), thisRep.getPoint());
			}
			trace = new Trace(lpn, reports);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		Trajectory traj = mapmatching.map(trace);
        if (traj!=null) {
            Logging.info("" + traj.getPath().toString());
        } else {
            Logging.info("Failed to map match the trace!");
        }
	}
}
