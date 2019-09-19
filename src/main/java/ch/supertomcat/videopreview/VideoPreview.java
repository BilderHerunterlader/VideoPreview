package ch.supertomcat.videopreview;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.opencv.core.Core;
import org.slf4j.LoggerFactory;

import com.sun.jna.Platform;

import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import ch.supertomcat.supertomcatutils.application.ApplicationUtil;
import ch.supertomcat.supertomcatutils.gui.Icons;
import ch.supertomcat.supertomcatutils.io.FileUtil;
import ch.supertomcat.videopreview.gui.VideoPreviewWindow;
import ch.supertomcat.videopreview.settings.SettingsManager;
import ch.supertomcat.videopreview.templates.TemplateManager;

/**
 * 
 */
public class VideoPreview {
	private static JFrame createInvisibleFrame() {
		JFrame frame = new JFrame("VideoPreview");
		frame.setIconImage(Icons.getImage("/ch/supertomcat/videopreview/gui/icons/VideoPreview-16x16.png"));
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		return frame;
	}

	/**
	 * Format Stacktrace to String
	 * 
	 * @param throwable Throwable
	 * @return Stacktrace as String
	 */
	private static String formatStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}

	/**
	 * Main-Method
	 * 
	 * @param args Arguments
	 */
	public static void main(String[] args) {
		try {
			ApplicationProperties.initProperties(VideoPreview.class.getResourceAsStream("/Application_Config.properties"));
		} catch (IOException e) {
			// Logger is not initialized at this point
			System.err.println("Could not initialize application properties");
			e.printStackTrace();
			ApplicationUtil.writeBasicErrorLogfile(new File("VP-Error.log"), "Could not initialize application properties:\n" + formatStackTrace(e));
			System.exit(1);
		}

		String jarFilename = ApplicationUtil.getThisApplicationsJarFilename(VideoPreview.class);
		ApplicationProperties.setProperty("JarFilename", jarFilename);

		// Geth the program directory
		String appPath = ApplicationUtil.getThisApplicationsPath(!jarFilename.isEmpty() ? jarFilename : ApplicationProperties.getProperty("ApplicationShortName") + ".jar");
		ApplicationProperties.setProperty("ApplicationPath", appPath);

		String programUserDir = System.getProperty("user.home") + FileUtil.FILE_SEPERATOR + "." + ApplicationProperties.getProperty("ApplicationShortName") + FileUtil.FILE_SEPERATOR;
		ApplicationProperties.setProperty("ProfilePath", programUserDir);
		ApplicationProperties.setProperty("DatabasePath", programUserDir);
		ApplicationProperties.setProperty("SettingsPath", programUserDir);
		ApplicationProperties.setProperty("UploadLogPath", programUserDir);
		ApplicationProperties.setProperty("LogsPath", programUserDir);

		String logFilename = ApplicationProperties.getProperty("ApplicationShortName") + ".log";
		// Loggers can be created after this point
		System.setProperty("bhlog4jlogfile", programUserDir + FileUtil.FILE_SEPERATOR + logFilename);

		ApplicationUtil.initializeSLF4JUncaughtExceptionHandler();

		// Write some useful info to the logfile
		ApplicationUtil.logApplicationInfo();

		// Delete old log files
		ApplicationUtil.deleteOldLogFiles(7, logFilename, ApplicationProperties.getProperty("LogsPath"));

		try {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (InstantiationException e) {
			} catch (ClassNotFoundException e) {
			} catch (UnsupportedLookAndFeelException e) {
			} catch (IllegalAccessException e) {
			}

			if (Platform.isWindows()) {
				File openCVPath = new File(ApplicationProperties.getProperty("ApplicationPath"), "OpenCV");
				if (Platform.is64Bit()) {
					System.load(new File(openCVPath, "java/x64/opencv_java347.dll").getAbsolutePath());
					System.load(new File(openCVPath, "bin/opencv_ffmpeg347_64.dll").getAbsolutePath());
				} else {
					System.load(new File(openCVPath, "java/x86/opencv_java347.dll").getAbsolutePath());
					System.load(new File(openCVPath, "bin/opencv_ffmpeg347.dll").getAbsolutePath());
				}
			} else {
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			}

			SettingsManager settingsManager = new SettingsManager(ApplicationProperties.getProperty("SettingsPath"), "settings.xml");
			// Read the settings from settings file
			settingsManager.readSettings();

			TemplateManager templateManager = new TemplateManager(null);

			new VideoPreviewWindow(settingsManager, templateManager);
		} catch (Exception | UnsatisfiedLinkError e) {
			LoggerFactory.getLogger(VideoPreview.class).error("Could not initialized VideoPreview", e);
			JFrame frame = null;
			try {
				frame = createInvisibleFrame();
				JOptionPane.showMessageDialog(frame, "VideoPreview could not be started!: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (frame != null) {
					frame.dispose();
				}
			}
			System.exit(1);
		}
	}
}
