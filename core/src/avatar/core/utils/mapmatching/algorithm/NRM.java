/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.mapmatching.algorithm.NRM
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.mapmatching.algorithm;

import avatar.core.model.geometry.Region;
import avatar.core.model.mobject.Trace;
import avatar.core.model.mobject.Trajectory;
import avatar.core.utils.common.Constant;
import avatar.core.utils.mapmatching.MMBase;
import avatar.core.utils.mapmatching.MMIface;
import avatar.core.utils.route.Route;

public class NRM extends MMBase implements MMIface {

	public NRM(Region region, Route route) {
		super(region, route);
	}

	//FIXME: arraylist version
	/*
	@Override
	public Match map(Trace trace) {
		Match match = null;
		try {
			match = setupCandidates(trace, Constant.MAX_CAND_NUM);
			getBestRoute(match);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return match;
	}
	 */
//	public String estimate (Trace trace) {
//		ArrayList<Match> matches = null;
//		String content = "";
//		try {
//			matches = setupCandidates(trace, Constant.MAX_CAND_NUM);
//			for (Match match: matches) {
//				List<VertexTime> vertices = estimateVertexTime(match);
//				for (VertexTime vertexTime: vertices) {
//					content += vertexTime + "\n";
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return content;
//	}

	@Override
	public Trajectory  map(Trace trace) {
		Trajectory traj = null;
		try {
			traj = setupCandidates(trace, Constant.MAX_CAND_NUM);
			getBestRoute(traj);
			traj.setTraceID(trace.getTraceID());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return traj;
	}

//	public static void main(String[] args) {
//		String mapfile = "/home/avatar/data/maps/shanghai.region";
//		Map map = new Reg();
//		map.load(mapfile);
//		Region region = new Region(map);
//		Route route = new Route(region);
//		MMIface mm = new NRM(region, route);
//		System.out.println("NRM::map loadded");
//		try {
//			TracePool tracePool = new TracePool(TracePoolType.FS);
//			Trace trace = Trace.get("107", 0, 1, tracePool);
//			System.out.println("NRM::trace loadded");
//			Match match = mm.map(trace);
//			System.out.println(match.getPath().getLength());
//			System.out.println("NRM::map matching done");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
