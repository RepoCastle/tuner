/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.route.AStar
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.route;

import avatar.core.model.common.Graph;
import avatar.core.utils.common.Constant.RouteType;

public class AStar extends Route {

	public AStar(Graph graph) {
		super(graph);
		this.routeType = RouteType.AStar;
	}

}
