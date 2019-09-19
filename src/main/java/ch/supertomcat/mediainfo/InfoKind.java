package ch.supertomcat.mediainfo;

/**
 * MediaInfo Info Kind
 */
public enum InfoKind {
	/**
	 * Unique parameter name
	 */
	NAME,

	/**
	 * Parameter Value
	 */
	TEXT,

	/**
	 * Unique measure unit parameter name
	 */
	MEASURE,

	/**
	 * Options
	 */
	OPTIONS,

	/**
	 * Translated parameter name
	 */
	NAME_TEXT,

	/**
	 * Translated measure unit parameter name
	 */
	MEASURE_TEXT,

	/**
	 * More information about the parameter
	 */
	INFO,

	/**
	 * How this parameter is supported, could be N (No), B (Beta), R (Read only), W
	 * (Read/Write)
	 */
	HOW_TO,

	/**
	 * Domain of this piece of information
	 */
	DOMAIN;
}
