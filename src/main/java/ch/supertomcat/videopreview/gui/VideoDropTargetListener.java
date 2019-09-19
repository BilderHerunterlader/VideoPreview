package ch.supertomcat.videopreview.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.io.File;
import java.util.List;

import javax.swing.JTextField;

/**
 * Drop-Target-Listener for Video-TextField
 */
public class VideoDropTargetListener implements DropTargetListener {

	private JTextField txtVideo = null;

	/**
	 * Constructor
	 * 
	 * @param txtVideo
	 */
	public VideoDropTargetListener(JTextField txtVideo) {
		super();
		this.txtVideo = txtVideo;
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
						List<?> fl = (List<?>)td;
						if (fl.size() == 1) {
							for (int x = 0; x < fl.size(); x++) {
								File f = (File)fl.get(x);
								txtVideo.setText(f.getAbsolutePath());
								f = null;
							}
							dtde.dropComplete(true);
							return;
						}
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

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}
}
