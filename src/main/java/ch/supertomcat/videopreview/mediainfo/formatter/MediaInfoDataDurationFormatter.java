package ch.supertomcat.videopreview.mediainfo.formatter;

/**
 * Formatter for Duration
 */
public class MediaInfoDataDurationFormatter implements MediaInfoDataFormatter {

	@Override
	public String format(String data) {
		long duration = (long)Double.parseDouble(data);
		String format = String.format("%%0%dd", 2);
		duration = duration / 1000;
		String seconds = String.format(format, duration % 60);
		String minutes = String.format(format, (duration % 3600) / 60);
		String hours = String.format(format, duration / 3600);
		return hours + ":" + minutes + ":" + seconds;
	}

}
