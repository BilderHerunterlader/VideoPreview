package ch.supertomcat.videopreview.gui;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.supertomcatutils.gui.PositionUtil;
import ch.supertomcat.supertomcatutils.gui.combobox.renderer.FilenameComboBoxRenderer;
import ch.supertomcat.supertomcatutils.gui.dialog.FileDialogUtil;
import ch.supertomcat.supertomcatutils.gui.layout.GridBagLayoutContainer;
import ch.supertomcat.supertomcatutils.gui.progress.IProgressObserver;
import ch.supertomcat.supertomcatutils.gui.progress.ProgressObserver;
import ch.supertomcat.supertomcatutils.gui.table.renderer.DefaultStringColorRowRenderer;
import ch.supertomcat.videopreview.creator.PreviewCreator;
import ch.supertomcat.videopreview.creator.PreviewCreatorException;
import ch.supertomcat.videopreview.creator.PreviewCreatorOpenCV;
import ch.supertomcat.videopreview.settings.SettingsManager;
import ch.supertomcat.videopreview.settingsconfig.LogLevelSetting;
import ch.supertomcat.videopreview.templates.TemplateManager;
import ch.supertomcat.videopreview.util.CapUtil;

/**
 * Main-Window
 */
public class VideoPreviewWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Pattern for video files
	 * TODO Update pattern include missing formats if there are any
	 */
	private static final Pattern VIDEO_FILE_PATTERN = Pattern.compile("(?i).*?\\.(avi|mpg|mpeg|mkv|rm|wmv|flv|mp4|asf|mov|ts|m2ts)$");

	/**
	 * FileFilter for video files
	 */
	private static final FileFilter VIDEO_FILE_FILTER = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname.isFile() && VIDEO_FILE_PATTERN.matcher(pathname.getName()).matches();
		}
	};

	/**
	 * FileFilter for directories
	 */
	private static final FileFilter DIRECTORY_FILE_FILTER = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	};

	/**
	 * Caps Row Height
	 */
	private static final int CAPS_ROW_HEIGHT = 50;

	/**
	 * Logger
	 */
	public Logger logger = LoggerFactory.getLogger(getClass());

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
	private JButton btnCapsSelect = new JButton("...");

	/**
	 * Caps Up Button
	 */
	private JButton btnCapsUp = new JButton("\u25B2");

	/**
	 * Caps Down Button
	 */
	private JButton btnCapsDown = new JButton("\u25BC");

	/**
	 * Caps Delete Button
	 */
	private JButton btnCapsRemove = new JButton("X");

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
	 * Cap File Column Model Index
	 */
	private final int capFileColumnModelIndex = jtCaps.getColumn("Cap-File").getModelIndex();

	/**
	 * Constructor
	 * 
	 * @param settingsManager Settings Manager
	 * @param templateManager Template Manager
	 */
	@SuppressWarnings("unchecked")
	public VideoPreviewWindow(SettingsManager settingsManager, TemplateManager templateManager) {
		super("VideoPreview");
		this.settingsManager = settingsManager;
		this.previewCreator = new PreviewCreatorOpenCV(templateManager);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

		txtVideo.setEditable(false);
		new DropTarget(txtVideo, new VideoDropTargetListener(txtVideo, rbMulti));
		btnVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File selectedPath;
				if (rbSingle.isSelected()) {
					selectedPath = FileDialogUtil.showFileOpenDialog(VideoPreviewWindow.this, settingsManager.getDirectorySettings().getLastUsedPath(), null);
				} else {
					selectedPath = FileDialogUtil.showFolderOpenDialog(VideoPreviewWindow.this, settingsManager.getDirectorySettings().getLastUsedPath(), null);
				}
				if (selectedPath != null) {
					txtVideo.setText(selectedPath.getAbsolutePath());
				}
			}
		});

		pnlCapsConfig.add(cbAutoTile);
		pnlCapsConfig.add(cbAutoCaps);
		pnlCapsConfig.add(lblCols);
		pnlCapsConfig.add(txtCols);
		pnlCapsConfig.add(lblRows);
		pnlCapsConfig.add(txtRows);
		pnlCapsConfig.add(lblWidth);
		pnlCapsConfig.add(txtWidth);

		cbAutoTile.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean selected = cbAutoTile.isSelected();
				txtCols.setEnabled(!selected);
				txtRows.setEnabled(!selected);
			}
		});

		cbAutoCaps.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean selected = cbAutoCaps.isSelected();
				jtCaps.setEnabled(!selected);
				btnCapsDown.setEnabled(!selected);
				btnCapsUp.setEnabled(!selected);
				btnCapsRemove.setEnabled(!selected);
				btnCapsSelect.setEnabled(!selected);
				lblCaps.setEnabled(!selected);
			}
		});

		txtCols.setEnabled(!cbAutoTile.isSelected());
		txtCols.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateCapsLabel();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateCapsLabel();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateCapsLabel();
			}
		});

		txtRows.setEnabled(!cbAutoTile.isSelected());
		txtRows.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateCapsLabel();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateCapsLabel();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateCapsLabel();
			}
		});

		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				updateCapsLabel();
			}
		});

		jtCaps.getColumn("Cap-Preview").setCellRenderer(new CapPreviewRenderer());
		jtCaps.getColumn("Cap-Preview").setPreferredWidth(120);
		jtCaps.getColumn("Cap-Preview").setMaxWidth(120);
		jtCaps.setRowHeight(CAPS_ROW_HEIGHT);
		jtCaps.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jtCaps.setDefaultRenderer(Object.class, new DefaultStringColorRowRenderer());

		ProgressObserver progressObserver = new ProgressObserver();
		progressObserver.addProgressListener(new IProgressObserver() {
			@Override
			public void progressModeChanged(boolean indeterminate) {
				if (EventQueue.isDispatchThread()) {
					pg.setIndeterminate(indeterminate);
				} else {
					EventQueue.invokeLater(() -> pg.setIndeterminate(indeterminate));
				}
			}

			@Override
			public void progressIncreased() {
				if (EventQueue.isDispatchThread()) {
					pg.setValue(pg.getValue() + 1);
				} else {
					EventQueue.invokeLater(() -> pg.setValue(pg.getValue() + 1));
				}
			}

			@Override
			public void progressCompleted() {
				if (EventQueue.isDispatchThread()) {
					pg.setValue(pg.getMaximum());
				} else {
					EventQueue.invokeLater(() -> pg.setValue(pg.getMaximum()));
				}
			}

			@Override
			public void progressChanged(int min, int max, int val) {
				setPGMin(min);
				setPGMax(max);
				updatePG(val);
			}

			@Override
			public void progressChanged(boolean visible) {
				if (EventQueue.isDispatchThread()) {
					pg.setVisible(visible);
				} else {
					EventQueue.invokeLater(() -> pg.setVisible(visible));
				}
			}

			@Override
			public void progressChanged(String text) {
				updatePG(text);
			}

			@Override
			public void progressChanged(int val) {
				updatePG(val);
			}
		});
		VideoPreviewWindowObserver mainWindowObserver = new VideoPreviewWindowObserver();
		mainWindowObserver.addProgressListener(new VideoPreviewWindowListener() {
			@Override
			public void unlockGUI() {
				VideoPreviewWindow.this.unlockGUI();
			}

			@Override
			public void lockGUI() {
				VideoPreviewWindow.this.lockGUI();
			}
		});
		new DropTarget(spCaps, new CapDropTargetListener(model, progressObserver, mainWindowObserver, CAPS_ROW_HEIGHT));

		box.add(btnCapsSelect);
		box.add(btnCapsUp);
		box.add(btnCapsDown);
		box.add(btnCapsRemove);

		btnCapsSelect.addActionListener(e -> actionCapsSelect());
		btnCapsUp.addActionListener(e -> actionCapsUp());
		btnCapsDown.addActionListener(e -> actionCapsDown());
		btnCapsRemove.addActionListener(e -> actionCapsRemove());

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

		pg.setStringPainted(true);
		pgO.setStringPainted(true);

		btnSaveSettings.addActionListener(e -> {
			// TODO Save Settings
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

		cbAlwaysOnTop.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setAlwaysOnTop(cbAlwaysOnTop.isSelected());
			}
		});
		cbAlwaysOnTop.setSelected(false);

		btnCreate.addActionListener(e -> actionCreate());
		btnClear.addActionListener(e -> {
			txtVideo.setText("");
			for (int i = jtCaps.getRowCount() - 1; i > -1; i--) {
				model.removeRow(i);
			}
		});
		btnExit.addActionListener(e -> dispose());

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

		add(mainPanel);

		pack();
		PositionUtil.setPositionMiddleScreen(this, null);
		setVisible(true);
	}

	/**
	 * Update Progress Value
	 * 
	 * @param value Value
	 */
	private void updatePG(final int value) {
		if (EventQueue.isDispatchThread()) {
			pg.setValue(value);
		} else {
			EventQueue.invokeLater(() -> pg.setValue(value));
		}
	}

	/**
	 * Update Progress Text
	 * 
	 * @param text Text
	 */
	private void updatePG(final String text) {
		if (EventQueue.isDispatchThread()) {
			pg.setString(text);
		} else {
			EventQueue.invokeLater(() -> pg.setString(text));
		}
	}

	/**
	 * Update Progress Minimum
	 * 
	 * @param min Minimum
	 */
	private void setPGMin(final int min) {
		if (EventQueue.isDispatchThread()) {
			pg.setMinimum(min);
		} else {
			EventQueue.invokeLater(() -> pg.setMinimum(min));
		}
	}

	/**
	 * Update Progress Maximum
	 * 
	 * @param max Maximum
	 */
	private void setPGMax(final int max) {
		if (EventQueue.isDispatchThread()) {
			pg.setMaximum(max);
		} else {
			EventQueue.invokeLater(() -> pg.setMaximum(max));
		}
	}

	/**
	 * Update Overall Progress Value
	 * 
	 * @param value Value
	 */
	private void updateOPG(final int value) {
		if (EventQueue.isDispatchThread()) {
			pgO.setValue(value);
			pgO.setString(value + " / " + pgO.getMaximum());
		} else {
			EventQueue.invokeLater(() -> {
				pgO.setValue(value);
				pgO.setString(value + " / " + pgO.getMaximum());
			});
		}
	}

	/**
	 * Update Overall Progress Text
	 * 
	 * @param text Text
	 */
	private void updateOPG(final String text) {
		if (EventQueue.isDispatchThread()) {
			pgO.setString(text);
		} else {
			EventQueue.invokeLater(() -> pgO.setString(text));
		}
	}

	/**
	 * Update Overall Progress Minimum
	 * 
	 * @param min Minimum
	 */
	private void setOPGMin(final int min) {
		if (EventQueue.isDispatchThread()) {
			pgO.setMinimum(min);
		} else {
			EventQueue.invokeLater(() -> pgO.setMinimum(min));
		}
	}

	/**
	 * Update Overall Progress Maximum
	 * 
	 * @param max Maximum
	 */
	private void setOPGMax(final int max) {
		if (EventQueue.isDispatchThread()) {
			pgO.setMaximum(max);
		} else {
			EventQueue.invokeLater(() -> pgO.setMaximum(max));
		}
	}

	/**
	 * Lock GUI
	 */
	private void lockGUI() {
		lockGUI(true);
	}

	/**
	 * Unlock GUI
	 */
	private void unlockGUI() {
		lockGUI(false);
	}

	/**
	 * Lock or Unlock GUI
	 * 
	 * @param lock True if GUI should be locked, false otherwise
	 */
	private void lockGUI(final boolean lock) {
		Runnable lockGUIRunnable = () -> {
			boolean bAutoCaps = !lock;
			if (bAutoCaps && cbAutoCaps.isSelected()) {
				bAutoCaps = false;
			}
			btnCapsSelect.setEnabled(bAutoCaps);
			btnCapsRemove.setEnabled(bAutoCaps);
			btnCapsDown.setEnabled(bAutoCaps);
			btnCapsUp.setEnabled(bAutoCaps);

			rbSingle.setEnabled(!lock);
			rbMulti.setEnabled(!lock);
			cbAutoCaps.setEnabled(!lock);
			cbAutoTile.setEnabled(!lock);
			txtCols.setEnabled(!lock);
			txtRows.setEnabled(!lock);
			txtWidth.setEnabled(!lock);

			btnClear.setEnabled(!lock);
			btnCreate.setEnabled(!lock);
			btnExit.setEnabled(!lock);
			btnVideo.setEnabled(!lock);
			jtCaps.setEnabled(!lock);
		};
		if (EventQueue.isDispatchThread()) {
			lockGUIRunnable.run();
		} else {
			EventQueue.invokeLater(lockGUIRunnable);
		}
	}

	private void actionCreate() {
		if (txtVideo.getText().isEmpty()) {
			updateOPG("No Video or Folder selected");
			return;
		}

		File fVideo = new File(txtVideo.getText());
		if (!fVideo.exists()) {
			updateOPG("Video or Folder does not exist");
			return;
		}

		if (rbSingle.isSelected()) {
			if (!fVideo.isFile()) {
				updateOPG("Selected Video ist not a file");
				return;
			}
		} else {
			if (!fVideo.isDirectory()) {
				updateOPG("Selected path is not a directory");
				return;
			}
		}

		if (!cbAutoCaps.isSelected() && jtCaps.getRowCount() == 0) {
			int choice = JOptionPane.showConfirmDialog(this, "No Caps in Cap-Table, abort?", "No Caps Found", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (choice == JOptionPane.YES_OPTION) {
				updateOPG("No Caps in Cap-Table");
				return;
			}
		}

		File selectedMainTemplate = (File)cmbMainTemplate.getSelectedItem();
		if (selectedMainTemplate == null) {
			updateOPG("No template selected");
			return;
		}

		File selectedFooterTemplate = (File)cmbFooterTemplate.getSelectedItem();
		if (selectedFooterTemplate == null) {
			updateOPG("No footer template selected");
			return;
		}

		int width;
		try {
			width = Integer.parseInt(txtWidth.getText());
		} catch (NumberFormatException nfe) {
			updateOPG("Width is not a number");
			return;
		}

		int rows;
		try {
			rows = Integer.parseInt(txtRows.getText());
		} catch (NumberFormatException nfe) {
			updateOPG("Rows is not a number");
			return;
		}

		int cols;
		try {
			cols = Integer.parseInt(txtCols.getText());
		} catch (NumberFormatException nfe) {
			updateOPG("Columns is not a number");
			return;
		}

		boolean autoTile = cbAutoTile.isSelected();
		boolean autoCaps = cbAutoCaps.isSelected();
		boolean recursive = cbRecursive.isSelected();

		List<File> capFiles = new ArrayList<>();
		for (int i = 0; i < jtCaps.getRowCount(); i++) {
			String capFilePath = (String)model.getValueAt(jtCaps.convertRowIndexToModel(i), capFileColumnModelIndex);
			capFiles.add(new File(capFilePath));
		}

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lockGUI();

					List<File> videoFiles;
					if (rbSingle.isSelected()) {
						videoFiles = Arrays.asList(fVideo);
					} else {
						updateOPG("Searching for Video-Files");
						videoFiles = createVideoList(fVideo, recursive);
						if (videoFiles.isEmpty()) {
							updateOPG("No Videos found in folder");
						}
					}

					setOPGMin(0);
					setOPGMax(videoFiles.size());
					updateOPG(0);

					int i = 0;
					for (File videoFile : videoFiles) {
						updateOPG("Create preview for file: " + videoFile.getName());
						try {
							previewCreator.createPreview(videoFile, width, autoTile, rows, cols, autoCaps, capFiles, selectedMainTemplate, selectedFooterTemplate);
						} catch (PreviewCreatorException e) {
							logger.error("Could not create preview for file: {}", videoFile.getAbsolutePath(), e);
						}
						i++;
						updateOPG(i);
					}
				} finally {
					unlockGUI();
				}
			}
		});
		t.setName("MultiPreviewCreateThread-" + t.getId());
		t.start();
	}

	/**
	 * Creates a list with all video-files in the specified folder
	 * and subfolders if the recursive-flag is true
	 * 
	 * @param folder Folder containing the videos
	 * @param recursive True if recursive, false otherwise
	 * @return List of video files
	 */
	private List<File> createVideoList(File folder, boolean recursive) {
		List<File> videoFiles = new ArrayList<>();
		// TODO Maybe use newer API
		File files[] = folder.listFiles(VIDEO_FILE_FILTER);
		if (files != null) {
			for (File file : files) {
				videoFiles.add(file);
			}
		}

		if (recursive) {
			File subFolders[] = folder.listFiles(DIRECTORY_FILE_FILTER);
			if (subFolders != null) {
				for (File subFolder : subFolders) {
					videoFiles.addAll(createVideoList(subFolder, recursive));
				}
			}
		}

		return videoFiles;
	}

	private void actionCapsSelect() {
		File files[] = FileDialogUtil.showMultiFileOpenDialog(this, settingsManager.getDirectorySettings().getLastUsedPath(), null);
		if (files == null || files.length == 0) {
			return;
		}

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					lockGUI();

					pg.setMinimum(0);
					pg.setMaximum(files.length - 1);
					pg.setValue(0);
					int i = 0;
					for (File file : files) {
						Image img = CapUtil.getCapPreview(file, CAPS_ROW_HEIGHT);
						if (img != null) {
							model.addRow(files[i].getAbsolutePath(), img);
						}
						i++;
						updatePG(i);
					}
				} finally {
					unlockGUI();
				}
			}
		});
		t.setName("AddCapsThread-" + t.getId());
		t.start();
	}

	private void actionCapsUp() {
		int row = jtCaps.getSelectedRow();
		if (row > 0) {
			model.moveRow(row, row, row - 1);
			jtCaps.setRowSelectionInterval(row - 1, row - 1);
		}
	}

	private void actionCapsDown() {
		int row = jtCaps.getSelectedRow();
		if (row != -1 && row < jtCaps.getRowCount() - 1) {
			model.moveRow(row, row, row + 1);
			jtCaps.setRowSelectionInterval(row + 1, row + 1);
		}
	}

	private void actionCapsRemove() {
		int row = jtCaps.getSelectedRow();
		if (row == -1) {
			return;
		}
		model.removeRow(jtCaps.convertRowIndexToModel(row));
		if (row < jtCaps.getRowCount()) {
			jtCaps.setRowSelectionInterval(row, row);
		} else if ((row - 1) >= 0) {
			jtCaps.setRowSelectionInterval(row - 1, row - 1);
		}
	}

	private void updateCapsLabel() {
		int cols;
		try {
			cols = Integer.parseInt(txtCols.getText());
		} catch (NumberFormatException nfe) {
			logger.error("Columns is not an integer: {}", txtCols.getText());
			return;
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
