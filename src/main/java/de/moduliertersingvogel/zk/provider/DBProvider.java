package de.moduliertersingvogel.zk.provider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import org.xapian.WritableDatabase;
import org.xapian.Xapian;

public class DBProvider {
	public static String dbpath() {
		Properties props = new Properties();
		try (BufferedReader reader = new BufferedReader(new FileReader("zk.conf"))) {
			props.load(reader);
		} catch (IOException e) {
			System.out.println("Using database file in current directory");
		}
		String dbpath = Paths.get(props.getProperty("base", ""), "zk.xapian").toString();
		return dbpath;
	}

	public static final WritableDatabase getDatabase() {
		String dbpath = dbpath();

		return new WritableDatabase(dbpath, Xapian.DB_CREATE_OR_OPEN);
	}
}
