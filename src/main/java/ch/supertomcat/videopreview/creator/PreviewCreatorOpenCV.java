package ch.supertomcat.videopreview.creator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import ch.supertomcat.videopreview.mediainfo.MediaInfoDataProvider;
import ch.supertomcat.videopreview.templates.TemplateManager;

/**
 * Preview Creator which uses OpenCV
 */
public class PreviewCreatorOpenCV implements PreviewCreator {
	/**
	 * New Line Pattern
	 */
	private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\r?\n|\r");

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Template Manager
	 */
	private final TemplateManager templateManager;

	/**
	 * Constructor
	 * 
	 * @param templateManager Template Manager
	 */
	public PreviewCreatorOpenCV(TemplateManager templateManager) {
		this.templateManager = templateManager;
	}

	@Override
	public void createPreviews(List<File> files, int width, boolean autoTile, int rows, int columns, boolean autoCaps, List<File> caps, File mainTemplate,
			File footerTemplate) throws PreviewCreatorException {
		for (File file : files) {
			createPreview(file, width, autoTile, rows, columns, autoCaps, caps, mainTemplate, footerTemplate);
		}
	}

	@Override
	public void createPreview(File file, int width, boolean autoTile, int rows, int columns, boolean autoCaps, List<File> caps, File mainTemplate, File footerTemplate) throws PreviewCreatorException {
		try {
			int infoTextPadding = 10;
			int infoTextLinePadding = 20;
			int fontFace = Core.FONT_HERSHEY_SIMPLEX;
			double fontScale = 0.5;
			int fontThickness = 1;

			String infoText = generateInfoText(file, mainTemplate, null);
			// TODO Handle image type
			BufferedImage infoTextBufferedImage = createInfoTextImage(infoText, width, 10, 5);
			Mat infoTextImage = bufferedImageToMat(infoTextBufferedImage);
			infoTextBufferedImage.flush();
			int infoTextHeight = infoTextImage.height();

			String footerText = generateInfoText(file, footerTemplate, "footers");
			BufferedImage footerTextBufferedImage = createInfoTextImage(footerText, width, 10, 5);
			Mat footerTextImage = bufferedImageToMat(footerTextBufferedImage);
			footerTextBufferedImage.flush();
			int footerTextHeight = footerTextImage.height();

			// TODO Remove if not needed anymore
			// String[] infoTextLines = NEW_LINE_PATTERN.split(infoText);
			// int infoTextHeight = 0;
			// List<Size> infoTextLineSizes = new ArrayList<>();
			// for (String infoTextLine : infoTextLines) {
			// Size infoTextSize = Imgproc.getTextSize(infoTextLine, fontFace, fontScale, fontThickness, new int[] { 0 });
			// infoTextLineSizes.add(infoTextSize);
			// infoTextHeight += (int)infoTextSize.height;
			// }
			// infoTextHeight += (infoTextLines.length - 1) * infoTextLinePadding;

			int capPadding = 10;
			int infoHeight = infoTextHeight;
			int height = infoHeight + capPadding + footerTextHeight;
			Mat videoPreviewImage;
			if (autoCaps) {
				int frameWidth = (width - (columns + 1) * capPadding) / columns;
				int frameHeight = 0;
				int frameType = CvType.CV_8UC3;
				int frameChannels = 3;
				VideoCapture video = null;
				try {
					video = new VideoCapture(file.getAbsolutePath());
					if (!video.isOpened()) {
						logger.error("Could not open file: {}", file);
						// TODO Throw exception
						return;
					}

					int capCount = rows * columns;

					int frameCount = (int)video.get(Videoio.CV_CAP_PROP_FRAME_COUNT);
					if (frameCount <= 0) {
						// TODO Handle this case somehow
						logger.error("Could not get frame count from video");
					}
					int posIncrease = frameCount / capCount;

					List<Mat> frames = new ArrayList<>();
					for (int i = 0; i < capCount; i++) {
						// Seek to position
						int framePosition = i * posIncrease;
						if (!video.set(Videoio.CV_CAP_PROP_POS_FRAMES, framePosition)) {
							logger.error("Could not seek to frame position: {}", framePosition);
						}
						Mat frame = new Mat();
						if (video.read(frame)) {
							Mat resizedFrame;
							double scaleFactor = (double)frameWidth / frame.width();
							if (scaleFactor != 0.0d) {
								int interpolation = frame.width() > frameWidth ? Imgproc.INTER_AREA : Imgproc.INTER_CUBIC;
								resizedFrame = new Mat();
								// Zero means it will be calculated using scale factor
								Size sz = new Size(0, 0);
								Imgproc.resize(frame, resizedFrame, sz, scaleFactor, scaleFactor, interpolation);
							} else {
								resizedFrame = frame;
							}

							if (frameHeight <= 0) {
								frameHeight = resizedFrame.height();
								frameType = resizedFrame.type();
								frameChannels = resizedFrame.channels();
							}

							frames.add(resizedFrame);
						} else {
							logger.error("Could not read frame");
						}
					}

					height += rows * (frameHeight + capPadding);

					// TODO Configurable
					Scalar backgroundColor = createScalar(Color.WHITE, frameChannels);

					videoPreviewImage = new Mat(height, width, frameType, backgroundColor);

					// JFrame debugWindow = new JFrame();
					// debugWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					// JLabel lblImage = new JLabel();
					// debugWindow.add(lblImage);
					// debugWindow.pack();
					// debugWindow.setVisible(true);

					int currentRow = 0;
					int currentColumn = 0;
					int capsStartHeight = infoHeight + capPadding;
					for (Mat frame : frames) {
						int startRow = capsStartHeight + currentRow * (frameHeight + capPadding);
						int endRow = startRow + frame.height();
						int startCol = currentColumn * (frameWidth + capPadding) + capPadding;
						int endCol = startCol + frame.width();

						Mat convertedFrame = new Mat();
						frame.convertTo(convertedFrame, videoPreviewImage.type());
						convertedFrame.copyTo(videoPreviewImage.rowRange(startRow, endRow).colRange(startCol, endCol));

						// lblImage.setIcon(new ImageIcon(mat2BufferedImage(convertedFrame)));

						currentColumn++;
						if (currentColumn >= columns) {
							currentColumn = 0;
							currentRow++;
						}
					}
				} finally {
					if (video != null) {
						video.release();
					}
				}
			} else {
				// TODO non auto caps
				int frameHeight = 0;
				height += rows * (frameHeight + capPadding);

				videoPreviewImage = new Mat(height, width, CvType.CV_8UC3, new Scalar(255, 255, 255));
			}

			// Scalar infoBackgroundColor = createScalar(Color.decode("#ffe0ae"), videoPreviewImage.channels());
			// Scalar infoForegroundColor = createScalar(Color.BLACK, videoPreviewImage.channels());
			//
			// Mat infoImage = new Mat(infoHeight, width, videoPreviewImage.type(), infoBackgroundColor);
			// double textHeightPoint = infoTextPadding;
			// int i = 0;
			// for (String infoTextLine : infoTextLines) {
			// Size infoTextLineSize = infoTextLineSizes.get(i);
			// textHeightPoint += infoTextLineSize.height;
			// Imgproc.putText(infoImage, infoTextLine, new Point(infoTextPadding, textHeightPoint), fontFace, fontScale, infoForegroundColor, fontThickness);
			// textHeightPoint += infoTextLinePadding;
			// i++;
			// }

			infoTextImage.copyTo(videoPreviewImage.rowRange(0, infoTextImage.height()).colRange(0, infoTextImage.width()));

			int footerRowStart = videoPreviewImage.height() - footerTextImage.height();
			footerTextImage.copyTo(videoPreviewImage.rowRange(footerRowStart, footerRowStart + footerTextImage.height()).colRange(0, footerTextImage.width()));

			Imgcodecs.imwrite(file.getAbsolutePath() + ".jpg", videoPreviewImage);

			videoPreviewImage.release();
		} catch (Exception e) {
			// TODO Message
			throw new PreviewCreatorException(e);
		}
	}

