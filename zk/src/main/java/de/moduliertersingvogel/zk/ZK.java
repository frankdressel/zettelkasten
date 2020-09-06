package de.moduliertersingvogel.zk;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "zk", mixinStandardHelpOptions = true, description = "Maincommand for zettelkasten", subcommands = {
		Search.class, Add.class, Get.class, Template.class })
public class ZK {
	public static void main(String... args) {
		int exitCode = new CommandLine(new ZK()).execute(args);
		System.exit(exitCode);
	}
}
