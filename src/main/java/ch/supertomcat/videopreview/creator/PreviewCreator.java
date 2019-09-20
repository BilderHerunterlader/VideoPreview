package ch.supertomcat.videopreview.creator;

import java.io.File;
import java.util.List;

/**
 * PreviewCreator Interface
 */
public interface PreviewCreator {
	/**
	 * @param file Video File
	 * @param width Width of preview
	 * @param autoTile True if tiles should be generated automatically, false otherwise
	 * @param rows Rows (Only used when autoTile is false)
	 * @param columns Columns (Only used when autoTile is false)
	 * @param autoCaps True if caps should be created automatically, false otherwise
	 * @param caps List of Caps (Only used when autoCaps is false)
	 * @param mainTemplate Main Template
	 * @param footerTemplate Footer Template
	 * @throws PreviewCreatorException
	 */
	public void createPreview(File file, int width, boolean autoTile, int rows, int columns, boolean autoCaps, List<File> caps, File mainTemplate, File footerTemplate) throws PreviewCreatorException;
}
