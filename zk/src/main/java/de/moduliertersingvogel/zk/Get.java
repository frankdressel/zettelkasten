package de.moduliertersingvogel.zk;

import java.util.Arrays;
import java.util.concurrent.Callable;

import org.xapian.PostingIterator;
import org.xapian.WritableDatabase;
import org.xapian.Xapian;

import com.google.gson.Gson;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get", mixinStandardHelpOptions = true, description = "Get a document for a given docID")
public class Get implements Callable<Integer> {

	@Parameters(index = "0")
	String title;

	@Override
	public Integer call() throws Exception {
		
		String dbpath = "zk.xapian";
        WritableDatabase db = new WritableDatabase(dbpath, Xapian.DB_OPEN);
        
        PostingIterator postListBegin = db.postListBegin("Q" + title);
		while(postListBegin.hasNext()) {
			final long docID = postListBegin.next();
			Entry entry = new Gson().fromJson(db.getDocument(docID).getData(), Entry.class);
	        System.out.println(new Gson().toJson(entry));
		}
		
		return 0;
	}
}

