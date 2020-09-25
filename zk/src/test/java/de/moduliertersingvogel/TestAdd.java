package de.moduliertersingvogel;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.xapian.WritableDatabase;
import org.xapian.Xapian;

import com.google.gson.Gson;

import de.moduliertersingvogel.zk.Entry;
import de.moduliertersingvogel.zk.ZK;
import de.moduliertersingvogel.zk.provider.DBProvider;
import picocli.CommandLine;

class TestAdd {

	@Test
	void testCall(@TempDir Path tempDir) {
		try (MockedStatic<DBProvider> dbprovider = mockStatic(DBProvider.class);) {
			WritableDatabase writableDatabase = new WritableDatabase(tempDir.resolve("test.xapian").toString(),
					Xapian.DB_CREATE_OR_OPEN);

			dbprovider.when(() -> DBProvider.getDatabase()).thenReturn(writableDatabase);

			ZK zk = new ZK();
			CommandLine cmd = new CommandLine(zk);
			final String jsonContent = new Gson().toJson(new Entry("Testtitle", "", new String[0]));
			cmd.execute("add", jsonContent);

			boolean documentWrittenToFile = Files.walk(tempDir.resolve("test.xapian")).filter(f -> f.toFile().isFile())
					.map(f -> {
						try (Scanner scanner = new Scanner(f);) {
							scanner.useDelimiter("\\Z");
							String content = scanner.next();
							return content.contains("Testtitle");
						} catch (Exception e) {
							System.err.println(e);
							return false;
						}
					}).anyMatch(p -> p);

			assertTrue(documentWrittenToFile);

			writableDatabase.flush();
			writableDatabase.close();
		} catch (IOException e) {
			fail(e);
		}
	}
}
