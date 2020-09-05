package de.moduliertersingvogel.zk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.xapian.Document;
import org.xapian.PostingIterator;
import org.xapian.WritableDatabase;
import org.xapian.Xapian;

import com.google.gson.Gson;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "link", mixinStandardHelpOptions = true, description = "links two entries based on title")
public class Link implements Callable<Integer> {

	@Parameters(index = "0")
	String title1;
	@Parameters(index = "1")
	String title2;

	public Integer call() throws Exception {
		String dbpath = "zk.xapian";
		WritableDatabase db = new WritableDatabase(dbpath, Xapian.DB_OPEN);
		Gson gson = new Gson();
		
		PostingIterator postListBegin1 = db.postListBegin("Q" + title1);
		while(postListBegin1.hasNext()) {
			long docID1 = postListBegin1.next();
			PostingIterator postListBegin2 = db.postListBegin("Q" + title2);
			while(postListBegin2.hasNext()) {
				long docID2 = postListBegin2.next();
				
				Document doc1 = db.getDocument(docID1);
				Document doc2 = db.getDocument(docID2);
				
				Entry entry1 = gson.fromJson(doc1.getData(), Entry.class);
				Entry entry2 = gson.fromJson(doc2.getData(), Entry.class);
				
				List<String> tmp1 = new ArrayList<String>(Arrays.asList(entry1.links));
				tmp1.add(title2);
				
				List<String> tmp2 = new ArrayList<String>(Arrays.asList(entry2.links));
				tmp2.add(title1);
				
				doc1.setData(gson.toJson(new Entry(entry1.title, entry1.text, tmp1.toArray(new String[0]))));
				doc2.setData(gson.toJson(new Entry(entry2.title, entry2.text, tmp2.toArray(new String[0]))));
								
				db.replaceDocument(docID1, doc1);
				db.replaceDocument(docID2, doc2);
				
				db.commit();
			}
		}

		return 0;
	}
}
