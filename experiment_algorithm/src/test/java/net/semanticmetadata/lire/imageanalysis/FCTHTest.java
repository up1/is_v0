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

public class FCTHTest {

	private String[] testFiles = new String[] { "img01.jpg", "img02.jpg", "img03.jpg", "img04.jpg", "img05.jpg", "img06.jpg", "img07.jpg", "img08.jpg", "img09.jpg", "img10.jpg" };
	private final String testFilesPath = "D:\\Somkiat\\เรียนโท\\ku\\is\\tarad\\variety\\";

	@Test
	public void testSingleFile() throws IOException {
		FCTH c = new FCTH();
		BufferedImage img = ImageIO.read(new File(testFilesPath + "img01.jpg"));
		c.extract(img);
		String s = Arrays.toString(c.histogram);
		System.out.println("s = " + s);
		byte[] b = c.getByteArrayRepresentation();
		FCTH d = new FCTH();
		d.setByteArrayRepresentation(b);
		System.out.println(d.getDistance(c));
	}

	@Test
	public void testMultiFile() {
		try {
			FCTH[] acc = new FCTH[testFiles.length];
			LinkedList<byte[]> vds = new LinkedList<byte[]>();
			for (int i = 0; i < acc.length; i++) {
				System.out.println("Extracting from number " + i);
				acc[i] = new FCTH();
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
				FCTH temp = new FCTH();
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
