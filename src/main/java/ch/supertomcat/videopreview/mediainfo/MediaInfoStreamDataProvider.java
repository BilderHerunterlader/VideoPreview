package ch.supertomcat.videopreview.mediainfo;

import java.util.HashMap;
import java.util.Map;

import ch.supertomcat.mediainfo.InfoKind;
import ch.supertomcat.mediainfo.MediaInfo;
import ch.supertomcat.mediainfo.StreamKind;
import ch.supertomcat.videopreview.mediainfo.accessor.MediaInfoDataAccessor;
import ch.supertomcat.videopreview.mediainfo.accessor.MediaInfoDataAccessorParameter;
import ch.supertomcat.videopreview.mediainfo.formatter.MediaInfoDataFormatter;

/**
 * MediaInfo Data Provider of a specific Stream for templates
 */
public class MediaInfoStreamDataProvider {
	/**
	 * MediaInfo
	 */
	private final MediaInfo mediaInfo;

	/**
	 * Stream Kind
	 */
	private final StreamKind streamKind;

	/**
	 * Stream Number
	 */
	private final int streamNumber;

	/**
	 * Predefined Information
	 */
	private final Map<String, Object> info = new HashMap<>();

	/**
	 * Constructor
	 * 
	 * @param mediaInfo MediaInfo
	 * @param streamKind Stream Kind
	 * @param streamNumber Stream Number
	 */
	public MediaInfoStreamDataProvider(MediaInfo mediaInfo, StreamKind streamKind, int streamNumber) {
		this.mediaInfo = mediaInfo;
		this.streamKind = streamKind;
		this.streamNumber = streamNumber;

		switch (streamKind) {
			case VIDEO:
				initPredefinedVideoData();
				break;
			case AUDIO:
				initPredefinedAudioData();
				break;
			default:
				// Nothing to do
				break;
		}
	}

	private void initPredefinedVideoData() {
		Map<MediaInfoDataAccessor, MediaInfoDataFormatter> predefinedInfo = new HashMap<>();
		predefinedInfo.put(new MediaInfoDataAccessorParameter("ContainerFormat", mediaInfo, StreamKind.GENERAL, "Format"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("Format", mediaInfo, streamKind, streamNumber, "Format"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("Format_Version", mediaInfo, streamKind, streamNumber, "Format_Version"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("CodecID", mediaInfo, streamKind, streamNumber, "CodecID"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("Width", mediaInfo, streamKind, streamNumber, "Width"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("Height", mediaInfo, streamKind, streamNumber, "Height"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("PixelAspectRatio", mediaInfo, streamKind, streamNumber, "PixelAspectRatio"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("DisplayAspectRatio", mediaInfo, streamKind, streamNumber, "DisplayAspectRatio/String"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("FrameRate", mediaInfo, streamKind, streamNumber, "FrameRate"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("ScanType", mediaInfo, streamKind, streamNumber, "ScanType"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("MultiView_Count", mediaInfo, streamKind, streamNumber, "MultiView_Count"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("MultiView_Layout", mediaInfo, streamKind, streamNumber, "MultiView_Layout"), null);

		MediaInfoDataUtil.loadInfo(predefinedInfo, info);
	}

	private void initPredefinedAudioData() {
		Map<MediaInfoDataAccessor, MediaInfoDataFormatter> predefinedInfo = new HashMap<>();
		predefinedInfo.put(new MediaInfoDataAccessorParameter("Format", mediaInfo, streamKind, streamNumber, "Format"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("Format_Version", mediaInfo, streamKind, streamNumber, "Format_Version"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("SamplingRate", mediaInfo, streamKind, streamNumber, "SamplingRate/String"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("Channels", mediaInfo, streamKind, streamNumber, "Channel(s)"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("Language", mediaInfo, streamKind, streamNumber, "Language"), null);

		MediaInfoDataUtil.loadInfo(predefinedInfo, info);
	}

	/**
	 * Returns the streamKind
	 * 
	 * @return streamKind
	 */
	public StreamKind getStreamKind() {
		return streamKind;
	}

	/**
	 * Returns the streamNumber
	 * 
	 * @return streamNumber
	 */
	public int getStreamNumber() {
		return streamNumber;
	}

	/**
	 * Get a single information about a file (String parameter)
	 * 
	 * @param parameter Requested Parameter (Codec, Width, Bitrate, ...)
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(String parameter) {
		return mediaInfo.get(streamKind, streamNumber, parameter);
	}

	/**
	 * Get a single information about a file (String parameter)
	 * 
	 * @param parameter Requested Parameter (Codec, Width, Bitrate, ...)
	 * @param infoKind Kind for Information about the parameter (text, measure, help,...)
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(String parameter, String infoKind) {
		return mediaInfo.get(streamKind, streamNumber, parameter, InfoKind.valueOf(infoKind));
	}

	/**
	 * Get a single information about a file (String parameter)
	 * 
	 * @param parameter Requested Parameter (Codec, Width, Bitrate, ...)
	 * @param infoKind Kind for Information about the parameter (text, measure, help,...)
	 * @param searchKind Where to look for parameter
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(String parameter, String infoKind, String searchKind) {
		return mediaInfo.get(streamKind, streamNumber, parameter, InfoKind.valueOf(infoKind), InfoKind.valueOf(searchKind));
	}

	/**
	 * Get a single information about a file (integer parameter)
	 * 
	 * @param parameterIndex Requested Parameter Index
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(int parameterIndex) {
		return mediaInfo.get(streamKind, streamNumber, parameterIndex);
	}

	/**
	 * Get a single information about a file (integer parameter)
	 * 
	 * @param parameterIndex Requested Parameter Index
	 * @param infoKind Kind for Information about the parameter (text, measure, help,...)
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(int parameterIndex, String infoKind) {
		return mediaInfo.get(streamKind, streamNumber, parameterIndex, InfoKind.valueOf(infoKind));
	}

	/**
	 * Get number of Information of the given Stream
	 * 
	 * @return Number of Information of the given Stream
	 */
	public int count() {
		return mediaInfo.count(streamKind, streamNumber);
	}

	/**
	 * Returns the info
	 * 
	 * @return info
	 */
	public Map<String, Object> getInfo() {
		return info;
	}
}
