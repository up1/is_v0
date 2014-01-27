package net.semanticmetadata.lire.imageanalysis;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.stromberglabs.jopensurf.SURFInterestPoint;
import com.stromberglabs.jopensurf.Surf;

public class SURFTest {

	private String[] testFiles = new String[] { "img01.jpg", "img02.jpg", "img03.jpg", "img04.jpg", "img05.jpg", "img06.jpg", "img07.jpg", "img08.jpg", "img09.jpg", "img10.jpg" };
	private final String testFilesPath = "D:\\Somkiat\\เรียนโท\\ku\\is\\tarad\\variety\\";

	@Test
	public void extract() {
		try {
		Surf sm = new Surf(ImageIO.read(new FileInputStream(testFilesPath + "img08.jpg")));
		Surf.saveToFile(sm,"D:\\Somkiat\\เรียนโท\\ku\\is\\surf_test\\surf_test.bin");
		List<SURFInterestPoint> pts = sm.getFreeOrientedInterestPoints();
		for (Iterator<SURFInterestPoint> surfInterestPointIterator = pts.iterator(); surfInterestPointIterator.hasNext();) {
			SURFInterestPoint pt = surfInterestPointIterator.next();
			System.out.println(pt.getX() + ", " + pt.getY());
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
