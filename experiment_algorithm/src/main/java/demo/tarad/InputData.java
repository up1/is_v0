package demo.tarad;

import com.stromberglabs.jopensurf.Surf;

public class InputData {
	public Surf surf;
	public String fileName;
	public int score;

	public InputData(Surf surf, String fileName) {
		this.surf = surf;
		this.fileName = fileName;
	}

	public Surf getSurf() {
		return surf;
	}

	public void setSurf(Surf surf) {
		this.surf = surf;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}