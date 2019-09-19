package ch.supertomcat.videopreview.gui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 * TableModel for Cap-Table
 */
public class VideoPreviewTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public VideoPreviewTableModel() {
		addColumn("Cap-Preview");
		addColumn("Cap-File");
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	/**
	 * Adds a row
	 * 
	 * @param file Path to Cap-File
	 * @param img Thumbnail of the Cap-File
	 */
	public void addRow(String file, Image img) {
		ImageIcon icon = null;
		if (img != null) {
			icon = new ImageIcon(img);
		}
		Object data[] = new Object[2];
		data[0] = icon;
		data[1] = file;
		this.addRow(data);
	}
}
