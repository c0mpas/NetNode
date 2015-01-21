package util;
import org.apache.commons.cli.*;

public class CommandLineOptions {
	public static Options getDefaultOptions() {
		// create the Options
		Options options = new Options();
		
		options.addOption("i","id",true,"node id" );
		options.addOption("f","file",true,"input file for node network" );
		
		return options;
	}
}
