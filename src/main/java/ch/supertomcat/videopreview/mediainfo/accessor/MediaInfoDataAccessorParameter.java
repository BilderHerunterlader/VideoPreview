package ch.supertomcat.videopreview.mediainfo.accessor;

import ch.supertomcat.mediainfo.InfoKind;
import ch.supertomcat.mediainfo.MediaInfo;
import ch.supertomcat.mediainfo.StreamKind;

/**
 * Accessor for Media Info Data
 */
public class MediaInfoDataAccessorParameter extends MediaInfoDataAccessorBase {
	/**
	 * Parameter
	 */
	protected final String parameter;

	/**
	 * Info Kind
	 */
	protected final InfoKind infoKind;

	/**
	 * Search Kind
	 */
	protected final InfoKind searchKind;

	/**
	 * Constructor
	 * 
	 * @param key Key (Used as Output Parameter, not used to access data)
	 * @param mediaInfo Media Info
	 * @param streamKind Stream Kind
	 * @param parameter Parameter
	 */
	public MediaInfoDataAccessorParameter(String key, MediaInfo mediaInfo, StreamKind streamKind, String parameter) {
		this(key, mediaInfo, streamKind, 0, parameter, InfoKind.TEXT, InfoKind.NAME);
	}

	/**
	 * Constructor
	 * 
	 * @param key Key (Used as Output Parameter, not used to access data)
	 * @param mediaInfo Media Info
	 * @param streamKind Stream Kind
	 * @param streamNumber Stream Number
	 * @param parameter Parameter
	 */
	public MediaInfoDataAccessorParameter(String key, MediaInfo mediaInfo, StreamKind streamKind, int streamNumber, String parameter) {
		this(key, mediaInfo, streamKind, streamNumber, parameter, InfoKind.TEXT, InfoKind.NAME);
	}

	/**
	 * Constructor
	 * 
	 * @param key Key (Used as Output Parameter, not used to access data)
	 * @param mediaInfo Media Info
	 * @param streamKind Stream Kind
	 * @param parameter Parameter
	 * @param infoKind Info Kind
	 */
	public MediaInfoDataAccessorParameter(String key, MediaInfo mediaInfo, StreamKind streamKind, String parameter, InfoKind infoKind) {
		this(key, mediaInfo, streamKind, 0, parameter, infoKind, InfoKind.NAME);
	}

	/**
	 * Constructor
	 * 
	 * @param key Key (Used as Output Parameter, not used to access data)
	 * @param mediaInfo Media Info
	 * @param streamKind Stream Kind
	 * @param streamNumber Stream Number
	 * @param parameter Parameter
	 * @param infoKind Info Kind
	 */
	public MediaInfoDataAccessorParameter(String key, MediaInfo mediaInfo, StreamKind streamKind, int streamNumber, String parameter, InfoKind infoKind) {
		this(key, mediaInfo, streamKind, streamNumber, parameter, infoKind, InfoKind.NAME);
	}

	/**
	 * Constructor
	 * 
	 * @param key Key (Used as Output Parameter, not used to access data)
	 * @param mediaInfo Media Info
	 * @param streamKind Stream Kind
	 * @param parameter Parameter
	 * @param infoKind Info Kind
	 * @param searchKind Search Kind
	 */
	public MediaInfoDataAccessorParameter(String key, MediaInfo mediaInfo, StreamKind streamKind, String parameter, InfoKind infoKind, InfoKind searchKind) {
		this(key, mediaInfo, streamKind, 0, parameter, infoKind, searchKind);
	}

	/**
	 * Constructor
	 * 
	 * @param key Key (Used as Output Parameter, not used to access data)
	 * @param mediaInfo Media Info
	 * @param streamKind Stream Kind
	 * @param streamNumber Stream Number
	 * @param parameter Parameter
	 * @param infoKind Info Kind
	 * @param searchKind Search Kind
	 */
	public MediaInfoDataAccessorParameter(String key, MediaInfo mediaInfo, StreamKind streamKind, int streamNumber, String parameter, InfoKind infoKind, InfoKind searchKind) {
		super(key, mediaInfo, streamKind, streamNumber);
		this.parameter = parameter;
		this.infoKind = infoKind;
		this.searchKind = searchKind;
	}

	@Override
	public String getData() {
		return mediaInfo.get(streamKind, streamNumber, parameter, infoKind, searchKind);
	}
}
