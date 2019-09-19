package ch.supertomcat.videopreview.gui;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.io.File;
import java.util.List;

import ch.supertomcat.videopreview.creator.CapUtil;

/**
 * Drop-Target-Listener for Cap-Table
 */
public class CapDropTargetListener implements DropTargetListener {
	private VideoPreviewTableModel model;
	private VideoPreviewWindow window;

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public CapDropTargetListener(VideoPreviewTableModel model, VideoPreviewWindow window) {
		this.model = model;
		this.window = window;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragExit(DropTargetEvent dte) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragOver(DropTargetDragEvent dtde) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public void drop(DropTargetDropEvent dtde) {
		boolean accepted = false;
		try {
			Transferable tr = dtde.getTransferable();
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (flavors[i].isFlavorJavaFileListType()) {
					dtde.acceptDrop(dtde.getDropAction());
					accepted = true;
					Object td = tr.getTransferData(flavors[i]);
					if (td instanceof List) {
						final List<?> fl = (List<?>)td;
						Thread t = new Thread(new Runnable() {
							@Override
							public void run() {
								window.lockGUI(true);
								window.setPGMin(0);
								window.setPGMax(fl.size() - 1);
								window.updatePG(0);
								for (int x = 0; x < fl.size(); x++) {
									final File f = (File)fl.get(x);
									if (f.isFile()) {
										final Image img = CapUtil.getCapPreview(f, VideoPreviewWindow.ROW_HEIGHT);
										EventQueue.invokeLater(new Runnable() {
											@Override
											public void run() {
												model.addRow(f.getAbsolutePath(), img);
											}
										});
									}
									window.updatePG(x);
								}
								window.lockGUI(false);
							}
						});
						t.setPriority(Thread.MIN_PRIORITY);
						t.start();
						dtde.dropComplete(true);
						return;
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		try {
			if (accepted == false) {
				dtde.rejectDrop();
			}
		} catch (InvalidDnDOperationException idoe) {
			idoe.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

}
