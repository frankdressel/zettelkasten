package de.moduliertersingvogel.zk;

public final class Entry {
	public final String title;
	public final String text;
	public final String[] links;

	public Entry(final String title, final String text, final String[] links) {
		this.title = title;
		this.text = text;
		this.links = links;
	}
}
