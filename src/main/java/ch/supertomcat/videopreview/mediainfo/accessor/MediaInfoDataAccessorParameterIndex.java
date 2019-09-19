package ch.supertomcat.videopreview.mediainfo.accessor;

import ch.supertomcat.mediainfo.InfoKind;
import ch.supertomcat.mediainfo.MediaInfo;
import ch.supertomcat.mediainfo.StreamKind;

/**
 * Accessor for Media Info Data
 */
public class MediaInfoDataAccessorParameterIndex extends MediaInfoDataAccessorBase {
	/**
	 * Parameter
	 */
	protected final int parameterIndex;

	/**
	 * Info Kind
	 */
	protected final InfoKind infoKind;

	/**
	 * Constructor
	 * 
	 * @param key Key (Used as Output Parameter, not used to access data)
	 * @param mediaInfo Media Info
	 * @param streamKind Stream Kind
	 * @param parameterIndex Parameter Index
	 */
	public MediaInfoDataAccessorParameterIndex(String key, MediaInfo mediaInfo, StreamKind streamKind, int parameterIndex) {
		this(key, mediaInfo, streamKind, 0, parameterIndex, InfoKind.TEXT);
	}

	/**
	 * Constructor
	 * 
	 * @param key Key (Used as Output Parameter, not used to access data)
	 * @param mediaInfo Media Info
	 * @param streamKind Stream Kind
	 * @param streamNumber Stream Number
	 * @param parameterIndex Parameter Index
	 */
	public MediaInfoDataAccessorParameterIndex(String key, MediaInfo mediaInfo, StreamKind streamKind, int streamNumber, int parameterIndex) {
		this(key, mediaInfo, streamKind, streamNumber, parameterIndex, InfoKind.TEXT);
	}

	/**
	 * Constructor
	 * 
	 * @param key Key (Used as Output Parameter, not used to access data)
	 * @param mediaInfo Media Info
	 * @param streamKind Stream Kind
	 * @param parameterIndex Parameter Index
	 * @param infoKind Info Kind
	 */
	public MediaInfoDataAccessorParameterIndex(String key, MediaInfo mediaInfo, StreamKind streamKind, int parameterIndex, InfoKind infoKind) {
		this(key, mediaInfo, streamKind, 0, parameterIndex, infoKind);
	}

	/**
	 * Constructor
	 * 
	 * @param key Key (Used as Output Parameter, not used to access data)
	 * @param mediaInfo Media Info
	 * @param streamKind Stream Kind
	 * @param streamNumber Stream Number
	 * @param parameterIndex Parameter Index
	 * @param infoKind Info Kind
	 */
	public MediaInfoDataAccessorParameterIndex(String key, MediaInfo mediaInfo, StreamKind streamKind, int streamNumber, int parameterIndex, InfoKind infoKind) {
		super(key, mediaInfo, streamKind, streamNumber);
		this.parameterIndex = parameterIndex;
		this.infoKind = infoKind;
	}

	@Override
	public String getData() {
		return mediaInfo.get(streamKind, streamNumber, parameterIndex, infoKind);
	}
}
