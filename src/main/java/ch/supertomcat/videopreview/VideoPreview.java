package ch.supertomcat.videopreview;

import java.nio.file.Path;
import java.nio.file.Paths;
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
						Path openCVPath = Paths.get(ApplicationProperties.getProperty(ApplicationMain.APPLICATION_PATH), "OpenCV");
						if (Platform.is64Bit()) {
							System.load(openCVPath.resolve("java/x64/opencv_java4120.dll").toAbsolutePath().toString());
							System.load(openCVPath.resolve("bin/opencv_videoio_ffmpeg4120_64.dll").toAbsolutePath().toString());
						} else {
							System.load(openCVPath.resolve("java/x86/opencv_java4120.dll").toAbsolutePath().toString());
							System.load(openCVPath.resolve("bin/opencv_videoio_ffmpeg4120.dll").toAbsolutePath().toString());
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
