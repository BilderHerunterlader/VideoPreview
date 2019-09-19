package ch.supertomcat.videopreview.mediainfo.accessor;

/**
 * Accessor for Media Info Data
 */
public interface MediaInfoDataAccessor {
	/**
	 * @return Data
	 */
	public String getData();

	/**
	 * @return Key (Used as Output Parameter, not used to access data)
	 */
	public String getKey();
}
