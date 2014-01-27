package com.somkiat.is.svm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.imageanalysis.SurfFeature;
import net.semanticmetadata.lire.utils.ImageUtils;

import com.stromberglabs.jopensurf.SURFInterestPoint;
import com.stromberglabs.jopensurf.Surf;

public class ExtractImage {

	public String process(String fileName) {
		BufferedImage imageTarget;
		Surf targetSurf = null;
		try {
			imageTarget = ImageIO.read(new File(fileName));
			targetSurf = new Surf(imageTarget);

			if (Math.max(imageTarget.getHeight(), imageTarget.getWidth()) > 1024) {
				imageTarget = ImageUtils.scaleImage(imageTarget, 1024);
			}

			List<SURFInterestPoint> interestPoints = targetSurf.getFreeOrientedInterestPoints();
			int count = 1;
			for (SURFInterestPoint surfInterestPoint : interestPoints) {
				SurfFeature feature = new SurfFeature(surfInterestPoint);
				double[] d = feature.getDoubleHistogram();
				count +=d.length;
			}
			System.out.println("Size : " + count);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return targetSurf.getStringRepresentation(true);
	}
}
