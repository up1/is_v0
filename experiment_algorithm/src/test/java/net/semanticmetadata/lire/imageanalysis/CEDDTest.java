package net.semanticmetadata.lire.imageanalysis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.junit.Test;

public class CEDDTest {

	private String[] testFiles = new String[] { "img01.jpg", "img02.jpg", "img03.jpg", "img04.jpg", "img05.jpg", "img06.jpg", "img07.jpg", "img08.jpg", "img09.jpg", "img10.jpg" };
	private final String testFilesPath = "D:\\Somkiat\\เรียนโท\\ku\\is\\tarad\\variety\\";

	@Test
	public void testSingleFile() throws IOException {
		CEDD c = new CEDD();
		BufferedImage img = ImageIO.read(new File(testFilesPath + "img01.jpg"));
		c.extract(img);
		String s = Arrays.toString(c.data);
		System.out.println("s = " + s);
		byte[] b = c.getByteArrayRepresentation();
		CEDD d = new CEDD();
		d.setByteArrayRepresentation(b);
		System.out.println(d.getDistance(c));
	}

	@Test
	public void testMultiFile() throws Exception {
		CEDD[] acc = new CEDD[testFiles.length];
		LinkedList<String> vds = new LinkedList<String>();
		for (int i = 0; i < acc.length; i++) {
			System.out.println("Extracting from number " + i);
			acc[i] = new CEDD();
			acc[i].extract(ImageIO.read(new FileInputStream(testFilesPath + testFiles[i])));
			vds.add(acc[i].getStringRepresentation());
		}

		System.out.println("Calculating distance for " + testFiles[5]);
		for (int i = 0; i < acc.length; i++) {
			float distance = acc[i].getDistance(acc[5]);
			// System.out.println(testFiles[i] + " distance = " + distance);
			System.out.println(distance);
		}
		int count = 0;
		for (Iterator<String> iterator = vds.iterator(); iterator.hasNext();) {
			String s = iterator.next();
			SimpleColorHistogram a = new SimpleColorHistogram();
			a.setStringRepresentation(s);
			float distance = acc[count].getDistance(a);
			System.out.println(testFiles[count] + " distance = " + distance);
			count++;
		}

	}

}
