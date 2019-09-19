package ch.supertomcat.videopreview.gui;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.supertomcatutils.gui.PositionUtil;
import ch.supertomcat.supertomcatutils.gui.combobox.renderer.FilenameComboBoxRenderer;
import ch.supertomcat.supertomcatutils.gui.dialog.FileDialogUtil;
import ch.supertomcat.supertomcatutils.gui.layout.GridBagLayoutContainer;
import ch.supertomcat.supertomcatutils.gui.table.renderer.DefaultStringColorRowRenderer;
import ch.supertomcat.videopreview.creator.CapUtil;
import ch.supertomcat.videopreview.creator.PreviewCreator;
import ch.supertomcat.videopreview.creator.PreviewCreatorException;
import ch.supertomcat.videopreview.creator.PreviewCreatorOpenCV;
import ch.supertomcat.videopreview.settings.SettingsManager;
import ch.supertomcat.videopreview.settingsconfig.LogLevelSetting;
import ch.supertomcat.videopreview.templates.TemplateManager;

/**
 * Main-Window
 */
public class VideoPreviewWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Caps Row Height
	 */
	private static final int CAPS_ROW_HEIGHT = 50;

	/**
	 * Logger
	 */
	public Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Template Manager
	 */
	private final TemplateManager templateManager;

	/**
	 * Settings Manager
	 */
	private final SettingsManager settingsManager;

	/**
	 * Preview Creator
	 */
	private final PreviewCreator previewCreator;

	/**
	 * Mode Label
	 */
	private JLabel lblMode = new JLabel("Mode:");

	/**
	 * Mode Panel
	 */
	private JPanel pnlMode = new JPanel();

	/**
	 * Single Mode Radio Button
	 */
	private JRadioButton rbSingle = new JRadioButton("Single", true);

	/**
	 * Multi Mode Radio Button
	 */
	private JRadioButton rbMulti = new JRadioButton("Multi", false);

	/**
	 * Recursive CheckBox
	 */
	private JCheckBox cbRecursive = new JCheckBox("Recursive", true);

	/**
	 * Mode Button Group
	 */
	private ButtonGroup bgMode = new ButtonGroup();

	/**
	 * Video Label
	 */
	private JLabel lblVideo = new JLabel("Video:");

	/**
	 * Video TextField
	 */
	private JTextField txtVideo = new JTextField(30);

	/**
	 * Video Select Button
	 */
	private JButton btnVideo = new JButton("...");

	/**
	 * Caps Label
	 */
	private JLabel lblCaps = new JLabel("Caps (0) | Caps % Cols = 0 | RxC = 0x0");

	/**
	 * Caps Config Label
	 */
	private JLabel lblCapsConfig = new JLabel("Caps-Config:");

	/**
	 * Caps Config Panel
	 */
	private JPanel pnlCapsConfig = new JPanel();

	/**
	 * Auto Tile CheckBox
	 */
	private JCheckBox cbAutoTile = new JCheckBox("Auto-Tile", false);

	/**
	 * Auto Caps CheckBox
	 */
	private JCheckBox cbAutoCaps = new JCheckBox("Auto-Caps", true);

	/**
	 * Columns Label
	 */
	private JLabel lblCols = new JLabel("Cols:");

	/**
	 * Rows Label
	 */
	private JLabel lblRows = new JLabel("Rows:");

	/**
	 * Columns TextField
	 */
	private JTextField txtCols = new JTextField("3", 5);

	/**
	 * Rows TextField
	 */
	private JTextField txtRows = new JTextField("4", 5);

	/**
	 * Width Label
	 */
	private JLabel lblWidth = new JLabel("Width:");

	/**
	 * Width TextField
	 */
	private JTextField txtWidth = new JTextField("900", 5);

	/**
	 * Caps Table Model
	 */
	private VideoPreviewTableModel model = new VideoPreviewTableModel();

	/**
	 * Caps Table
	 */
	private JTable jtCaps = new JTable(model);

	/**
	 * Caps ScrollPane
	 */
	private JScrollPane spCaps = new JScrollPane(jtCaps);

	/**
	 * Caps Table Buttons Box Layout
	 */
	private Box box = new Box(BoxLayout.Y_AXIS);

	/**
	 * Caps Select Button
	 */
	private JButton btnCaps = new JButton("...");

	/**
	 * Caps Up Button
	 */
	private JButton btnUp = new JButton("\u25B2");

	/**
	 * Caps Down Button
	 */
	private JButton btnDown = new JButton("\u25BC");

	/**
	 * Caps Delete Button
	 */
	private JButton btnDelete = new JButton("X");

	/**
	 * Panel Template Selection
	 */
	private JPanel pnlTemplates = new JPanel(new SpringLayout());

	/**
	 * Main Template Label
	 */
	private JLabel lblMainTemplate = new JLabel("Main Template");

	/**
	 * Main Template ComboBox
	 */
	private JComboBox<File> cmbMainTemplate = new JComboBox<>();

	/**
	 * Footer Template Label
	 */
	private JLabel lblFooterTemplate = new JLabel("FooterTemplate");

	/**
	 * Footer Template ComboBox
	 */
	private JComboBox<File> cmbFooterTemplate = new JComboBox<>();

	/**
	 * Overall Progress Label
	 */
	private JLabel lblOPG = new JLabel("Overall Progress:");

	/**
	 * Overall ProgressBar
	 */
	private JProgressBar pgO = new JProgressBar();

	/**
	 * Progress Label
	 */
	private JLabel lblPG = new JLabel("Progress:");

	/**
	 * ProgressBar
	 */
	private JProgressBar pg = new JProgressBar();

	/**
	 * Button Panel
	 */
	private JPanel pnlButtons = new JPanel();

	/**
	 * Save Settings Button
	 */
	private JButton btnSaveSettings = new JButton("Save Settings");

	/**
	 * Debug Level ComboBox
	 */
	private JComboBox<LogLevelSetting> cmbDebugLevel = new JComboBox<>();

	/**
	 * Always On Top CheckBox
	 */
	private JCheckBox cbAlwaysOnTop = new JCheckBox("Always on Top");

	/**
	 * Create Button
	 */
	private JButton btnCreate = new JButton("Create");

	/**
	 * Clear Button
	 */
	private JButton btnClear = new JButton("Clear");

	/**
	 * Exit Button
	 */
	private JButton btnExit = new JButton("Exit");

	/**
	 * Constructor
	 * 
	 * @param settingsManager Settings Manager
	 * @param templateManager Template Manager
	 */
	@SuppressWarnings("unchecked")
	public VideoPreviewWindow(SettingsManager settingsManager, TemplateManager templateManager) {
		super("VideoPreview");
		this.templateManager = templateManager;
		this.settingsManager = settingsManager;
		this.previewCreator = new PreviewCreatorOpenCV(templateManager);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jtCaps.getColumn("Cap-Preview").setCellRenderer(new CapPreviewRenderer());
		jtCaps.getColumn("Cap-Preview").setPreferredWidth(120);
		jtCaps.getColumn("Cap-Preview").setMaxWidth(120);
		jtCaps.setRowHeight(CAPS_ROW_HEIGHT);
		jtCaps.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jtCaps.setDefaultRenderer(Object.class, new DefaultStringColorRowRenderer());

		txtVideo.setEditable(false);

		cbRecursive.setEnabled(rbMulti.isSelected());
		rbMulti.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				cbRecursive.setEnabled(rbMulti.isSelected());
			}
		});
		bgMode.add(rbSingle);
		bgMode.add(rbMulti);
		pnlMode.add(rbSingle);
		pnlMode.add(rbMulti);
		pnlMode.add(cbRecursive);

		pnlCapsConfig.add(cbAutoTile);
		pnlCapsConfig.add(cbAutoCaps);
		pnlCapsConfig.add(lblCols);
		pnlCapsConfig.add(txtCols);
		pnlCapsConfig.add(lblRows);
		pnlCapsConfig.add(txtRows);
		pnlCapsConfig.add(lblWidth);
		pnlCapsConfig.add(txtWidth);

		FilenameComboBoxRenderer filenameComboBoxRenderer = new FilenameComboBoxRenderer();

		// Main Template
		cmbMainTemplate.setRenderer(filenameComboBoxRenderer);
		String strSelectedMainTemplate = settingsManager.getGUISettings().getSelectedMainTemplate();
		File selectedMainTemplate = null;
		for (File file : templateManager.getMainTemplateFiles()) {
			cmbMainTemplate.addItem(file);
			if (strSelectedMainTemplate != null && file.getName().equals(strSelectedMainTemplate)) {
				selectedMainTemplate = file;
			}
		}
		if (selectedMainTemplate != null) {
			cmbMainTemplate.setSelectedItem(selectedMainTemplate);
		}

		cmbMainTemplate.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					File selectedMainTemplate = (File)e.getItem();
					if (selectedMainTemplate != null) {
						settingsManager.getGUISettings().setSelectedMainTemplate(selectedMainTemplate.getName());
						settingsManager.writeSettings(true);
					}
				}
			}
		});

		// Footer Template
		cmbFooterTemplate.setRenderer(filenameComboBoxRenderer);
		String strSelectedFooterTemplate = settingsManager.getGUISettings().getSelectedFooterTemplate();
		File selectedFooterTemplate = null;
		for (File file : templateManager.getFooterTemplateFiles()) {
			cmbFooterTemplate.addItem(file);
			if (strSelectedFooterTemplate != null && file.getName().equals(strSelectedFooterTemplate)) {
				selectedFooterTemplate = file;
			}
		}
		if (selectedFooterTemplate != null) {
			cmbFooterTemplate.setSelectedItem(selectedFooterTemplate);
		}

		cmbFooterTemplate.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					File selectedFooterTemplate = (File)e.getItem();
					if (selectedFooterTemplate != null) {
						settingsManager.getGUISettings().setSelectedFooterTemplate(selectedFooterTemplate.getName());
						settingsManager.writeSettings(true);
					}
				}
			}
		});

		pnlTemplates.add(lblMainTemplate);
		pnlTemplates.add(cmbMainTemplate);
		pnlTemplates.add(lblFooterTemplate);
		pnlTemplates.add(cmbFooterTemplate);
		SpringUtilities.makeCompactGrid(pnlTemplates, 2, 2, 5, 5, 5, 5);

		txtCols.setEnabled(!cbAutoTile.isSelected());
		txtRows.setEnabled(!cbAutoTile.isSelected());

		txtCols.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				updateCapsLabel();
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});

		txtRows.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				updateCapsLabel();
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});

		cbAutoTile.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean b = cbAutoTile.isSelected();
				txtCols.setEnabled(!b);
				txtRows.setEnabled(!b);
			}
		});

		cbAutoCaps.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean b = cbAutoCaps.isSelected();
				jtCaps.setEnabled(!b);
				btnDown.setEnabled(!b);
				btnUp.setEnabled(!b);
				btnDelete.setEnabled(!b);
				btnCaps.setEnabled(!b);
				lblCaps.setEnabled(!b);
			}
		});

		cmbDebugLevel.addItem(LogLevelSetting.TRACE);
		cmbDebugLevel.addItem(LogLevelSetting.DEBUG);
		cmbDebugLevel.addItem(LogLevelSetting.INFO);
		cmbDebugLevel.addItem(LogLevelSetting.WARN);
		cmbDebugLevel.addItem(LogLevelSetting.ERROR);
		cmbDebugLevel.addItem(LogLevelSetting.FATAL);
		cmbDebugLevel.setSelectedItem(settingsManager.getSettings().getLogLevel());
		cmbDebugLevel.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				settingsManager.setLogLevel((LogLevelSetting)cmbDebugLevel.getSelectedItem());
			}
		});

		btnVideo.addActionListener(this);
		btnCaps.addActionListener(this);
		btnCreate.addActionListener(this);
		btnSaveSettings.addActionListener(this);
		btnClear.addActionListener(this);
		btnExit.addActionListener(this);
		btnUp.addActionListener(this);
		btnDown.addActionListener(this);
		btnDelete.addActionListener(this);

		cbAlwaysOnTop.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setAlwaysOnTop(cbAlwaysOnTop.isSelected());
			}
		});
		cbAlwaysOnTop.setSelected(false);

		pg.setMinimum(0);
		pg.setMaximum(5);
		pg.setValue(0);

		pnlButtons.add(btnSaveSettings);
		pnlButtons.add(cmbDebugLevel);
		pnlButtons.add(cbAlwaysOnTop);
		pnlButtons.add(btnCreate);
		pnlButtons.add(btnClear);
		pnlButtons.add(btnExit);

		JPanel mainPanel = new JPanel();
		GridBagLayoutContainer gblc = new GridBagLayoutContainer(mainPanel);

		int x = 0;
		gblc.add(lblMode, 0, x, 1, 1, 0.0d, 0.0d);
		gblc.add(pnlMode, 1, x, 1, 1, 0.0d, 0.0d);
		x++;
		gblc.add(lblVideo, 0, x, 1, 1, 0.0d, 0.0d);
		gblc.add(txtVideo, 1, x, 1, 1, 0.0d, 0.0d);
		gblc.add(btnVideo, 2, x, 1, 1, 0.0d, 0.0d);
		x++;
		gblc.add(lblCapsConfig, 0, x, 1, 1, 0.0d, 0.0d);
		gblc.add(pnlCapsConfig, 1, x, 1, 1, 0.0d, 0.0d);
		x++;
		gblc.add(lblCaps, 0, x, 3, 1, 0.0d, 0.0d);
		x++;
		box.add(btnCaps);
		box.add(btnUp);
		box.add(btnDown);
		box.add(btnDelete);
		gblc.add(spCaps, 0, x, 2, 1, 0.0d, 0.3d);
		gblc.add(box, 2, x, 1, 1, 0.0d, 0.0d);
		x++;
		gblc.add(pnlTemplates, 0, x, 3, 1, 1.0d, 0.0d);
		x++;
		gblc.add(lblOPG, 0, x, 1, 1, 0.0d, 0.0d);
		gblc.add(pgO, 1, x, 2, 1, 0.6d, 0.0d);
		x++;
		gblc.add(lblPG, 0, x, 1, 1, 0.0d, 0.0d);
		gblc.add(pg, 1, x, 2, 1, 0.6d, 0.0d);
		x++;
		gblc.add(pnlButtons, 0, x, 3, 1, 1.0d, 0.0d);

		getContentPane().add(mainPanel);

		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				updateCapsLabel();
			}
		});

		new DropTarget(spCaps, new CapDropTargetListener(model, this));

		new DropTarget(txtVideo, new VideoDropTargetListener(txtVideo));

		pack();

		PositionUtil.setPositionMiddleScreen(this, null);

		setVisible(true);
	}

	/**
	 * @param value
	 */
	public synchronized void updatePG(final int value) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				pg.setValue(value);
			}
		});
	}

	/**
	 * @param text
	 */
	public synchronized void updatePG(final String text) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				pg.setStringPainted(true);
				pg.setString(text);
			}
		});
	}

	/**
	 * @param min
	 */
	public synchronized void setPGMin(final int min) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				pg.setMinimum(min);
			}
		});
	}

	/**
	 * @param max
	 */
	public synchronized void setPGMax(final int max) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				pg.setMaximum(max);
			}
		});
	}

	/**
	 * @param value
	 */
	public synchronized void updateOPG(final int value) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				int val = pgO.getValue() + 1;
				if (value == -1 && val <= pgO.getMaximum()) {
					pgO.setValue(pgO.getValue() + 1);
					pgO.setStringPainted(true);
					String text = pgO.getValue() + " / " + pgO.getMaximum();
					pgO.setString(text);
					VideoPreviewWindow.this.setTitle("VideoPreview (" + text + ")");
				} else {
					pgO.setValue(value);
					pgO.setStringPainted(true);
					String text = value + " / " + pgO.getMaximum();
					pgO.setString(text);
					VideoPreviewWindow.this.setTitle("VideoPreview (" + text + ")");
				}
			}
		});
	}

	/**
	 * @param text
	 */
	public synchronized void updateOPG(final String text) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				pgO.setStringPainted(true);
				pgO.setString(text);
			}
		});
	}

	/**
	 * @param min
	 */
	public synchronized void setOPGMin(final int min) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				pgO.setMinimum(min);
			}
		});
	}

	/**
	 * @param max
	 */
	public synchronized void setOPGMax(final int max) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				pgO.setMaximum(max);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnExit) {
			this.dispose();
		} else if (e.getSource() == btnCreate) {
			if (txtVideo.getText().length() == 0) {
				updateOPG("No Video or Path selected");
				return;
			}
			File fVideo = new File(txtVideo.getText());
			if (fVideo.exists() == false) {
				fVideo = null;
				updateOPG("Video or Path does not exist");
				return;
			} else {
				if (rbSingle.isSelected()) {
					if (fVideo.isFile() == false) {
						fVideo = null;
						updateOPG("Selected Video ist not a file");
						return;
					}
				} else {
					if (fVideo.isDirectory() == false) {
						fVideo = null;
						updateOPG("Selected path is not a directory");
						return;
					}
				}
			}
			fVideo = null;
			if (cbAutoCaps.isSelected() == false && jtCaps.getRowCount() == 0) {
				int choice = JOptionPane.showConfirmDialog(this, "No Caps in Cap-Table, abort?", "No Caps Found", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (choice == JOptionPane.YES_OPTION) {
					updateOPG("No Caps in Cap-Table");
					return;
				}
			}

			final String videoFile = txtVideo.getText();
			String capsa[] = new String[jtCaps.getRowCount()];
			for (int i = 0; i < jtCaps.getRowCount(); i++) {
				capsa[i] = (String)jtCaps.getValueAt(i, 1);
			}

			final int rows;
			final int cols;
			final int width;
			try {
				width = Integer.parseInt(txtWidth.getText());
			} catch (NumberFormatException nfe) {
				updateOPG("Width is not a number");
				return;
			}
			try {
				rows = Integer.parseInt(txtRows.getText());
			} catch (NumberFormatException nfe) {
				updateOPG("Rows is not a number");
				return;
			}
			try {
				cols = Integer.parseInt(txtCols.getText());
			} catch (NumberFormatException nfe) {
				updateOPG("Columns is not a number");
				return;
			}

			final boolean autoTile = cbAutoTile.isSelected();
			final boolean autoCaps = cbAutoCaps.isSelected();
			final String capsarr[] = capsa;

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					lockGUI(true);

					File selectedMainTemplate = (File)cmbMainTemplate.getSelectedItem();
					if (selectedMainTemplate == null) {
						return;
					}
					File selectedFooterTemplate = (File)cmbFooterTemplate.getSelectedItem();
					if (selectedFooterTemplate == null) {
						return;
					}

					File folder = new File(videoFile);
					updateOPG("Searching for Video-Files");
					Vector<String> videos = null;
					if (rbSingle.isSelected()) {
						videos = new Vector<String>();
						videos.add(videoFile);
					} else {
						// TODO createVideoList
						videos = new Vector<>();
					}

					if (videos.size() > 0) {
						setOPGMin(0);
						setOPGMax(videos.size());
						updateOPG(0);

						List<File> videoFiles = videos.stream().map(x -> new File(x)).collect(Collectors.toList());
						List<File> capFiles = Arrays.stream(capsarr).map(x -> new File(x)).collect(Collectors.toList());

						try {
							previewCreator.createPreviews(videoFiles, width, autoTile, rows, cols, autoCaps, capFiles, selectedMainTemplate, selectedFooterTemplate);
						} catch (PreviewCreatorException e) {
							logger.error("Could not create previews", e);
						}
					} else {
						updateOPG("No Videos found in folder");
					}
					lockGUI(false);
				}
			});
			t.setName("MultiPreviewCreateThread");
			t.start();
		} else if (e.getSource() == btnVideo) {
			if (rbSingle.isSelected()) {
				File file = FileDialogUtil.showFileOpenDialog(this, settingsManager.getDirectorySettings().getLastUsedPath(), null);
				if (file != null) {
					txtVideo.setText(file.getAbsolutePath());
				}
			} else {
				File folder = FileDialogUtil.showFolderOpenDialog(this, settingsManager.getDirectorySettings().getLastUsedPath(), null);
				if (folder != null) {
					txtVideo.setText(folder.getAbsolutePath());
				}
			}
		} else if (e.getSource() == btnCaps) {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					lockGUI(true);
					File files[] = FileDialogUtil.showMultiFileOpenDialog(VideoPreviewWindow.this, settingsManager.getDirectorySettings().getLastUsedPath(), null);
					if (files != null) {
						pg.setMinimum(0);
						pg.setMaximum(files.length - 1);
						pg.setValue(0);
						for (int i = 0; i < files.length; i++) {
							Image img = CapUtil.getCapPreview(files[i], CAPS_ROW_HEIGHT);
							model.addRow(files[i].getAbsolutePath(), img);
							updatePG(i);
						}
					}
					lockGUI(false);
				}
			});
			t.setPriority(Thread.MIN_PRIORITY);
			t.start();
		} else if (e.getSource() == btnUp) {

			int row = jtCaps.getSelectedRow();
			if ((row > 0) && (row < jtCaps.getRowCount())) {
				model.moveRow(row, row, row - 1);
				jtCaps.setRowSelectionInterval(row - 1, row - 1);
			}
		} else if (e.getSource() == btnDown) {
			int row = jtCaps.getSelectedRow();
			if ((row > -1) && (row < (jtCaps.getRowCount() - 1))) {
				model.moveRow(row, row, row + 1);
				jtCaps.setRowSelectionInterval(row + 1, row + 1);
			}
		} else if (e.getSource() == btnDelete) {
			int row = jtCaps.getSelectedRow();
			if ((row > -1) && (row < jtCaps.getRowCount())) {
				model.removeRow(row);
				if (row < jtCaps.getRowCount()) {
					jtCaps.setRowSelectionInterval(row, row);
				} else {
					if ((row - 1) > -1) {
						jtCaps.setRowSelectionInterval(row - 1, row - 1);
					}
				}
			}
		} else if (e.getSource() == btnClear) {
			txtVideo.setText("");
			for (int i = jtCaps.getRowCount() - 1; i > -1; i--) {
				model.removeRow(i);
			}
		} else if (e.getSource() == btnSaveSettings) {
			// TODO Save settings
		}
	}

	/**
	 * @param b
	 */
	public void lockGUI(final boolean b) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				boolean bAutoCaps = !b;
				if (bAutoCaps && cbAutoCaps.isSelected()) {
					bAutoCaps = false;
				}
				btnCaps.setEnabled(bAutoCaps);
				btnDelete.setEnabled(bAutoCaps);
				btnDown.setEnabled(bAutoCaps);
				btnUp.setEnabled(bAutoCaps);

				rbSingle.setEnabled(!b);
				rbMulti.setEnabled(!b);
				cbAutoCaps.setEnabled(!b);
				cbAutoTile.setEnabled(!b);
				txtCols.setEnabled(!b);
				txtRows.setEnabled(!b);
				txtWidth.setEnabled(!b);

				btnClear.setEnabled(!b);
				btnCreate.setEnabled(!b);
				btnExit.setEnabled(!b);
				btnVideo.setEnabled(!b);
				jtCaps.setEnabled(!b);
			}
		});
	}

	private void updateCapsLabel() {
		int cols;
		try {
			cols = Integer.parseInt(txtCols.getText());
		} catch (NumberFormatException nfe) {
			cols = 1;
		}
		int caps = jtCaps.getRowCount();

		int mod = caps % cols;

		int calculatedRows = caps / cols;

		if (mod != 0) {
			calculatedRows++;
		}

		lblCaps.setText("Caps (" + caps + ") | Caps % Cols = " + caps % cols + " | RxC = " + calculatedRows + "x" + cols);
	}
}
