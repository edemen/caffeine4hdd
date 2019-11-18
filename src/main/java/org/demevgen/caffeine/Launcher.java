package org.demevgen.caffeine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 *
 * @author Eugene Dementiev
 */
public class Launcher {
	
	private static final Config CONFIG = new Config();

	public static void main(String[] args) {
		
		CONFIG.processArgs(args);
		caffeinate();
	}
	
	private static void caffeinate(){
		
		if (CONFIG.isVerbose()){
			String message = "Will be keeping awake path: \"" + CONFIG.getPath() + "\"";
			if (CONFIG.getDuration() > 0){
				message += " for " + CONFIG.getDuration() + " minutes";
			}
			System.out.println(message);
		}
		
		Random random = new Random();
		byte[] bytes = new byte[100];
		do {
			try {
				File file = File.createTempFile("caf", null, CONFIG.getPath().toFile());
				
				random.nextBytes(bytes);
				
				Files.write(file.toPath(), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				
				file.delete();
				
				if (CONFIG.isVerbose()){
					System.out.println(ZonedDateTime.now() + "\t" + file.toPath());
				}
				
				Thread.sleep(1000*60);
				
				if (CONFIG.getDuration() > 0 && CONFIG.getStartTime().until(ZonedDateTime.now(), ChronoUnit.MINUTES) >= CONFIG.getDuration()){
					if (CONFIG.isVerbose()){
						System.out.println(CONFIG.getDuration() + " minutes have lapsed, stopping");
					}
					break;
				}
				
			}
			catch (IOException | InterruptedException ex){
				if (CONFIG.isVerbose()){
					ex.printStackTrace();
				}
				break;
			}
		}
		while(true);
	}
}
