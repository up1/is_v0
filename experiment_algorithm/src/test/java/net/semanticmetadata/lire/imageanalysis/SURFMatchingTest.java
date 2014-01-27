package net.semanticmetadata.lire.imageanalysis;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.stromberglabs.jopensurf.SURFInterestPoint;
import com.stromberglabs.jopensurf.Surf;

public class SURFMatchingTest {
	private String[] testFiles = new String[] { "img01.jpg", "img02.jpg", "img03.jpg", "img04.jpg", "img05.jpg", "img06.jpg", "img07.jpg", "img08.jpg", "img09.jpg", "img10.jpg" };
	private final String testFilesPath = "D:\\Somkiat\\เรียนโท\\ku\\is\\tarad\\variety\\";

	@Test
	public void test1() throws Exception {
		
		BufferedImage imageTest = ImageIO.read(new FileInputStream(testFilesPath + testFiles[6]));
		Surf surfTest = new Surf(imageTest);

		for (int i = 0; i < testFiles.length; i++) {
			BufferedImage image = ImageIO.read(new FileInputStream(testFilesPath + testFiles[i]));
			Surf surf = new Surf(image);
			Map<SURFInterestPoint, SURFInterestPoint> matchingPoints = surf.getMatchingPoints(surfTest, false);
			System.out.println(i+1 + ":" + matchingPoints.size());
		}
	}
}
