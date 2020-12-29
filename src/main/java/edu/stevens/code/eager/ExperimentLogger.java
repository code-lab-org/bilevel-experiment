/******************************************************************************
 * Copyright 2020 Stevens Institute of Technology, Collective Design Lab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
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

import edu.stevens.code.eager.model.Designer;
import edu.stevens.code.eager.model.Manager;

/**
 * A basic logger to record log messages during an experiment.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class ExperimentLogger {
    private static final Logger logger = LogManager.getLogger(ExperimentLogger.class);
    
	private final Path logFile;
	private Gson gson = new Gson();
	
	/**
	 * Instantiates a new experiment logger.
	 */
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
	
	/**
	 * Writes a log message when an experimental round changes.
	 *
	 * @param manager the manager
	 */
	public void logRoundChange(Manager manager) {
		logAction(manager.toString(), "round", manager.getRoundName());
	}
	
	/**
	 * Writes a log message when an experimental task changes.
	 *
	 * @param manager the manager
	 */
	public void logTaskChange(Manager manager) {
		logAction(manager.toString(), "task", manager.getTasks());
	}
	
	/**
	 * Writes a log message when an experimental task starts.
	 *
	 * @param manager the manager
	 * @param values the values
	 */
	public void logTaskStart(Manager manager, int[] values) {
		logAction(manager.toString(), "start", values);
	}
	
	/**
	 * Writes a log message when an experimental task ends.
	 *
	 * @param manager the manager
	 * @param values the values
	 */
	public void logTaskEnd(Manager manager, int[] values) {
		logAction(manager.toString(), "end", values);
	}
	
	/**
	 * Writes a log message when a designer changes a design decision.
	 *
	 * @param designer the designer
	 */
	public void logDesignerDesignChange(Designer designer) {
		logAction(designer.toString(), "design", designer.getDesigns());
	}
	
	/**
	 * Writes a log message when a designer changes a strategy decision.
	 *
	 * @param designer the designer
	 */
	public void logDesignerStrategyChange(Designer designer) {
		logAction(designer.toString(), "strategy", designer.getStrategy());
	}
	
	/**
	 * Helper function to write a generic action.
	 *
	 * @param actor the actor
	 * @param action the action
	 * @param data the data
	 */
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
