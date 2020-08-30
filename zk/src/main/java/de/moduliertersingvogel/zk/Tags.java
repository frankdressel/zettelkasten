package de.moduliertersingvogel.zk;

import java.util.concurrent.Callable;

import org.xapian.QueryParser;
import org.xapian.Stem;
import org.xapian.TermIterator;
import org.xapian.WritableDatabase;
import org.xapian.Xapian;

import picocli.CommandLine.Command;

@Command(name = "tags", mixinStandardHelpOptions = true, description = "Get current tags")
public class Tags  implements Callable<Integer>{
	public Integer call() throws Exception {
		String dbpath = "zk.xapian";
        WritableDatabase db = new WritableDatabase(dbpath, Xapian.DB_OPEN);
        
        QueryParser queryParser = new QueryParser();
        queryParser.setStemmer(new Stem("en"));
        
        String prefix = "XT";
		TermIterator termIterator = db.allTermsBegin(prefix);
        while(termIterator.hasNext()) {
        	String term = termIterator.next().replaceFirst(prefix, "");
        	System.out.println(term);
        }
		
		return 0;
	}
}
