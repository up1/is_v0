package com.somkiat.is.svm;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import junit.framework.Assert;
import net.semanticmetadata.lire.imageanalysis.CEDD;

import org.junit.Test;

public class TestExtractImageToSVMFormat {

	private final String testFilesPath = "D:\\Somkiat\\เรียนโท\\ku\\is\\tarad\\bag\\3\\";

	public void extractSurfFeatureThenReturnStringOfFeature() throws Exception {
		ExtractImage extractImage = new ExtractImage();
		String result = extractImage.process("D:\\Somkiat\\เรียนโท\\ku\\is\\tarad\\bag\\r1.jpg");
		Assert.assertNotNull(result);
	}

	@Test
	public void extractCEDDFeatureThenReturnSVMOfFeature() {
		try {
			List<String> fileNames = listFilesForFolder(new File(testFilesPath));
			CEDD[] acc = new CEDD[fileNames.size()];
			for (String fileName : fileNames) {
				CEDD cedd = new CEDD();
				cedd.extract(ImageIO.read(new FileInputStream(fileName)));
				byte[] data = cedd.getByteArrayRepresentation();
				System.out.print("3");
				for (int i = 0; i < data.length; i++) {
					System.out.print(" " + (i+1) + ":" + data[i]);
				}
				System.out.println();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private List<String> listFilesForFolder(final File folder) {
		List<String> fileNames = new ArrayList<String>();
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
}
