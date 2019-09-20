package ch.supertomcat.videopreview.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Drop-Target-Listener for Video-TextField
 */
public class VideoDropTargetListener implements DropTargetListener {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Video TextField
	 */
	private final JTextField txtVideo;

	/**
	 * Multi Mode Radio Button
	 */
	private final JRadioButton rbMulti;

	/**
	 * Constructor
	 * 
	 * @param txtVideo Video TextField
	 * @param rbMulti Multi Mode Radio Button
	 */
	public VideoDropTargetListener(JTextField txtVideo, JRadioButton rbMulti) {
		this.txtVideo = txtVideo;
		this.rbMulti = rbMulti;
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		checkDragAccepted(dtde);
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		checkDragAccepted(dtde);
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		checkDragAccepted(dtde);
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// Nothing to do
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		if (!dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			dtde.rejectDrop();
			return;
		}

		dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

		try {
			@SuppressWarnings("unchecked")
			List<File> files = (List<File>)dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
			if (files.size() != 1) {
				logger.error("Can only drag and drop single files or folders");
				dtde.dropComplete(false);
				return;
			}
			File file = files.get(0);
			if (rbMulti.isSelected() && !file.isDirectory()) {
				logger.error("Can only drag and drop folder in Multi Mode");
				dtde.dropComplete(false);
				return;
			} else if (!rbMulti.isSelected() && !file.isFile()) {
				logger.error("Can only drag and drop file in Single Mode");
				dtde.dropComplete(false);
				return;
			}
			txtVideo.setText(file.getAbsolutePath());
			dtde.dropComplete(true);
		} catch (UnsupportedFlavorException | IOException e) {
			logger.error("Could not drop files", e);
			dtde.dropComplete(false);
		}
	}

	private void checkDragAccepted(DropTargetDragEvent dtde) {
		if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
		} else {
			dtde.rejectDrag();
		}
	}
}
