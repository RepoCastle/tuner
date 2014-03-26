/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.mapmatching.MMIface
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.mapmatching;

import avatar.core.model.mobject.Trace;
import avatar.core.model.mobject.Trajectory;

public interface MMIface {
	//FIXME: arraylist version
	//	public Match map(Trace trace);
	public Trajectory map(Trace trace);
//	public String estimate(Trace trace);
}
