package de.moduliertersingvogel.zk;

public final class Entry {
	public final String title;
	public final String text;
	public final String[] tags;

	public Entry(final String title, final String text, final String[] tags) {
		this.title = title;
		this.text = text;
		this.tags = tags;
	}
}
