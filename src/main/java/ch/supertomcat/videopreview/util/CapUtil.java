package ch.supertomcat.videopreview.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for caps
 */
public class CapUtil {
	/**
	 * Logger for this class
	 */
	private static Logger logger = LoggerFactory.getLogger(CapUtil.class);

	/**
	 * Reads an image and generates a Thumbnail for usage in Cap-Table
	 * 
	 * @param file Cap-File
	 * @param height Height
	 * @return Thumbnail-Image
	 */
	public static Image getCapPreview(Path file, int height) {
		try (InputStream in = Files.newInputStream(file)) {
			// Load image
			BufferedImage img = ImageIO.read(in);
			if (img == null) {
				logger.error("{} is not an Image", file);
				return null;
			} else {
				// Generate Thumbnail and return it
				return img.getScaledInstance(-1, height, Image.SCALE_DEFAULT);
			}
		} catch (IOException e) {
			logger.error("Could not load image: {}", file, e);
			return null;
		}
	}
}
