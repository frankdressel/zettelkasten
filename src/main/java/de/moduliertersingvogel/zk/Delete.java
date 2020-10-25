package de.moduliertersingvogel.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.xapian.PostingIterator;
import org.xapian.WritableDatabase;

import de.moduliertersingvogel.zk.provider.DBProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", mixinStandardHelpOptions = true, description = "Delete a document for a given docID")
public class Delete implements Callable<Integer> {

	@Parameters(index = "0")
	String title;

	@Override
	public Integer call() throws Exception {
		
        WritableDatabase db = DBProvider.getDatabase();
        
        List<Long> docids = new ArrayList<>();
        PostingIterator postListBegin = db.postListBegin("Q" + title);
		while(postListBegin.hasNext()) {
			final long docID = postListBegin.next();
			docids.add(Long.valueOf(docID));
		}
		for (Long long1 : docids) {
			db.deleteDocument(long1);
		}
		
		db.commit();
		db.flush();
		
		return 0;
	}
}

