package de.moduliertersingvogel.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.xapian.PostingIterator;
import org.xapian.WritableDatabase;

import com.google.gson.Gson;

import de.moduliertersingvogel.zk.model.Entry;
import de.moduliertersingvogel.zk.provider.DBProvider;
import picocli.CommandLine.Command;

@Command(name = "list", mixinStandardHelpOptions = true, description = "Get all titles")
public class ListTitles implements Callable<Integer> {

	@Override
	public Integer call() throws Exception {
		
        WritableDatabase db = DBProvider.getDatabase();
        
        PostingIterator postListBegin = db.postListBegin("");
        List<String> titles = new ArrayList<>();
		while(postListBegin.hasNext()) {
			final long docID = postListBegin.next();
			Entry entry = new Gson().fromJson(db.getDocument(docID).getData(), Entry.class);
	        titles.add(entry.title);
		}
		
		titles.sort((a, b) -> a.compareTo(b));
		System.out.println(titles.stream().collect(Collectors.joining("\n")));
		
		return 0;
	}
}

