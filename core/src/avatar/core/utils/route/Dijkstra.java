/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.route.Dijkstra
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.route;

import avatar.core.model.common.Graph;
import avatar.core.utils.common.Constant.RouteType;

public class Dijkstra extends Route {
	public Dijkstra(Graph graph) {
		super(graph);
		this.routeType = RouteType.Dijkstra;
	}
}
