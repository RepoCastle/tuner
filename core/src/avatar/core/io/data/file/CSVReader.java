/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.io.file.CSVReader
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:07
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.io.data.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import avatar.core.model.mobject.Report;
import avatar.core.model.mobject.Trace;
import avatar.core.utils.common.Constant.DataSourceType;

public class CSVReader {
	private RandomAccessFile csv;
	private CSVConvertor convertor;

	public CSVReader(String filename, DataSourceType srcType)
			throws FileNotFoundException {
		this.csv = new RandomAccessFile(filename, "r");
		this.convertor = new CSVConvertor(srcType);
	}

	public Report next() {
		Report report = null;
		try {
			String line = csv.readLine();
			if (line != null) {
				report = convertor.line2rep(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return report;
	}

	public void close() throws IOException {
		csv.close();
	}

	public static Trace get(String csvFile) throws Exception {
		Trace trace = null;
		String lpn = "";
		
		ArrayList<Report> reports = null;
		CSVReader csv = new CSVReader(csvFile, DataSourceType.SHTAXI);

		reports = new ArrayList<Report>();
		Report report;
		while ((report = csv.next()) != null) {
			reports.add(report);
		}
		csv.close();
		if (reports.size()>0) {
			lpn = reports.get(0).getLpn();
		}
		trace = new Trace(lpn, reports);
		return trace;
	}
}
