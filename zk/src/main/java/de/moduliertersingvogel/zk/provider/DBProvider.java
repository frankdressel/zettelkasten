package de.moduliertersingvogel.zk.provider;

import org.xapian.WritableDatabase;
import org.xapian.Xapian;

public class DBProvider {
	public static final WritableDatabase getDatabase() {
		String dbpath = "zk.xapian";
		return new WritableDatabase(dbpath, Xapian.DB_CREATE_OR_OPEN);
	}
}
