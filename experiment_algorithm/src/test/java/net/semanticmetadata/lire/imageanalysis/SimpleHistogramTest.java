package net.semanticmetadata.lire.imageanalysis;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.junit.Test;

public class SimpleHistogramTest {
	private String[] testFiles = new String[] { "img01.jpg", "img02.jpg", "img03.jpg", "img04.jpg", "img05.jpg", "img06.jpg", "img07.jpg", "img08.jpg", "img09.jpg", "img10.jpg" };
	private final String testFilesPath = "D:\\Somkiat\\เรียนโท\\ku\\is\\tarad\\variety\\";

	@Test
	public void testMultiFile() {
		try {
			SimpleColorHistogram[] acc = new SimpleColorHistogram[testFiles.length];
			LinkedList<byte[]> vds = new LinkedList<byte[]>();
			for (int i = 0; i < acc.length; i++) {
				System.out.println("Extracting from number " + i);
				acc[i] = new SimpleColorHistogram();
				acc[i].extract(ImageIO.read(new FileInputStream(testFilesPath + testFiles[i])));
				vds.add(acc[i].getByteArrayRepresentation());
			}

			System.out.println("Calculating distance for " + testFiles[5]);
			for (int i = 0; i < acc.length; i++) {
				float distance = acc[i].getDistance(acc[5]);
				// System.out.println(testFiles[i] + " distance = " + distance);
				System.out.println(distance);
			}
			int count = 0;
			for (Iterator<byte[]> iterator = vds.iterator(); iterator.hasNext();) {
				byte[] s = iterator.next();
				SimpleColorHistogram temp = new SimpleColorHistogram();
				temp.setByteArrayRepresentation(s);
				float distance = acc[count].getDistance(temp);
				System.out.println(testFiles[count] + " distance = " + distance);
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
