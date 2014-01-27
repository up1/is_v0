package demo.tarad;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import com.stromberglabs.jopensurf.Surf;

public class MyPrototype {

	public final static String BASE_PATH = "D:\\Somkiat\\เรียนโท\\ku\\is\\tarad\\";
	private List<String> fileNames = new ArrayList<String>();

	public static void main(String[] args) {
		MyPrototype p = new MyPrototype();
		p.listFilesForFolder(new File(BASE_PATH + "bag\\"));
		List<InputData> inputImages = p.extractDatas();
		p.searchData(inputImages, BASE_PATH + "target_phone.jpg");
	}

	public List<String> listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				String fileName = fileEntry.getAbsolutePath();
				if (fileName.toLowerCase().endsWith(".jpg")) {
					System.out.println(fileName);
					fileNames.add(fileName);
				}
			}
		}
		return fileNames;
	}

	public List<InputData> extractDatas() {
		List<InputData> inputImages = new ArrayList<InputData>();
		try {
			for (String fileName : fileNames) {
				BufferedImage imageBuffer = ImageIO.read(new File(fileName));
				inputImages.add(new InputData(new Surf(imageBuffer), fileName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputImages;
	}

	public void searchData(List<InputData> inputImages, String targetImage) {
		try {
			BufferedImage imageTarget = ImageIO.read(new File(targetImage));
			Surf targetSurf = new Surf(imageTarget);

			for (InputData inputData : inputImages) {
				int matchingPoint = targetSurf.getMatchingPoints(inputData.getSurf(), false).size();
				inputData.setScore(matchingPoint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Collections.sort(inputImages, new InputDataComparable());
		for (InputData inputData : inputImages) {
			System.out.print(inputData.getFileName());
			System.out.println(", " + inputData.getScore());
		}

	}

}

