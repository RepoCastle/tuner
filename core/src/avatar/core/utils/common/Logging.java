/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.common.Logging
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:06
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.common;

public class Logging {

	public static String info(String msg) {
		return info(msg, 0);
	}
	
	public static String warn(String msg) {
		return warn(msg, 0);
	}
	
	public static String debug(String msg) {
		return debug(msg, 0);
	}
	
	public static String info(String msg, int identNum) {
		String showStr = "";
		
		String time = Date.mills2str(Date.getTimeInMills());
		showStr += "[" + time + "]\t";
		for (int i=0; i<identNum; i++) {
			showStr += "\t";
		}
		showStr += msg;
		
		System.out.println(showStr);
		
		return showStr;
	}
	
	public static String warn(String msg, int identNum) {
		String showStr = "";
		
		String time = Date.mills2str(Date.getTimeInMills());
		showStr += "[" + time + "]\t";
		for (int i=0; i<identNum; i++) {
			showStr += "\t";
		}
		showStr += msg;
		
//		System.out.println("WARNING: " + showStr);
		
		return showStr;
	}
	
	public static String debug(String msg, int identNum) {
		String showStr = "";
		String time = Date.mills2str(Date.getTimeInMills());
		showStr += "[" + time + "] DEBUG INFO: ";
		for (int i=0; i<identNum; i++) {
			showStr += "\t";
		}
		showStr = showStr + msg;
		System.out.println(showStr);
		
		return showStr;
	}
}
