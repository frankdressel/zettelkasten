package de.moduliertersingvogel.zk;

import java.util.concurrent.Callable;

import org.xapian.Enquire;
import org.xapian.MSet;
import org.xapian.MSetIterator;
import org.xapian.Query;
import org.xapian.QueryParser;
import org.xapian.Stem;
import org.xapian.WritableDatabase;
import org.xapian.Xapian;

import com.google.gson.Gson;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "search", mixinStandardHelpOptions = true, description = "Search the corresponding term")
public class Search implements Callable<Integer> {

	@Parameters(index = "0")
	String searchterm;

	@Override
	public Integer call() throws Exception {
		
		String dbpath = "zk.xapian";
        WritableDatabase db = new WritableDatabase(dbpath, Xapian.DB_OPEN);
        
        QueryParser queryParser = new QueryParser();
        queryParser.setStemmer(new Stem("en"));
        
        Query query = queryParser.parseQuery(searchterm);
        
        Enquire enquire = new Enquire(db);
        enquire.setQuery(query);
        
        long returnsetSize = 10;
        long offset = 0;
        MSet mSet = enquire.getMSet(offset, returnsetSize);
        while(!mSet.empty()) {
        	MSetIterator mSetIterator = mSet.begin();
        	while(mSetIterator.hasNext()) {
        		long docID = mSetIterator.next();
        		
        		Entry entry = new Gson().fromJson(db.getDocument(docID).getData(), Entry.class);
        		System.out.println(String.format("%s: %s", docID, entry.title));
        	}
        	offset = offset + returnsetSize;
        	mSet = enquire.getMSet(offset, returnsetSize);
        }
		
		return 0;
	}
}
