package de.moduliertersingvogel.zk;

import java.util.concurrent.Callable;

import org.xapian.Document;
import org.xapian.Stem;
import org.xapian.TermGenerator;
import org.xapian.WritableDatabase;

import com.google.gson.Gson;

import de.moduliertersingvogel.zk.provider.DBProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "add", mixinStandardHelpOptions = true, description = "Add things to zettelkasten")
public class Add implements Callable<Integer> {
	@Parameters(index = "0")
	String text;

	public Integer call() {
		final Entry entry = new Gson().fromJson(text, Entry.class);

		WritableDatabase db = DBProvider.getDatabase();

		Document document = new Document();
		document.setData(new Gson().toJson(entry));

		TermGenerator termGenerator = new TermGenerator();
		termGenerator.setStemmer(new Stem("en"));
		termGenerator.setDocument(document);
		termGenerator.indexText(entry.title, 1, "S");
		termGenerator.indexText(entry.title);
		termGenerator.indexText(entry.text);

		// ID is title
		String idterm = "Q" + entry.title;
		document.addBooleanTerm(idterm);

		db.replaceDocument(idterm, document);
		db.commit();
		db.flush();
		
		return 0;
	}
}
