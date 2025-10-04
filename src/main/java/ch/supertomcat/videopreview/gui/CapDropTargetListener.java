package ch.supertomcat.videopreview.gui;

import java.awt.EventQueue;
import java.awt.Image;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.supertomcatutils.gui.progress.ProgressObserver;
import ch.supertomcat.videopreview.util.CapUtil;

/**
 * Drop-Target-Listener for Cap-Table
 */
public class CapDropTargetListener implements DropTargetListener {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Caps Table Model
	 */
	private final VideoPreviewTableModel model;

	/**
	 * Progress Observer
	 */
	private final ProgressObserver progress;

	/**
	 * MainWindow Observer
	 */
	private final VideoPreviewWindowObserver mainWindowObserver;

	/**
	 * Row Height
	 */
	private final int rowHeight;

	/**
	 * Constructor
	 * 
	 * @param model Caps Table Model
	 * @param progress Progress Observer
	 * @param mainWindowObserver MainWindow Observer
	 * @param rowHeight Row Height
	 */
	public CapDropTargetListener(VideoPreviewTableModel model, ProgressObserver progress, VideoPreviewWindowObserver mainWindowObserver, int rowHeight) {
		this.model = model;
		this.progress = progress;
		this.mainWindowObserver = mainWindowObserver;
		this.rowHeight = rowHeight;
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
			if (files == null || files.stream().anyMatch(x -> !x.isFile())) {
				logger.error("Can only drag and drop files");
				dtde.dropComplete(false);
				return;
			}

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mainWindowObserver.lockGUI();
						progress.progressChanged(0, files.size(), 0);
						int i = 0;
						for (File file : files) {
							Image img = CapUtil.getCapPreview(file, rowHeight);
							if (img != null) {
								EventQueue.invokeLater(new Runnable() {
									@Override
									public void run() {
										model.addRow(file.getAbsolutePath(), img);
									}
								});
							}
							i++;
							progress.progressChanged(i);
						}
					} finally {
						mainWindowObserver.unlockGUI();
					}
				}
			});
			t.setName("DragAndDropCapsThread-" + t.threadId());
			t.start();

			dtde.dropComplete(true);
		} catch (UnsupportedFlavorException | IOException e) {
			logger.error("Could not drop cap files", e);
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
