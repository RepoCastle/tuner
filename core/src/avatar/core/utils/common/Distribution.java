/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.common.Distribution
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.common;

public class Distribution {
	public static double normal(double distance, double mean, double deviation) {
		double score = Math.exp((distance - mean) * (mean - distance)
				/ (2 * deviation * deviation))
				/ (Math.sqrt(2 * Math.PI) * deviation);
		return score;
	}
	
	public static double normrand() {
		double mu = 0;
		double sigma = 1;

		double r, sum = 0.0;
		for (int i = 1; i <= 12; i++)
			sum = sum + Math.random();
		r = (sum - 6.00) * sigma + mu;

		return r;
	}
	
	public static double normrandDist(double maxDistanceError) {
		double dist = 0;
		double sigma = 1;
		double r = normrand();

		// 2*sigma makes 95% of the distance if in +- maxDistanceError
		// please refer to http://en.wikipedia.org/wiki/Normal_distribution.
		if (r>2*sigma) {
			r=2*sigma;
		}
		dist = r / (2*sigma) * maxDistanceError;
		return dist;
	}
	
	public static void main(String[] args) {
		System.out.println(Distribution.normrandDist(40));
	}
}
