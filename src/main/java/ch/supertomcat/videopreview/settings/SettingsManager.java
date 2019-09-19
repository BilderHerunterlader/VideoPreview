package ch.supertomcat.videopreview.settings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.Level;
import org.xml.sax.SAXException;

import ch.supertomcat.supertomcatutils.settings.SettingsManagerBase;
import ch.supertomcat.videopreview.settingsconfig.DirectorySettings;
import ch.supertomcat.videopreview.settingsconfig.GUISettings;
import ch.supertomcat.videopreview.settingsconfig.LogLevelSetting;
import ch.supertomcat.videopreview.settingsconfig.LookAndFeelSetting;
import ch.supertomcat.videopreview.settingsconfig.ObjectFactory;
import ch.supertomcat.videopreview.settingsconfig.Settings;
import ch.supertomcat.videopreview.util.VPUtil;

/**
 * Class which handels the settings
 */
public class SettingsManager extends SettingsManagerBase<Settings, VPSettingsListener> {
	/**
	 * Resource Path to the default settings file
	 */
	private static final String DEFAULT_SETTINGS_FILE_RESOURCE_PATH = "/ch/supertomcat/videopreview/settingsconfig/default-settings.xml";

	/**
	 * Resource Path to settings schema file
	 */
	private static final String SETTINGS_SCHEMA_FILE_RESOURCE_PATH = "/ch/supertomcat/videopreview/settingsconfig/settings.xsd";

	/**
	 * LookAndFeel ClassNames
	 */
	protected static final Map<LookAndFeelSetting, String> LOOK_AND_FEEL_CLASS_NAMES = new LinkedHashMap<>();

	/**
	 * LookAndFeel Names
	 */
	protected static final Map<LookAndFeelSetting, String> LOOK_AND_FEEL_NAMES = new LinkedHashMap<>();

	/**
	 * Log Level Mapping
	 */
	protected static final Map<LogLevelSetting, Level> LOG_LEVEL_MAPPING = new LinkedHashMap<>();

