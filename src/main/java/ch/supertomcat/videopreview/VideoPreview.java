package ch.supertomcat.videopreview;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.slf4j.LoggerFactory;

import com.sun.jna.Platform;

import ch.supertomcat.supertomcatutils.application.ApplicationMain;
import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import ch.supertomcat.videopreview.gui.VideoPreviewWindow;
import ch.supertomcat.videopreview.settings.SettingsManager;
import ch.supertomcat.videopreview.templates.TemplateManager;

/**
 * Video Preview
 */
public class VideoPreview {
	/**
	 * Main-Method
	 * 
	 * @param args Arguments
	 */
	public static void main(String[] args) {
		List<String> additionalPaths = Arrays.asList("SettingsPath");
		ApplicationMain applicationMain = new ApplicationMain("VP", null, true, false, VideoPreview.class, additionalPaths) {
			@Override
			protected void main(String[] args) {
				try {
					if (Platform.isWindows()) {
						File openCVPath = new File(ApplicationProperties.getProperty(ApplicationMain.APPLICATION_PATH), "OpenCV");
						if (Platform.is64Bit()) {
							System.load(new File(openCVPath, "java/x64/opencv_java451.dll").getAbsolutePath());
							System.load(new File(openCVPath, "bin/opencv_videoio_ffmpeg451_64.dll").getAbsolutePath());
						} else {
							System.load(new File(openCVPath, "java/x86/opencv_java451.dll").getAbsolutePath());
							System.load(new File(openCVPath, "bin/opencv_videoio_ffmpeg451.dll").getAbsolutePath());
						}
					} else {
						System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
					}

					SettingsManager settingsManager = new SettingsManager(ApplicationProperties.getProperty(ApplicationMain.SETTINGS_PATH), "settings.xml");
					// Read the settings from settings file
					settingsManager.readSettings();

					TemplateManager templateManager = new TemplateManager(null);

					new VideoPreviewWindow(settingsManager, templateManager);
				} catch (Exception | UnsatisfiedLinkError e) {
					LoggerFactory.getLogger(VideoPreview.class).error("Could not initialized VideoPreview", e);
					displayStartupError("VideoPreview could not be started!: " + e.getMessage());
					System.exit(1);
				}
			}
		};
		applicationMain.start(args);
	}
}
