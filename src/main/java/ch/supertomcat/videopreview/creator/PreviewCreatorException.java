package ch.supertomcat.videopreview.creator;

/**
 * PreviewCreator Exception
 */
public class PreviewCreatorException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message Message
	 */
	public PreviewCreatorException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param cause Cause
	 */
	public PreviewCreatorException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor
	 * 
	 * @param message Message
	 * @param cause Cause
	 */
	public PreviewCreatorException(String message, Throwable cause) {
		super(message, cause);
	}
}
