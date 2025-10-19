package ch.supertomcat.videopreview.creator;

import java.nio.file.Path;
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
	 * @param selectCaps True if caps should be selected manually from video, false otherwise (Only used when autoCaps is false)
	 * @param caps List of Caps (Only used when autoCaps is false and selectCaps is false)
	 * @param mainTemplate Main Template
	 * @param footerTemplate Footer Template
	 * @throws PreviewCreatorException
	 */
	public void createPreview(Path file, int width, boolean autoTile, int rows, int columns, boolean autoCaps, boolean selectCaps, List<Path> caps, Path mainTemplate,
			Path footerTemplate) throws PreviewCreatorException;
}
