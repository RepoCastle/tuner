/*************************************************************
 * Project: Avatar Demo
 * Class: avatar.core.utils.mapmatching.BestPrev
 * Author: Huajian Mao
 * Purpose: ...
 * LastChanged: 2011-12-28 14:52:05
 * Email: huajianmao@gmail.com
*************************************************************/
package avatar.core.utils.mapmatching;

import avatar.core.model.geometry.Path;

public class BestPrev {
	private Candidate bestPrevCand = null;
	private Path bestPrevPath = null;

	public Candidate getBestPrevCand() {
		return bestPrevCand;
	}

	public void setBestPrevCand(Candidate bestPrevCand) {
		this.bestPrevCand = bestPrevCand;
	}

	public Path getBestPrevPath() {
		return bestPrevPath;
	}

	public void setBestPrevPath(Path bestPrevPath) {
		this.bestPrevPath = bestPrevPath;
	}
}
