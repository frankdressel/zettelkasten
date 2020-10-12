package de.moduliertersingvogel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

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

class TestGet {
	final PrintStream originalOut = System.out;
    final PrintStream originalErr = System.err;
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ByteArrayOutputStream err = new ByteArrayOutputStream();

	@Test
	void test(@TempDir Path tempDir) {
		try (MockedStatic<DBProvider> dbprovider = mockStatic(DBProvider.class);) {
			WritableDatabase database = new WritableDatabase(Paths.get(tempDir.toString(), "test.xapian").toString(),
					Xapian.DB_CREATE_OR_OPEN);
			dbprovider.when(() -> DBProvider.getDatabase()).thenReturn(database);

			ZK zk = new ZK();
			CommandLine cmd = new CommandLine(zk);
			Entry entry = new Entry("Test title", "Hello World", new String[] {"Huh"});
			final String jsonContent = new Gson().toJson(entry);
			cmd.execute("add", jsonContent);

			// Redirect output stream to capture output for comparison.
			out.reset();
	        err.reset();
	        System.setOut(new PrintStream(out));
	        System.setErr(new PrintStream(err));
	        
			cmd.execute("get", "Test title");
			
			// Each entry should be on its own line and thus a line break is expected.
			assertEquals(new Gson().toJson(entry) + "\n", out.toString());
			
			// Reset output stream.
			System.setOut(originalOut);
	        System.setErr(originalErr);
		}
	}
}
