package org.demevgen.caffeine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Eugene Dementiev
 */
public class Config {

	private Path path = Paths.get("").toAbsolutePath();
	private boolean verbose = false;
	private long duration = -1;
	
	private final ZonedDateTime startTime = ZonedDateTime.now();
	
	private static final CommandLineParser PARSER = new DefaultParser();
	private static CommandLine CMD;
	private static final Options OPTIONS = new Options();
	
	enum ARGS {
		h, help,
		verbose,
		d, duration,
		p, path
	}
	
	static {
		OPTIONS.addOption(Option.builder(ARGS.h.toString()).longOpt(ARGS.help.toString()).desc("print this message").build());
		OPTIONS.addOption(Option.builder().longOpt(ARGS.verbose.toString()).desc("print working path and print temp file names").build());
		OPTIONS.addOption(Option.builder(ARGS.d.toString()).longOpt(ARGS.duration.toString()).hasArg().argName("minutes").desc("Number of minutes to exit after").build());
		OPTIONS.addOption(Option.builder(ARGS.p.toString()).longOpt(ARGS.path.toString()).hasArg().argName("path").desc("Path to keep awake (if path is a file, will use its parent directory instead)").build());
	}
	
	protected void processArgs(String[] args){
		try {
			CMD = PARSER.parse(OPTIONS, args);
		}
		catch (ParseException ex){
			System.err.println("Error reading arguments: " + ex.getMessage());
			System.exit(1);
		}
		
		if (CMD.hasOption(ARGS.h.toString())){
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Caffeine for HDD (caffeine4hdd)\nWill write and delete a temp file into a location to keep its HDD awake", OPTIONS);
			System.exit(0);
		}
		
		if (CMD.hasOption(ARGS.p.toString())){
			Path path = Paths.get(CMD.getOptionValue(ARGS.p.toString())).toAbsolutePath();
			
			if (!Files.isDirectory(path)){
				
				if (Files.isDirectory(path.getParent())){
					path = path.getParent();
				}
			}
			
			if (!Files.isDirectory(path) || !Files.isWritable(path)){
				System.err.println("Path provided is not a writable directory");
				System.exit(0);
			}
			
			setPath(path);
		}
		
		setVerbose(CMD.hasOption(ARGS.verbose.toString()));
		
		if (CMD.hasOption(ARGS.d.toString())){
			try {
				long duration = Long.parseLong(CMD.getOptionValue(ARGS.d.toString()));
				setDuration(duration);
			}
			catch (NumberFormatException ex){
				System.err.println("Error parsing duration: " + ex.getMessage());
				System.exit(1);
			}
		}
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long durationMin) {
		this.duration = durationMin;
	}

	public ZonedDateTime getStartTime() {
		return startTime;
	}
}
