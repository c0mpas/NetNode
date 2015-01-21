import org.apache.commons.cli.*;
import util.CommandLineOptions;

public class Node {
	
	private int id;
	private String file;

	public static void main(String[] args) {
		// create the command line parser
		CommandLineParser parser = new BasicParser();
		// get command line options
		Options options = CommandLineOptions.getDefaultOptions();
		
		int id = 0;
		String file = "";
		
		try {
		    // parse the command line arguments
		    CommandLine line = parser.parse( options, args );
		    // check id
		    if (line.hasOption("id")) {
		        id = Integer.parseInt(line.getOptionValue("id"));
		    } else {
		    	throw new RuntimeException("Missing ID");
		    }
		    // check input file
		    if (line.hasOption("file")) {
		        file = line.getOptionValue("file");
		    } else {
		    	throw new RuntimeException("Input File");
		    }
		} catch (ParseException e) {
		    System.out.println("ERROR" + e.getMessage());
		    e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}
	
	private Node() {}
	
	public Node(int id, String file) {
		if (id < 1) throw new RuntimeException("ID invalid (must be > 0)");
		if (file == null || file.isEmpty()) throw new RuntimeException("Filename invalid");
		
		this.id = id;
		this.file = file;
	}

}
