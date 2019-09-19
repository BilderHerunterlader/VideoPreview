package ch.supertomcat.videopreview.mediainfo.formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.supertomcatutils.gui.formatter.UnitFormatUtil;

/**
 * Formatter for File Size
 */
public class MediaInfoDataFileSizeFormatter implements MediaInfoDataFormatter {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String format(String data) {
		try {
			return UnitFormatUtil.getSizeString(Long.parseLong(data), 3);
		} catch (NumberFormatException nfe) {
			logger.error("Could not format file size", nfe);
			return "Could not format file size";
		}
	}
}
