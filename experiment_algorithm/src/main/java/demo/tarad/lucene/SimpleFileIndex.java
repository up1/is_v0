package demo.tarad.lucene;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.clustering.Cluster;
import net.semanticmetadata.lire.clustering.KMeans;
import net.semanticmetadata.lire.clustering.ParallelKMeans;
import net.semanticmetadata.lire.utils.ImageUtils;
import net.semanticmetadata.lire.utils.SerializationUtils;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import com.stromberglabs.jopensurf.SURFInterestPoint;
import com.stromberglabs.jopensurf.Surf;

import demo.tarad.MyPrototype;

public class SimpleFileIndex {

	public static final String LUCENE_INDEX_PATH = "D:\\Somkiat\\เรียนโท\\ku\\is\\lucene\\";
	public final static String BASE_PATH = "D:\\Somkiat\\เรียนโท\\ku\\is\\tarad\\";

	public static void main(String[] args) {
		MyPrototype prototype = new MyPrototype();

		SimpleFileIndex index = new SimpleFileIndex();
		try {
			index.indexer(LUCENE_INDEX_PATH, prototype.listFilesForFolder(new File(BASE_PATH)));
			index.clusterIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Cluster[] clusters;

	private void indexer(String indexDir, List<String> fileList) throws IOException {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
		Directory index = FSDirectory.open(new File(indexDir));
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41, analyzer);
		IndexWriter indexWriter = new IndexWriter(index, config);

		for (String fileName : fileList) {

			BufferedImage imageTarget = ImageIO.read(new File(fileName));
			Surf targetSurf = new Surf(imageTarget);

			if (Math.max(imageTarget.getHeight(), imageTarget.getWidth()) > 1024) {
				imageTarget = ImageUtils.scaleImage(imageTarget, 1024);
			}

			Document doc = new Document();
			List<SURFInterestPoint> interestPoints = targetSurf.getFreeOrientedInterestPoints();
			for (Iterator<SURFInterestPoint> points = interestPoints.iterator(); points.hasNext();) {
				SURFInterestPoint point = points.next();
				doc.add(new StoredField("image", SerializationUtils.toByteArray(point.getDescriptor())));
			}
			doc.add(new StringField("filename", fileName, Field.Store.YES));
			indexWriter.addDocument(doc);
		}
		indexWriter.commit();
		System.out.println(indexWriter.numDocs());
		indexWriter.close();
	}

	private void clusterIndex() throws Exception {
		Directory index = FSDirectory.open(new File(LUCENE_INDEX_PATH));
		IndexReader reader = IndexReader.open(index);
		KMeans k = new ParallelKMeans(1000);
		List<double[]> features = new LinkedList<double[]>();
		for (int i = 0; i < reader.numDocs(); i++) {
			Document doc = reader.document(i);
			features.clear();
			BytesRef[] binaryValues = doc.getBinaryValues("image");
			String file = doc.get("filename");
			for (BytesRef bytesRef : binaryValues) {
				features.add(SerializationUtils.toDoubleArray(bytesRef.bytes));
			}
			k.addImage(file, features);
		}

		k.init();
		clusters = k.getClusters();
		Cluster.writeClusters(clusters, "test1");

		WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_41);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41, analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter indexWriter = new IndexWriter(index, config);

		int[] tmpHist = new int[1000];
		for (int i = 0; i < reader.numDocs(); i++) {
			Document doc = reader.document(i);
			doc.removeField("image_surf");
			doc.removeField("filename_found");
			BytesRef[] binaryValues = doc.getBinaryValues("image");
			for (BytesRef bytesRef : binaryValues) {
				tmpHist[clusterForFeature(SerializationUtils.toDoubleArray(bytesRef.bytes))]++;
			}
			normalize(tmpHist);
			doc.add(new TextField("image_surf", arrayToVisualWordString(tmpHist), Field.Store.YES));
			doc.add(new StringField("filename_found", SerializationUtils.arrayToString(tmpHist), Field.Store.YES));

			indexWriter.updateDocument(new Term("filename", doc.getValues("filename")[0]), doc);

		}
		
		indexWriter.commit();
		indexWriter.close();
	}

	private String arrayToVisualWordString(int[] hist) {
		StringBuilder sb = new StringBuilder(1024);
		for (int i = 0; i < hist.length; i++) {
			int visualWordIndex = hist[i];
			for (int j = 0; j < visualWordIndex; j++) {
				sb.append('v');
				sb.append(i);
				sb.append(' ');
			}
		}
		return sb.toString();
	}

	private int clusterForFeature(double[] f) {
		double distance = clusters[0].getDistance(f);
		double tmp;
		int result = 0;
		for (int i = 1; i < clusters.length; i++) {
			tmp = clusters[i].getDistance(f);
			if (tmp < distance) {
				distance = tmp;
				result = i;
			}
		}
		return result;
	}

	private void normalize(int[] histogram) {
		int max = 0;
		for (int i = 0; i < histogram.length; i++) {
			max = Math.max(max, histogram[i]);
		}
		for (int i = 0; i < histogram.length; i++) {
			histogram[i] = (int) Math.floor(((double) histogram[i] * 15d) / (double) max);
		}
	}

}
