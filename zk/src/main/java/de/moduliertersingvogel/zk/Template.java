package de.moduliertersingvogel.zk;

import java.util.concurrent.Callable;

import com.google.gson.Gson;

import de.moduliertersingvogel.zk.model.Entry;
import picocli.CommandLine.Command;

@Command(name = "template", mixinStandardHelpOptions = true, description = "Show an entry template")
public class Template implements Callable<Integer> {
	@Override
	public Integer call() throws Exception {
		
		System.out.println(new Gson().toJson(new Entry("", "", new String[0])));
		
		return 0;
	}
}
