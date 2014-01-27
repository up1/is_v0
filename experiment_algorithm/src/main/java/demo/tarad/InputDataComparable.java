package demo.tarad;

import java.util.Comparator;

class InputDataComparable implements Comparator<InputData> {

	public int compare(InputData source, InputData target) {
		return (source.getScore() > target.getScore() ? -1 : (source.getScore() == target.getScore() ? 0 : 1));
	}
}