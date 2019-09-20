package ch.supertomcat.videopreview.gui;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Progress Observer for VideoPreviewWindow
 */
public class VideoPreviewWindowObserver {
	/**
	 * Listeners
	 */
	private List<VideoPreviewWindowListener> listeners = new CopyOnWriteArrayList<>();

	/**
	 * Lock GUI
	 */
	public void lockGUI() {
		for (VideoPreviewWindowListener listener : listeners) {
			listener.lockGUI();
		}
	}

	/**
	 * Unlock GUI
	 */
	public void unlockGUI() {
		for (VideoPreviewWindowListener listener : listeners) {
			listener.unlockGUI();
		}
	}

	/**
	 * Add listener
	 * 
	 * @param l Listener
	 */
	public void addProgressListener(VideoPreviewWindowListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * Remove listener
	 * 
	 * @param l Listener
	 */
	public void removeProgressListener(VideoPreviewWindowListener l) {
		listeners.remove(l);
	}
}
