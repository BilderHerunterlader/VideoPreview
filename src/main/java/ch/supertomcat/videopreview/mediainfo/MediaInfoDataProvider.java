package ch.supertomcat.videopreview.mediainfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.supertomcat.mediainfo.InfoKind;
import ch.supertomcat.mediainfo.MediaInfo;
import ch.supertomcat.mediainfo.StreamKind;
import ch.supertomcat.videopreview.mediainfo.accessor.MediaInfoDataAccessor;
import ch.supertomcat.videopreview.mediainfo.accessor.MediaInfoDataAccessorParameter;
import ch.supertomcat.videopreview.mediainfo.formatter.MediaInfoDataDurationFormatter;
import ch.supertomcat.videopreview.mediainfo.formatter.MediaInfoDataFileSizeFormatter;
import ch.supertomcat.videopreview.mediainfo.formatter.MediaInfoDataFormatter;

/**
 * MediaInfo Data Provider for Templates
 */
public class MediaInfoDataProvider implements AutoCloseable {
	/**
	 * File
	 */
	private final File file;

	/**
	 * Media Info Instance
	 */
	private final MediaInfo mediaInfo = new MediaInfo();

	/**
	 * Stream Map
	 */
	private final Map<StreamKind, List<MediaInfoStreamDataProvider>> streamMap = new HashMap<>();

	/**
	 * Predefined Information
	 */
	private final Map<String, Object> info = new HashMap<>();

	/**
	 * Constructor
	 * 
	 * @param file File
	 */
	public MediaInfoDataProvider(File file) {
		this.file = file;
		if (mediaInfo.open(file.getAbsolutePath()) <= 0) {
			// TODO Throw checked exception
			throw new RuntimeException("Could not open file: " + file);
		}

		for (StreamKind streamKind : StreamKind.values()) {
			int streamCount = mediaInfo.count(streamKind);
			if (streamCount > 0) {
				List<MediaInfoStreamDataProvider> streams = new ArrayList<>();
				for (int i = 0; i < streamCount; i++) {
					streams.add(new MediaInfoStreamDataProvider(mediaInfo, streamKind, i));
				}
				streamMap.put(streamKind, streams);
			}
		}

		Map<MediaInfoDataAccessor, MediaInfoDataFormatter> predefinedInfo = new HashMap<>();
		predefinedInfo.put(new MediaInfoDataAccessorParameter("Format", mediaInfo, StreamKind.GENERAL, "Format"), null);
		predefinedInfo.put(new MediaInfoDataAccessorParameter("FileSize", mediaInfo, StreamKind.GENERAL, "FileSize"), new MediaInfoDataFileSizeFormatter());
		predefinedInfo.put(new MediaInfoDataAccessorParameter("Duration", mediaInfo, StreamKind.GENERAL, "Duration"), new MediaInfoDataDurationFormatter());

		MediaInfoDataUtil.loadInfo(predefinedInfo, info);
	}

	@Override
	public void close() throws Exception {
		mediaInfo.closeFile();
	}

	/**
	 * @return File Path
	 */
	public String getFilePath() {
		return file.getAbsolutePath();
	}

	/**
	 * @return Filename
	 */
	public String getFilename() {
		return file.getName();
	}

	/**
	 * @return All information in a String
	 */
	public String getAllInfo() {
		return mediaInfo.inform();
	}

	/**
	 * Get a single information about a file (String parameter)
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @param streamNumber Number in Kind of Stream (first, second, ...)
	 * @param parameter Requested Parameter (Codec, Width, Bitrate, ...)
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(String streamKind, int streamNumber, String parameter) {
		return mediaInfo.get(StreamKind.valueOf(streamKind), streamNumber, parameter);
	}

	/**
	 * Get a single information about a file (String parameter)
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @param streamNumber Number in Kind of Stream (first, second, ...)
	 * @param parameter Requested Parameter (Codec, Width, Bitrate, ...)
	 * @param infoKind Kind for Information about the parameter (text, measure, help,...)
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(String streamKind, int streamNumber, String parameter, String infoKind) {
		return mediaInfo.get(StreamKind.valueOf(streamKind), streamNumber, parameter, InfoKind.valueOf(infoKind));
	}

	/**
	 * Get a single information about a file (String parameter)
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @param streamNumber Number in Kind of Stream (first, second, ...)
	 * @param parameter Requested Parameter (Codec, Width, Bitrate, ...)
	 * @param infoKind Kind for Information about the parameter (text, measure, help,...)
	 * @param searchKind Where to look for parameter
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(String streamKind, int streamNumber, String parameter, String infoKind, String searchKind) {
		return mediaInfo.get(StreamKind.valueOf(streamKind), streamNumber, parameter, InfoKind.valueOf(infoKind), InfoKind.valueOf(searchKind));
	}

	/**
	 * Get a single information about a file (integer parameter)
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @param streamNumber Number in Kind of Stream (first, second, ...)
	 * @param parameterIndex Requested Parameter Index
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(String streamKind, int streamNumber, int parameterIndex) {
		return mediaInfo.get(StreamKind.valueOf(streamKind), streamNumber, parameterIndex);
	}

	/**
	 * Get a single information about a file (integer parameter)
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @param streamNumber Number in Kind of Stream (first, second, ...)
	 * @param parameterIndex Requested Parameter Index
	 * @param infoKind Kind for Information about the parameter (text, measure, help,...)
	 * @return String containing the request information or empty String if there is a problem
	 */
	public String get(String streamKind, int streamNumber, int parameterIndex, String infoKind) {
		return mediaInfo.get(StreamKind.valueOf(streamKind), streamNumber, parameterIndex, InfoKind.valueOf(infoKind));
	}

	/**
	 * Get number of Streams of the given Stream Kind
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @return Number of Streams of the given Stream Kind
	 */
	public int count(String streamKind) {
		return mediaInfo.count(StreamKind.valueOf(streamKind));
	}

	/**
	 * Get number of Streams of the given Stream Kind or number of Information of the given Stream
	 * 
	 * @param streamKind Kind of Stream (general, video, audio, ...)
	 * @param streamNumber Number in Kind of Stream (first, second, ...)
	 * @return Number of Streams/Information of the given Stream Kind
	 */
	public int count(String streamKind, int streamNumber) {
		return mediaInfo.count(StreamKind.valueOf(streamKind), streamNumber);
	}

	/**
	 * Returns the streamMap
	 * 
	 * @return streamMap
	 */
	public Map<StreamKind, List<MediaInfoStreamDataProvider>> getStreamMap() {
		return streamMap;
	}

	/**
	 * @param streamKind Stream Kind
	 * @return List of Streams of the given Stream Kind
	 */
	private List<MediaInfoStreamDataProvider> getStreams(StreamKind streamKind) {
		List<MediaInfoStreamDataProvider> streams = streamMap.get(streamKind);
		if (streams == null) {
			return Collections.emptyList();
		}
		return streams;
	}

	/**
	 * @param streamKind Stream Kind
	 * @return List of Streams of the given Stream Kind
	 */
	public List<MediaInfoStreamDataProvider> getStreams(String streamKind) {
		return getStreams(StreamKind.valueOf(streamKind));
	}

	/**
	 * @return List of Video Streams
	 */
	public List<MediaInfoStreamDataProvider> getVideoStreams() {
		return getStreams(StreamKind.VIDEO);
	}

	/**
	 * @return List of Audio Streams
	 */
	public List<MediaInfoStreamDataProvider> getAudioStreams() {
		return getStreams(StreamKind.AUDIO);
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
