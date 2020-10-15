package de.moduliertersingvogel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.xapian.WritableDatabase;
import org.xapian.Xapian;

import com.google.gson.Gson;

import de.moduliertersingvogel.zk.ZK;
import de.moduliertersingvogel.zk.model.Entry;
import de.moduliertersingvogel.zk.provider.DBProvider;
import picocli.CommandLine;

class TestList {

	final PrintStream originalOut = System.out;
    final PrintStream originalErr = System.err;
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ByteArrayOutputStream err = new ByteArrayOutputStream();
    
    @Test
	void test(@TempDir Path tempDir) {
		try (MockedStatic<DBProvider> dbprovider = mockStatic(DBProvider.class);) {
			WritableDatabase writableDatabase = new WritableDatabase(tempDir.resolve("test.xapian").toString(),
					Xapian.DB_CREATE_OR_OPEN);

			dbprovider.when(() -> DBProvider.getDatabase()).thenReturn(writableDatabase);
			ZK zk = new ZK();
			CommandLine cmd = new CommandLine(zk);
			final String jsonContent1 = new Gson().toJson(new Entry("Testtitle", "", new String[0]));
			final String jsonContent2 = new Gson().toJson(new Entry("Hallo Welt", "", new String[0]));
			final String jsonContent3 = new Gson().toJson(new Entry("kakao", "", new String[0]));
			cmd.execute("add", jsonContent1);
			cmd.execute("add", jsonContent2);
			cmd.execute("add", jsonContent3);
			
			out.reset();
	        err.reset();
	        System.setOut(new PrintStream(out));
	        System.setErr(new PrintStream(err));
	        
			cmd.execute("list");
			
			// Each entry should be on its own line and thus a line break is expected.
			assertEquals("Hallo Welt\nkakao\nTesttitle\n", out.toString());
			
			// Reset output stream.
			System.setOut(originalOut);
	        System.setErr(originalErr);
		}
	}
}
