package ch.supertomcat.videopreview.mediainfo.accessor;

import ch.supertomcat.mediainfo.MediaInfo;
import ch.supertomcat.mediainfo.StreamKind;

/**
 * Accessor for Media Info Data
 */
public abstract class MediaInfoDataAccessorBase implements MediaInfoDataAccessor {
	/**
	 * Key (Used as Output Parameter, not used to access data)
	 */
	protected final String key;

	/**
	 * Media Info
	 */
	protected final MediaInfo mediaInfo;

	/**
	 * Stream Kind
	 */
	protected final StreamKind streamKind;

	/**
	 * Stream Number
	 */
	protected final int streamNumber;

	/**
	 * Constructor
	 * 
	 * @param key Key (Used as Output Parameter, not used to access data)
	 * @param mediaInfo Media Info
	 * @param streamKind Stream Kind
	 */
	public MediaInfoDataAccessorBase(String key, MediaInfo mediaInfo, StreamKind streamKind) {
		this(key, mediaInfo, streamKind, 0);
	}

	/**
	 * Constructor
	 * 
	 * @param key Key (Used as Output Parameter, not used to access data)
	 * @param mediaInfo Media Info
	 * @param streamKind Stream Kind
	 * @param streamNumber Stream Number
	 */
	public MediaInfoDataAccessorBase(String key, MediaInfo mediaInfo, StreamKind streamKind, int streamNumber) {
		this.key = key;
		this.mediaInfo = mediaInfo;
		this.streamKind = streamKind;
		this.streamNumber = streamNumber;
	}

	@Override
	public String getKey() {
		return key;
	}

	/**
	 * Returns the mediaInfo
	 * 
	 * @return mediaInfo
	 */
	public MediaInfo getMediaInfo() {
		return mediaInfo;
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
}
