package demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.stromberglabs.jopensurf.SURFInterestPoint;
import com.stromberglabs.jopensurf.Surf;

public class PlotSurfImage {

	private static void createAndShowGUI() {

		// Create and set up the window.
		JFrame frame = new JFrame("Show Image with Speeded-Up Robust Features (SURF)");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ImagePanel imagePanel = new ImagePanel();
		imagePanel.findPoint();
		frame.setContentPane(imagePanel);

		// Display the window.
		frame.setPreferredSize(new Dimension(500, 500));
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}

@SuppressWarnings("serial")
class ImagePanel extends JPanel {
	private BufferedImage image;
	private final ArrayList<Line> lines = new ArrayList<Line>();

	String testImage = "D:\\Somkiat\\เรียนโท\\ku\\is\\tarad\\bag\\spd_20130612181703_b.jpg";

	public ImagePanel() {
		try {
			image = ImageIO.read(new File(testImage));
		    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);  
		    ColorConvertOp op = new ColorConvertOp(cs, null);  
		    op.filter(image, image);  
		} catch (IOException ex) {
			// handle exception...
		}
	}

	public void findPoint() {
		try {
			Surf sm = new Surf(ImageIO.read(new FileInputStream(testImage)));
			List<SURFInterestPoint> pts = sm.getFreeOrientedInterestPoints();

			List<Point2> points = new ArrayList<Point2>();

			for (Iterator<SURFInterestPoint> surfInterestPointIterator = pts.iterator(); surfInterestPointIterator.hasNext();) {
				SURFInterestPoint pt = surfInterestPointIterator.next();
				System.out.println(pt.getX() + ", " + pt.getY());
				points.add(new Point2((int) pt.getX(), (int) pt.getY()));
			}

			for (int i = 1; i < points.size(); i++) {
				lines.add(new Line(points.get(i - 1), points.get(i)));
				if (i + 1 == points.size()) {
					lines.add(new Line(points.get(i), points.get(0)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, this);
		for (Line r : lines) {
			g.setColor(Color.red);
			r.paint(g);
		}
	}

}

class Point2 {
	int x;
	int y;

	public Point2(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

class Line {
	private Point2 startPoint;
	private Point2 finishPoint;

	public Line(Point2 startPoint, Point2 finishPoint) {
		this.startPoint = startPoint;
		this.finishPoint = finishPoint;
	}

	public void paint(Graphics g) {
//		g.drawLine(startPoint.x, startPoint.y, finishPoint.x, finishPoint.y);
		g.drawRect(startPoint.x, startPoint.y, 5, 5);
		g.drawRect(finishPoint.x, finishPoint.y, 5, 5);
	}
}
