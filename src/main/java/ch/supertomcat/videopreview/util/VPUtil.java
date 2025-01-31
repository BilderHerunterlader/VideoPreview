package ch.supertomcat.videopreview.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * This class provides methods, which are often used.
 */
public class VPUtil {
	/**
	 * Changes the root logger level
	 * 
	 * @param level Level
	 */
	public static void changeLog4JRootLoggerLevel(Level level) {
		LoggerContext loggerContext = (LoggerContext)LogManager.getContext(false);
		Configuration config = loggerContext.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
		loggerConfig.setLevel(level);
		loggerContext.updateLoggers(config);
	}
}
