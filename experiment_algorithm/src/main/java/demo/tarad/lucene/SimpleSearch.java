package demo.tarad.lucene;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.clustering.Cluster;
import net.semanticmetadata.lire.utils.ImageUtils;
import net.semanticmetadata.lire.utils.SerializationUtils;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import com.stromberglabs.jopensurf.SURFInterestPoint;
import com.stromberglabs.jopensurf.Surf;

import demo.tarad.MyPrototype;

public class SimpleSearch {

	Cluster[] clusters;

	public static void main(String[] args) {
		BufferedImage imageTarget;
		Surf targetSurf;
		try {
			SimpleSearch p = new SimpleSearch();
			p.search(MyPrototype.BASE_PATH + "target.JPG");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void search(String fileName) throws Exception {
		int hitsPerPage = 10;
		Directory index = FSDirectory.open(new File(SimpleFileIndex.LUCENE_INDEX_PATH));
		IndexReader reader = IndexReader.open(index);
		int numDocs = reader.numDocs();
		System.out.println("numDocs = " + numDocs);

		IndexSearcher isearcher = new IndexSearcher(reader);
		isearcher.setSimilarity(new BM25Similarity());
		
		Document targetDocument = createDocument(fileName);

		clusters = Cluster.readClusters("test1");
		System.out.println( "Cluster Size : " + clusters.length );
		int[] tmpHist = new int[clusters.length];
		BytesRef[] binaryValues = targetDocument.getBinaryValues("image");
		for (BytesRef bytesRef : binaryValues) {
			tmpHist[clusterForFeature(SerializationUtils.toDoubleArray(bytesRef.bytes))]++;
		}
		normalize(tmpHist);
		targetDocument.add(new TextField("image_surf", arrayToVisualWordString(tmpHist), Field.Store.YES));
		targetDocument.add(new StringField("filename_found", SerializationUtils.arrayToString(tmpHist), Field.Store.YES));
		targetDocument.removeFields("image");

		String queryString = targetDocument.getValues("image_surf")[0];
		QueryParser qp = new QueryParser(Version.LUCENE_41, "image_surf", new WhitespaceAnalyzer(Version.LUCENE_41));
		BooleanQuery.setMaxClauseCount(10000);
		Query query = qp.parse(queryString);
		TopDocs docs = isearcher.search(query, hitsPerPage);
		System.out.println(docs.scoreDocs.length);
		for (int i = 0; i < docs.scoreDocs.length; i++) {
			float distance = 1f / docs.scoreDocs[i].score;
			Document tmpDoc = reader.document(docs.scoreDocs[i].doc);
			System.out.print(tmpDoc.get("filename"));
			System.out.print(":score=" + docs.scoreDocs[i].score);
			System.out.println(":=" + distance);
		}

	}

	private Document createDocument(String fileName) throws Exception {
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
		return doc;
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

	private float[] convertStringRepresentation(String input) {
		float[] result = null;
		LinkedList<Float> tmp = new LinkedList<Float>();
		StringTokenizer st = new StringTokenizer(input, ",");
		st.nextToken(); // point.x
		st.nextToken(); // point.y
		st.nextToken(); // response
		while (st.hasMoreTokens()) {
			tmp.add(Float.parseFloat(st.nextToken()));
		}
		result = new float[tmp.size()];
		int i = 0;
		for (Iterator<Float> iterator = tmp.iterator(); iterator.hasNext();) {
			Float next = iterator.next();
			result[i] = next;
			i++;
		}
		return result;
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
