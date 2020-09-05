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

	public Integer call() throws Exception {
		Entry entry = new Entry(title, text, new String[0]);

		String dbpath = "zk.xapian";
		WritableDatabase db = new WritableDatabase(dbpath, Xapian.DB_CREATE_OR_OPEN);

		Document document = new Document();
		document.setData(new Gson().toJson(entry));

		TermGenerator termGenerator = new TermGenerator();
		termGenerator.setStemmer(new Stem("en"));
		termGenerator.setDocument(document);
		termGenerator.indexText(entry.title, 1, "S");
		termGenerator.indexText(entry.title);
		termGenerator.indexText(entry.text);

		// ID is title
		String idterm = "Q" + title;
		document.addBooleanTerm(idterm);

		db.replaceDocument(idterm, document);
		db.commit();
		
		return 0;
	}
}
