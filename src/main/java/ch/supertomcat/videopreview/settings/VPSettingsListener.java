package ch.supertomcat.videopreview.settings;

import ch.supertomcat.supertomcatutils.settings.SettingsListener;

/**
 * VideoPreview Settings Listener
 */
public interface VPSettingsListener extends SettingsListener {
	/**
	 * Look and Feel changed
	 */
	public void lookAndFeelChanged();
}
