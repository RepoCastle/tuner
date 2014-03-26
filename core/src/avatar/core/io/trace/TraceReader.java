/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.io.trace.TraceReader
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.io.trace;

import java.util.ArrayList;

import avatar.core.model.mobject.Report;

public interface TraceReader {
	public ArrayList<Report> get(String lpn, long stime, long etime) throws Exception ;
}
