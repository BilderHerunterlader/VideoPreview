package ch.supertomcat.mediainfo;

/**
 * MediaInfo Status
 */
public enum Status {
	/**
	 * None
	 */
	NONE(0x00),

	/**
	 * Accepted
	 */
	ACCEPTED(0x01),

	/**
	 * Filled
	 */
	FILLED(0x02),

	/**
	 * Updated
	 */
	UPDATED(0x04),

	/**
	 * Finalized
	 */
	FINALIZED(0x08);

	/**
	 * Native Value
	 */
	private final int value;

	/**
	 * Constructor
	 * 
	 * @param value Native Value
	 */
	private Status(int value) {
		this.value = value;
	}

	/**
	 * Returns the native value
	 * 
	 * @return value Native Value
	 */
	public int getValue() {
		return value;
	}
}
