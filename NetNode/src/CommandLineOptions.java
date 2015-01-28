
import org.apache.commons.cli.*;

public class CommandLineOptions {
	
	/**
	 * return the command line options for use in command line parser
	 * 
	 * @return	the options
	 */
	public static Options getDefaultOptions() {
		// create the Options
		Options options = new Options();
		
		options.addOption("i", "id",	true, "node id");
		options.addOption("f", "file",	true, "input file for node network");
		options.addOption("g", "graph",	true, "graph file for node network layout");
		
		return options;
	}
	
}
