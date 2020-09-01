package de.moduliertersingvogel.zk;

import java.util.Arrays;
import java.util.concurrent.Callable;

import org.xapian.WritableDatabase;
import org.xapian.Xapian;

import com.google.gson.Gson;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get", mixinStandardHelpOptions = true, description = "Get a document for a given docID")
public class Get implements Callable<Integer> {

	@Parameters(index = "0")
	long docID;

	@Override
	public Integer call() throws Exception {
		
		String dbpath = "zk.xapian";
        WritableDatabase db = new WritableDatabase(dbpath, Xapian.DB_OPEN);
        
        Entry entry = new Gson().fromJson(db.getDocument(docID).getData(), Entry.class);
        System.out.println(entry.title);
        System.out.println();
        System.out.println(entry.text);
        System.out.println();
        System.out.println(Arrays.deepToString(entry.links));
		
		return 0;
	}
}

