/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.mapmatching.algorithm.STM
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.mapmatching.algorithm;

import avatar.core.model.geometry.Path;
import avatar.core.model.geometry.Region;
import avatar.core.utils.mapmatching.MMIface;
import avatar.core.utils.route.Route;

public class STM extends HMM implements MMIface {

	public STM(Region region, Route route) {
		super(region, route);
	}

	protected double getPathScore(Path path, double repsDist) {
		double score = -1;
		if (repsDist != 0 && path.getLength() == 0) {
			score = 0;
		} else if (path.getLength() != 0) {
			score = repsDist / path.getLength();
		} else {
			score = 0;
		}
		return score;
	}
}
