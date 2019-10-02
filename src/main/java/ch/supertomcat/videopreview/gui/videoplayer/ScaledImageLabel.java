package ch.supertomcat.videopreview.gui.videoplayer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Scaled Image Label
 */
public class ScaledImageLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param image Image
	 */
	public ScaledImageLabel(Icon image) {
		super(image);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Dimension compSize = getSize();
		int compWidth = compSize.width;
		int compHeight = compSize.height;

		g.setColor(getBackground());
		g.fillRect(0, 0, compWidth, compHeight);

		Icon icon = getIcon();
		if (!(icon instanceof ImageIcon)) {
			return;
		}
		ImageIcon imageIcon = (ImageIcon)icon;
		Image image = imageIcon.getImage();
		if (image == null) {
			return;
		}

		int width = image.getWidth(null);
		int height = image.getHeight(null);

		boolean scaleHeight = false;
		float scale = 1.0f;

		if (width > 0) {
			scale = (float)compWidth / width;
			if (scale * height > compHeight) {
				scale = (float)compHeight / height;
				scaleHeight = true;
			}
		}

		float scaledWidth = scale * width;
		float scaledHeight = scale * height;

		// Top Left X
		int dx1 = 0;
		// Top Left Y
		int dy1 = 0;
		if (scaleHeight) {
			dx1 = (int)((float)compWidth / 2 - scaledWidth / 2);
		} else {
			dy1 = (int)((float)compHeight / 2 - scaledHeight / 2);
		}
		// Bottom Right X
		int dx2 = dx1 + (int)scaledWidth;
		// Bottom Right Y
		int dy2 = dy1 + (int)scaledHeight;

		g.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, width, height, null);
	}
}
