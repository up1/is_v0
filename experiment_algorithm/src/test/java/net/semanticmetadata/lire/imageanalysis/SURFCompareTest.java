package net.semanticmetadata.lire.imageanalysis;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.stromberglabs.jopensurf.SurfCompare;

public class SURFCompareTest {

	public static void main(String[] args) {

		String testImage = "D:\\Somkiat\\เรียนโท\\ku\\is\\tarad\\bag\\";

		try {

			BufferedImage imageA = ImageIO.read(new File(testImage + "spd_20130422104338_b.jpg"));
			BufferedImage imageB = ImageIO.read(new File(testImage + "r1.jpg"));

			SurfCompare show = new SurfCompare(imageA, imageB);
			show.display();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
