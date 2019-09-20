package ch.supertomcat.videopreview.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
	public static Image getCapPreview(File file, int height) {
		try {
			// Load image
			BufferedImage img = ImageIO.read(file);
			if (img == null) {
				logger.error(file.getAbsolutePath() + " is not an Image");
				return null;
			} else {
				// Generate Thumbnail and return it
				return img.getScaledInstance(-1, height, BufferedImage.SCALE_DEFAULT);
			}
		} catch (IOException e) {
			logger.error("Could not load image: {}", file.getAbsolutePath(), e);
			return null;
		}
	}
}
