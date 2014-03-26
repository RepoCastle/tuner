/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.mapmatching.algorithm.SPM
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.mapmatching.algorithm;

import avatar.core.model.geometry.Path;
import avatar.core.model.geometry.Region;
import avatar.core.utils.mapmatching.MMIface;
import avatar.core.utils.route.Route;

public class SPM extends HMM implements MMIface {

	public SPM(Region region, Route route) {
		super(region, route);
	}

	protected double getSpatialScore(double distance) {
//		double score = Distribution.normal(distance,
//				Constant.NORMAL_DISTANCE_ERROR_MEAN,
//				Constant.NORMAL_DISTANCE_ERROR_DEVIATION);
//		return score;
		return 1;
	}

	protected double getPathScore(Path path, double repsDist) {
//		double score = -1;
//		if (path != null) {
//			double beta, delta;
//
//			beta = 50 / Math.log(2);
//			delta = Math.abs(repsDist - path.getLength());
//			score = Math.exp(-delta / beta) / beta;
//		}
//		return score;
		return 10000-path.getLength();
	}
}
