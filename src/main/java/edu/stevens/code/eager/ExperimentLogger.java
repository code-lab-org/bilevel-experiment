package edu.stevens.code.eager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class ExperimentLogger {
    private static final Logger logger = LogManager.getLogger(ExperimentLogger.class);
    
	private final Path logFile;
	private Gson gson = new Gson();
	
	public ExperimentLogger() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		logFile = Paths.get("logs/log-"+format.format(new Date())+".log");
		try {
			Files.createDirectories(Paths.get("logs"));
			Files.createFile(logFile);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public void logRoundChange(Manager manager) {
		logAction(manager.toString(), "round", manager.getRoundName());
	}
	
	public void logTaskChange(Manager manager) {
		logAction(manager.toString(), "task", manager.getTasks());
	}
	
	public void logTaskStart(Manager manager, int[] values) {
		logAction(manager.toString(), "start", values);
	}
	
	public void logTaskEnd(Manager manager, int[] values) {
		logAction(manager.toString(), "end", values);
	}
	
	public void logDesignerDesignChange(Designer designer) {
		logAction(designer.toString(), "design", designer.getDesigns());
	}
	
	public void logDesignerStrategyChange(Designer designer) {
		logAction(designer.toString(), "strategy", designer.getStrategy());
	}
	
	private void logAction(String actor, String action, Object data) {
		try {
			Files.write(logFile, Arrays.asList(
					new Date().getTime() + "\t" + 
					actor + "\t" + 
					action + "\t" + 
					gson.toJson(data)), 
				StandardOpenOption.APPEND);
		} catch (IOException e) {
			logger.error(e);
		}
	}
}