	public BufferedImage mat2BufferedImage(Mat m) {
		// Fastest code
		// output can be assigned either to a BufferedImage or to an Image

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

	public Mat bufferedImageToMat(BufferedImage img) {
		if (img.getType() != BufferedImage.TYPE_3BYTE_BGR) {
			throw new IllegalArgumentException("BufferedImage type is not supported: " + img.getType());
		}

		Mat mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}

	private Scalar createScalar(Color color, int channels) {
		switch (channels) {
			case 3:
				// BGR
				return new Scalar(color.getBlue(), color.getGreen(), color.getRed());
			case 4:
				// BGRA
				return new Scalar(color.getBlue(), color.getGreen(), color.getRed(), color.getAlpha());
			case 1:
			case 2:
			default:
				throw new IllegalArgumentException("Number of channels is not supported: " + channels);
		}
	}

	private String generateInfoText(File file, File template, String templateSubFolder) {
		try (MediaInfoDataProvider mediaInfoDataProvider = new MediaInfoDataProvider(file)) {
			Map<String, Object> vars = new LinkedHashMap<>();
			vars.put("mediaInfo", mediaInfoDataProvider);
			vars.put("escapeTool", new EscapeTool());
			vars.put("dateTool", new DateTool());
			vars.put("applicationName", ApplicationProperties.getProperty("ApplicationName"));
			vars.put("applicationShortName", ApplicationProperties.getProperty("ApplicationShortName"));
			vars.put("applicationVersion", ApplicationProperties.getProperty("ApplicationVersion"));

			long start = System.nanoTime();
			String templateName;
			if (templateSubFolder != null) {
				templateName = templateSubFolder + "/" + template.getName();
			} else {
				templateName = template.getName();
			}

			String renderedTemplate = templateManager.renderTemplate(templateName, vars);
			long end = System.nanoTime();
			long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
			logger.debug("Rendered Template {} in {}ms:\n{}", template, duration, renderedTemplate);
			return renderedTemplate;
		} catch (Exception e) {
			logger.error("Could not render template: {}", template.getAbsolutePath(), e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String stackTrace = sw.toString();
			return stackTrace;
		} finally {
			System.gc();
		}
	}

	private BufferedImage createInfoTextImage(String infoText, int width, int paddingX, int paddingY) {
		String[] lines = NEW_LINE_PATTERN.split(infoText);

		Color backgroundColor = Color.decode("#ffe0ae");
		Font font = new Font("Tahoma", Font.PLAIN, 13);

		BufferedImage fontMetricsImage = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
		Graphics fontMetricsGraphics = fontMetricsImage.getGraphics();
		FontMetrics fontMetrics = fontMetricsGraphics.getFontMetrics(font);
		int fontHeight = fontMetrics.getHeight();
		int fontDescent = fontMetrics.getDescent();
		int height = lines.length * fontHeight + 2 * paddingY;
		fontMetricsImage.flush();

		BufferedImage infoTextImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics graphics = infoTextImage.getGraphics();

		graphics.setColor(backgroundColor);
		graphics.fillRect(0, 0, width, height);

		graphics.setColor(Color.BLACK);
		graphics.setFont(font);
		int y = paddingY + fontHeight - fontDescent;
		for (String line : lines) {
			graphics.drawString(line, paddingX, y);
			y += fontHeight;
		}
		graphics.dispose();
		return infoTextImage;
	}
}
