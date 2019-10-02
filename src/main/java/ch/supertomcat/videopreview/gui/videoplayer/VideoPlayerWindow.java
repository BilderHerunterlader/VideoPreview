package ch.supertomcat.videopreview.gui.videoplayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.supertomcatutils.gui.Icons;
import ch.supertomcat.supertomcatutils.gui.PositionUtil;

/**
 * Video Player Window
 */
public class VideoPlayerWindow extends JDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Frame
	 */
	private Mat frame = new Mat();

	/**
	 * Video
	 */
	private VideoCapture video;

	/**
	 * Video Frame Icon
	 */
	private final ImageIcon videoFrameIcon = new ImageIcon();

	/**
	 * Video Frame
	 */
	private final ScaledImageLabel lblVideoFrame = new ScaledImageLabel(videoFrameIcon);

	/**
	 * Slider
	 */
	private final JSlider slider;

	/**
	 * Frame Width for Captures
	 */
	private final int captureFrameWidth;

	/**
	 * Captures
	 */
	private final List<Mat> captures = new ArrayList<>();

	/**
	 * Video Player Runnable
	 */
	private VideoPlayerRunnable videoPlayerRunnable = null;

	/**
	 * Video Player Thread
	 */
	private Thread videoPlayerThread = null;

	/**
	 * Constructor
	 * 
	 * @param title Window Title
	 * @param parent Parent or null
	 * @param video Video
	 * @param captureFrameWidth Frame Width for Captures
	 */
	public VideoPlayerWindow(String title, JFrame parent, VideoCapture video, int captureFrameWidth) {
		super(parent, title);
		this.video = video;
		this.captureFrameWidth = captureFrameWidth;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		int frameCount = (int)video.get(Videoio.CV_CAP_PROP_FRAME_COUNT);
		if (frameCount <= 0) {
			// TODO Handle this case somehow
			logger.error("Could not get frame count from video");
		}

		lblVideoFrame.setOpaque(true);
		lblVideoFrame.setBackground(Color.BLACK);
		add(lblVideoFrame, BorderLayout.CENTER);

		slider = new JSlider(0, frameCount, 0);
		slider.setPaintTicks(true);
		slider.addChangeListener(new ChangeListener() {
			private boolean seekNeeded = false;

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (source.getValueIsAdjusting()) {
					seekNeeded = true;
				} else if (seekNeeded) {
					seekNeeded = false;
					boolean playing = isPlaying();
					pause();
					seek(source.getValue());
					if (playing) {
						start();
					} else {
						displayFrame();
					}
				} else if (!isPlaying()) {
					seek(source.getValue());
					displayFrame();
				}
			}
		});

		JButton btnStart = new JButton(Icons.getTangoIcon("actions/media-playback-start.png", 16));
		btnStart.addActionListener(e -> start());
		JButton btnPause = new JButton(Icons.getTangoIcon("actions/media-playback-pause.png", 16));
		btnPause.addActionListener(e -> pause());
		JButton btnStop = new JButton(Icons.getTangoIcon("actions/media-playback-stop.png", 16));
		btnStop.addActionListener(e -> stop());
		JButton btnCapture = new JButton(Icons.getTangoIcon("actions/media-record.png", 16));
		btnCapture.addActionListener(e -> capture());
		JButton btnSkipBackward = new JButton(Icons.getTangoIcon("actions/media-skip-backward.png", 16));
		btnSkipBackward.addActionListener(e -> skipBackward());
		JButton btnSeekBackward = new JButton(Icons.getTangoIcon("actions/media-seek-backward.png", 16));
		btnSeekBackward.addActionListener(e -> seekBackward());
		JButton btnSeekForward = new JButton(Icons.getTangoIcon("actions/media-seek-forward.png", 16));
		btnSeekForward.addActionListener(e -> seekForward());
		JButton btnSkipForward = new JButton(Icons.getTangoIcon("actions/media-skip-forward.png", 16));
		btnSkipForward.addActionListener(e -> skipForward());
		JButton btnStep = new JButton(Icons.getTangoIcon("actions/go-next.png", 16));
		btnStep.addActionListener(e -> step());

		JPanel pnlButtons = new JPanel();
		pnlButtons.add(btnStart);
		pnlButtons.add(btnPause);
		pnlButtons.add(btnStop);
		pnlButtons.add(new JSeparator(JSeparator.VERTICAL));
		pnlButtons.add(btnCapture);
		pnlButtons.add(new JSeparator(JSeparator.VERTICAL));
		pnlButtons.add(btnSkipBackward);
		pnlButtons.add(btnSeekBackward);
		pnlButtons.add(btnSeekForward);
		pnlButtons.add(btnSkipForward);
		pnlButtons.add(new JSeparator(JSeparator.VERTICAL));
		pnlButtons.add(btnStep);

		JPanel pnlControls = new JPanel(new BorderLayout());
		pnlControls.add(slider, BorderLayout.NORTH);
		pnlControls.add(pnlButtons, BorderLayout.SOUTH);
		add(pnlControls, BorderLayout.SOUTH);

		displayFrame();

		pack();
		GraphicsDevice device = PositionUtil.getScreenDeviceOfComponent(this);
		if (device != null) {
			Rectangle screenBounds = device.getDefaultConfiguration().getBounds();
			if (getWidth() > screenBounds.width) {
				setSize(screenBounds.width, getHeight());
			}
			if (getHeight() > screenBounds.height) {
				setSize(getWidth(), screenBounds.height);
			}
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				pause();
			}
		});

		PositionUtil.setPositionMiddleScreen(this, parent);
	}

	/**
	 * Returns the captures
	 * 
	 * @return captures
	 */
	public List<Mat> getCaptures() {
		return captures;
	}

	/**
	 * Start
	 */
	private void start() {
		if (isPlaying()) {
			return;
		}

		double fps = video.get(Videoio.CV_CAP_PROP_FPS);
		if (fps <= 0.0d) {
			// TODO Handle this case somehow
			logger.error("Could not get fps from video");
			return;
		}

		videoPlayerRunnable = new VideoPlayerRunnable(fps);
		videoPlayerThread = new Thread(videoPlayerRunnable);
		videoPlayerThread.setName("Video-Player-Thread" + videoPlayerThread.getId());
		videoPlayerThread.start();
	}

	/**
	 * Pause
	 */
	private void pause() {
		if (videoPlayerRunnable == null) {
			return;
		}

		videoPlayerRunnable.stop();
		try {
			videoPlayerThread.join();
		} catch (InterruptedException e) {
			logger.error("Thread Join was interrupted", e);
		}
		videoPlayerRunnable = null;
		videoPlayerThread = null;
	}

	/**
	 * Stop
	 */
	private void stop() {
		pause();
		dispose();
	}

	/**
	 * Capture
	 */
	private void capture() {
		Mat localFrame = frame;
		if (!localFrame.empty()) {
			captures.add(resizeCap(localFrame, captureFrameWidth));
		}
	}

	/**
	 * TODO Remove duplicate code, this was copied from PreviewCreatorOpenCV
	 * Resize Cap
	 * 
	 * @param cap Cap
	 * @param frameWidth Frame Width
	 * @return Resized Cap
	 */
	private Mat resizeCap(Mat cap, int frameWidth) {
		double scaleFactor = (double)frameWidth / cap.width();
		if (scaleFactor != 0.0d) {
			int interpolation = cap.width() > frameWidth ? Imgproc.INTER_AREA : Imgproc.INTER_CUBIC;
			Mat resizedCap = new Mat();
			// Zero means it will be calculated using scale factor
			Size sz = new Size(0, 0);
			Imgproc.resize(cap, resizedCap, sz, scaleFactor, scaleFactor, interpolation);
			return resizedCap;
		} else {
			return cap;
		}
	}

	/**
	 * Skip Backward
	 */
	private void skipBackward() {
		if (seek(0)) {
			if (displayFrame()) {
				updateDisplayedPosition(0);
			}
		}
	}

	/**
	 * Seek Backward
	 */
	private void seekBackward() {

	}

	/**
	 * Seek Forward
	 */
	private void seekForward() {

	}

	/**
	 * Skip Forward
	 */
	private void skipForward() {
		int frameCount = (int)video.get(Videoio.CV_CAP_PROP_FRAME_COUNT);
		if (frameCount > 0) {
			if (seek(frameCount - 2)) {
				if (displayFrame()) {
					updateDisplayedPosition(frameCount - 1);
				}
			}
		}
	}

	private boolean seek(int framePosition) {
		if (!video.set(Videoio.CV_CAP_PROP_POS_FRAMES, framePosition)) {
			logger.error("Could not seek to frame position: {}", framePosition);
			return false;
		}
		return true;
	}

	/**
	 * Step
	 */
	private void step() {
		if (displayFrame()) {
			increaseDisplayedPosition();
		}
	}

	/**
	 * Display Frame
	 * 
	 * @return True if successful, false otherwise
	 */
	private boolean displayFrame() {
		if (video.read(frame)) {
			Runnable r = () -> {
				videoFrameIcon.setImage(mat2BufferedImage(frame));
				lblVideoFrame.repaint();
			};
			if (EventQueue.isDispatchThread()) {
				r.run();
			} else {
				EventQueue.invokeLater(r);
			}
			return true;
		} else {
			logger.error("Could not read video frame");
			return false;
		}
	}

	private void increaseDisplayedPosition() {
		Runnable r = () -> {
			if (!slider.getValueIsAdjusting()) {
				int pos = slider.getValue() + 1;
				if (pos <= slider.getMaximum()) {
					slider.setValue(pos);
				}
			}
		};
		if (EventQueue.isDispatchThread()) {
			r.run();
		} else {
			EventQueue.invokeLater(r);
		}
	}

	private void updateDisplayedPosition(int framePosition) {
		Runnable r = () -> {
			if (!slider.getValueIsAdjusting()) {
				slider.setValue(framePosition);
			}
		};
		if (EventQueue.isDispatchThread()) {
			r.run();
		} else {
			EventQueue.invokeLater(r);
		}
	}

	private boolean isPlaying() {
		return videoPlayerThread != null && videoPlayerThread.isAlive();
	}

	/**
	 * Mat to BufferedImage
	 * 
	 * @param m Mat
	 * @return BufferedImage
	 */
	private BufferedImage mat2BufferedImage(Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (m.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels() * m.cols() * m.rows();
		byte[] b = new byte[bufferSize];
		m.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;
	}

	/**
	 * Video Player Runnable
	 */
	private class VideoPlayerRunnable implements Runnable {
		/**
		 * Stop Flag
		 */
		private boolean stop = false;

		/**
		 * Milliseconds per Frame
		 */
		private long millisPerFrame;

		/**
		 * Constructor
		 * 
		 * @param fps Frames per Second
		 */
		public VideoPlayerRunnable(double fps) {
			millisPerFrame = (long)(1000 / fps);
		}

		@Override
		public void run() {
			stop = false;
			while (!stop) {
				long startTime = System.nanoTime();
				if (displayFrame()) {
					increaseDisplayedPosition();
				} else {
					videoPlayerRunnable = null;
					break;
				}
				long endTime = System.nanoTime();
				long renderTime = endTime - startTime;
				if (renderTime < 0) {
					continue;
				}
				long sleepDuration = millisPerFrame - TimeUnit.MILLISECONDS.convert(renderTime, TimeUnit.NANOSECONDS);
				if (sleepDuration > 0) {
					try {
						Thread.sleep(sleepDuration);
					} catch (InterruptedException e) {
						logger.error("Sleep was interrupted", e);
					}
				}
			}
		}

		/**
		 * Stop
		 */
		public void stop() {
			stop = true;
		}
	}
}