	static {
		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_DEFAULT, UIManager.getCrossPlatformLookAndFeelClassName());
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_DEFAULT, "Default");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_OS, UIManager.getSystemLookAndFeelClassName());
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_OS, "OperatingSystem");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_METAL, "javax.swing.plaf.metal.MetalLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_METAL, "Metal");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_WINDOWS, "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_WINDOWS, "Windows");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_WINDOWS_CLASSIC, "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_WINDOWS_CLASSIC, "Windows Classic");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_MOTIF, "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_MOTIF, "Motif");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_GTK, "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_GTK, "GTK");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_MACOS, "javax.swing.plaf.mac.MacLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_MACOS, "Mac OS");

		LOOK_AND_FEEL_CLASS_NAMES.put(LookAndFeelSetting.LAF_NIMBUS, "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		LOOK_AND_FEEL_NAMES.put(LookAndFeelSetting.LAF_NIMBUS, "Nimbus");

		LOG_LEVEL_MAPPING.put(LogLevelSetting.TRACE, Level.TRACE);
		LOG_LEVEL_MAPPING.put(LogLevelSetting.DEBUG, Level.DEBUG);
		LOG_LEVEL_MAPPING.put(LogLevelSetting.INFO, Level.INFO);
		LOG_LEVEL_MAPPING.put(LogLevelSetting.WARN, Level.WARN);
		LOG_LEVEL_MAPPING.put(LogLevelSetting.ERROR, Level.ERROR);
		LOG_LEVEL_MAPPING.put(LogLevelSetting.FATAL, Level.FATAL);
	}

	/**
	 * Dummy Constructor. ONLY USE FOR UNIT TESTS.
	 * 
	 * @throws JAXBException
	 */
	protected SettingsManager() throws JAXBException {
		super(ObjectFactory.class, DEFAULT_SETTINGS_FILE_RESOURCE_PATH, SETTINGS_SCHEMA_FILE_RESOURCE_PATH);
	}

	/**
	 * Constructor
	 * 
	 * @param strSettingsFolder Settings Folder
	 * @param strSettingsFilename Settings Filename
	 * @throws JAXBException
	 */
	public SettingsManager(String strSettingsFolder, final String strSettingsFilename) throws JAXBException {
		super(strSettingsFolder, strSettingsFilename, ObjectFactory.class, DEFAULT_SETTINGS_FILE_RESOURCE_PATH, SETTINGS_SCHEMA_FILE_RESOURCE_PATH);
	}

	/**
	 * Read the Settings
	 * 
	 * @return True if successful, false otherwise
	 */
	public synchronized boolean readSettings() {
		if (settingsFile.exists()) {
			logger.info("Loading Settings File: {}", settingsFile.getAbsolutePath());
			try {
				this.settings = loadUserSettingsFile();
				applyLogLevel();
				settingsChanged();
				return true;
			} catch (Exception e) {
				logger.error("Could not read settings file: {}", settingsFile.getAbsolutePath(), e);
				return false;
			}
		} else {
			logger.info("Loading Default Settings File");
			try {
				this.settings = loadDefaultSettingsFile();
				applyLogLevel();
				settingsChanged();
				return true;
			} catch (Exception e) {
				logger.error("Could not read default settings file", e);
				return false;
			}
		}
	}

	/**
	 * Save the Settings
	 * 
	 * @param noShutdown When the application is not shutdowned
	 * @return True if successful, false otherwise
	 */
	public synchronized boolean writeSettings(boolean noShutdown) {
		try (FileOutputStream out = new FileOutputStream(settingsFile)) {
			writeSettingsFile(this.settings, out, false);
			settingsChanged();
			if (noShutdown) {
				backupSettingsFile();
			}
			return true;
		} catch (IOException | SAXException | JAXBException e) {
			logger.error("Could not write settings file: {}", settingsFile.getAbsolutePath(), e);
			return false;
		}
	}

	/**
	 * Apply the log level
	 */
	private void applyLogLevel() {
		Level log4jLevel = LOG_LEVEL_MAPPING.get(settings.getLogLevel());
		if (log4jLevel == null) {
			logger.error("Unsupported log level: {}", settings.getLogLevel());
			return;
		}
		VPUtil.changeLog4JRootLoggerLevel(log4jLevel);
	}

	/**
	 * Sets the log level
	 * 
	 * @param logLevel Log Level
	 */
	public void setLogLevel(LogLevelSetting logLevel) {
		if (logLevel == null) {
			throw new IllegalArgumentException("logLevel is null");
		}

		Level log4jLevel = LOG_LEVEL_MAPPING.get(logLevel);
		if (log4jLevel == null) {
			logger.error("Unsupported log level: {}", logLevel);
			return;
		}
		settings.setLogLevel(logLevel);
		VPUtil.changeLog4JRootLoggerLevel(log4jLevel);
	}

	/**
	 * @param lookAndFeel Look and Feel
	 */
	public void setLookAndFeel(LookAndFeelSetting lookAndFeel) {
		LookAndFeelSetting previousLAF = settings.getGuiSettings().getLookAndFeel();
		settings.getGuiSettings().setLookAndFeel(lookAndFeel);
		if (lookAndFeel != previousLAF) {
			String lafClassName = LOOK_AND_FEEL_CLASS_NAMES.get(lookAndFeel);
			if (lafClassName == null) {
				logger.error("LookAndFeelSetting missing in LOOK_AND_FEEL_CLASS_NAMES map!");
				return;
			}
			try {
				UIManager.setLookAndFeel(lafClassName);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
				logger.error("Could not set LookAndFeel", e);
				return;
			}

			for (VPSettingsListener listener : listeners) {
				listener.lookAndFeelChanged();
			}
		}
	}

	/**
	 * Returns the settings
	 * 
	 * @return settings
	 */
	public DirectorySettings getDirectorySettings() {
		return settings.getDirectorySettings();
	}

	/**
	 * Returns the settings
	 * 
	 * @return settings
	 */
	public GUISettings getGUISettings() {
		return settings.getGuiSettings();
	}

	/**
	 * Gets the class name for the given LookAndFeel Setting
	 * 
	 * @param lookAndFeelSetting LookAndFeel Setting
	 * @return Class name for the given LookAndFeel Setting
	 */
	public static String getLookAndFeelClassName(LookAndFeelSetting lookAndFeelSetting) {
		String className = LOOK_AND_FEEL_CLASS_NAMES.get(lookAndFeelSetting);
		if (className == null) {
			return UIManager.getSystemLookAndFeelClassName();
		}
		return className;
	}

	/**
	 * Gets the name for the given LookAndFeel Setting
	 * 
	 * @param lookAndFeelSetting LookAndFeel Setting
	 * @return Name for the given LookAndFeel Setting
	 */
	public static String getLookAndFeelName(LookAndFeelSetting lookAndFeelSetting) {
		String name = LOOK_AND_FEEL_NAMES.get(lookAndFeelSetting);
		if (name == null) {
			return "OperatingSystem";
		}
		return name;
	}

	/**
	 * @return LookAndFeels
	 */
	public static List<LookAndFeelSetting> getLookAndFeels() {
		List<LookAndFeelSetting> lookAndFeels = new ArrayList<>();
		// Use entrySet instead of keySet, so that insertion order is preserved
		for (Map.Entry<LookAndFeelSetting, String> entry : LOOK_AND_FEEL_NAMES.entrySet()) {
			lookAndFeels.add(entry.getKey());
		}
		return lookAndFeels;
	}
}
