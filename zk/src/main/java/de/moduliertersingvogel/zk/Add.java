package de.moduliertersingvogel.zk;

import java.util.concurrent.Callable;

import org.xapian.Document;
import org.xapian.Stem;
import org.xapian.TermGenerator;
import org.xapian.WritableDatabase;
import org.xapian.Xapian;

import com.google.gson.Gson;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "add", mixinStandardHelpOptions = true, description = "Add things to zettelkasten")
public class Add implements Callable<Integer> {
	@Parameters(index = "0")
	String text;
	@Option(names = "title", description = "Title for the document", required = true)
	String title;
	@Option(names = "tags", description = "Tags for labeling the document", required = true)
	String[] tags;

	public Integer call() throws Exception {
		Entry entry = new Entry(title, text, tags, new String[0]);

		String dbpath = "zk.xapian";
		WritableDatabase db = new WritableDatabase(dbpath, Xapian.DB_CREATE_OR_OPEN);

		Document document = new Document();
		document.setData(new Gson().toJson(entry));

		TermGenerator termGenerator = new TermGenerator();
		termGenerator.setStemmer(new Stem("en"));
		termGenerator.setDocument(document);
		termGenerator.indexText(entry.title, 1, "S");
		termGenerator.indexText(entry.text);
		for (int i = 0; i < tags.length; i++) {
			String tag = tags[i];
			termGenerator.indexText(tag, i + 1, "XT");
		}
		// ID is title
		document.addBooleanTerm("Q" + title);

		db.addDocument(document);
		db.commit();
		
		System.out.println(document.getDocId());

		return 0;
	}
}
