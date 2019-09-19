package ch.supertomcat.videopreview.mediainfo;

import java.util.HashMap;
import java.util.Map;

import ch.supertomcat.videopreview.mediainfo.accessor.MediaInfoDataAccessor;
import ch.supertomcat.videopreview.mediainfo.formatter.MediaInfoDataFormatter;

/**
 * Utility class for MediaInfo Data
 */
public final class MediaInfoDataUtil {
	/**
	 * Constructor
	 */
	private MediaInfoDataUtil() {
	}

	/**
	 * Load predefined info
	 * 
	 * @param predefinedInfo Predefined Info to load
	 * @return Loaded Info
	 */
	public static Map<String, Object> loadInfo(Map<MediaInfoDataAccessor, MediaInfoDataFormatter> predefinedInfo) {
		Map<String, Object> info = new HashMap<>();
		loadInfo(predefinedInfo, info);
		return info;
	}

	/**
	 * Load predefined info
	 * 
	 * @param predefinedInfo Predefined Info to load
	 * @param info Map into which the info is loaded
	 */
	public static void loadInfo(Map<MediaInfoDataAccessor, MediaInfoDataFormatter> predefinedInfo, Map<String, Object> info) {
		for (Map.Entry<MediaInfoDataAccessor, MediaInfoDataFormatter> entry : predefinedInfo.entrySet()) {
			MediaInfoDataAccessor accessor = entry.getKey();
			MediaInfoDataFormatter formatter = entry.getValue();
			String outputValue;
			if (formatter != null) {
				outputValue = formatter.format(accessor.getData());
			} else {
				outputValue = accessor.getData();
			}
			info.put(accessor.getKey(), outputValue);
		}
	}

}
