/**
 * 
 */
package hu.bme.cs.music.gui;

import hu.bme.cs.music.file.FileReader;
import hu.bme.cs.music.utils.FileUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Jozsef
 * 
 */
public class MainWindow {

	private static Button mode1Button;
	private static Button mode2Button;

	private static Button browserButton;

	private static Combo melodicClusterDropDown;

	private static Button weightedHammingButton;
	private static Button manhattanButton;
	private static Button euclideanButton;
	private static Button intervalDiffButton;
	private static Button swapButton;
	private static Button chronotinicButton;
	private static Button continousChronotonicButton;

	private static Button singleLinkageButton;
	private static Button avgLinkageButton;
	private static Button completeLinkageButton;
	private static Button randomKMeansButton;
	private static Button firstRandomKMeansButton;
	private static Button farthestFirstKMeansButton;
	private static Button DBSCANButton;

	private static Text dirText;
	private static Text fileIdsText;

	private static Text kText;
	private static Text minPtsText;
	private static Text epsText;
	private static Text KMeansLoopText;

	private static Text resultText;
	private static Text metricsText;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileUtils.initLogging();
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Rhythm classifier");
		shell.setSize(750, 500);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		shell.setLayout(gridLayout);

		// ---------- First row

		mode1Button = new Button(shell, SWT.RADIO | SWT.LEFT);
		mode1Button.setText("Working directory: ");
		mode1Button.setSelection(true);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		mode1Button.setLayoutData(gd);

		dirText = new Text(shell, SWT.BORDER);
		setDefaultGridData(dirText);
		dirText.setText("data/szeke/nya");

		browserButton = new Button(shell, SWT.PUSH);
		browserButton.setText("Browse");
		browserButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				DirectoryDialog dlg = new DirectoryDialog(shell);
				dlg.setFilterPath(dirText.getText());
				dlg.setText("Choose working directory");
				String dir = dlg.open();
				if (dir != null) {
					dirText.setText(dir);
				}
			}
		});

		// ---------- Second row

		mode2Button = new Button(shell, SWT.RADIO | SWT.LEFT);
		mode2Button.setText("Melodic cluster: ");
		mode2Button.setSelection(false);

		melodicClusterDropDown = new Combo(shell, SWT.DROP_DOWN | SWT.BORDER);
		for (int i = 1; i <= 28; i++) {
			melodicClusterDropDown.add(i + "");
		}
		melodicClusterDropDown.setText("11");
		melodicClusterDropDown.setEnabled(false);

		fileIdsText = new Text(shell, SWT.BORDER);
		setDefaultGridData(fileIdsText, 2);
		fileIdsText
				.setText("100 101 102 104 105 107 111 138 139 171 1136 1295 106");
		fileIdsText.setEnabled(false);

		// ---------- Third row

		Group comparersGroup = new Group(shell, SWT.SHADOW_IN);
		comparersGroup.setText("Comparer method");

		gd = new GridData();
		gd.horizontalSpan = 2;
		comparersGroup.setLayoutData(gd);
		comparersGroup.setLayout(new RowLayout(SWT.VERTICAL));

		weightedHammingButton = new Button(comparersGroup, SWT.RADIO);
		weightedHammingButton.setText("Weighted Hamming");
		weightedHammingButton.setSelection(true);
		
		manhattanButton= new Button(comparersGroup, SWT.RADIO);
		manhattanButton.setText("Manhattan");

		euclideanButton = new Button(comparersGroup, SWT.RADIO);
		euclideanButton.setText("Euclidean");

		intervalDiffButton = new Button(comparersGroup, SWT.RADIO);
		intervalDiffButton.setText("Interval Difference");

		swapButton = new Button(comparersGroup, SWT.RADIO);
		swapButton.setText("Swap");

		chronotinicButton = new Button(comparersGroup, SWT.RADIO);
		chronotinicButton.setText("Chronotonic");

		continousChronotonicButton = new Button(comparersGroup, SWT.RADIO);
		continousChronotonicButton.setText("Continous Chronotonic");

		Group classifiersGroup = new Group(shell, SWT.SHADOW_IN);
		classifiersGroup.setText("Classifier method");
		classifiersGroup.setLayout(new RowLayout(SWT.VERTICAL));

		singleLinkageButton = new Button(classifiersGroup, SWT.RADIO);
		singleLinkageButton.setText("Single Linkage");

		avgLinkageButton = new Button(classifiersGroup, SWT.RADIO);
		avgLinkageButton.setText("Average Linkage");

		completeLinkageButton = new Button(classifiersGroup, SWT.RADIO);
		completeLinkageButton.setText("Complete Linkage");
		completeLinkageButton.setSelection(true);

		randomKMeansButton = new Button(classifiersGroup, SWT.RADIO);
		randomKMeansButton.setText("Random K-Means");

		firstRandomKMeansButton = new Button(classifiersGroup, SWT.RADIO);
		firstRandomKMeansButton.setText("First Random K-Means");

		farthestFirstKMeansButton = new Button(classifiersGroup, SWT.RADIO);
		farthestFirstKMeansButton.setText("Farthest First K-Means");

		DBSCANButton = new Button(classifiersGroup, SWT.RADIO);
		DBSCANButton.setText("DBSCAN");

		Group paramsGroup = new Group(shell, SWT.SHADOW_IN);
		paramsGroup.setText("Parameters");
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		paramsGroup.setLayout(gridLayout);

		new Label(paramsGroup, SWT.None).setText("K: ");

		kText = new Text(paramsGroup, SWT.BORDER);
		kText.setText("6");
		setDefaultGridData(kText);

		new Label(paramsGroup, SWT.None).setText("minPts: ");

		minPtsText = new Text(paramsGroup, SWT.BORDER);
		minPtsText.setText("2");
		setDefaultGridData(minPtsText);
		minPtsText.setEnabled(false);

		new Label(paramsGroup, SWT.None).setText("eps: ");

		epsText = new Text(paramsGroup, SWT.BORDER);
		epsText.setText("0.21");
		setDefaultGridData(epsText);
		epsText.setEnabled(false);

		new Label(paramsGroup, SWT.None).setText("Loop count: ");

		KMeansLoopText = new Text(paramsGroup, SWT.BORDER);
		KMeansLoopText.setText("10");
		setDefaultGridData(KMeansLoopText);
		KMeansLoopText.setEnabled(false);

		final LogicControl lc = new LogicControl();

		final Button startButton = new Button(paramsGroup, SWT.PUSH);
		startButton.setText("Classify!");
		setDefaultGridData(startButton, 2);
		startButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				lc.run();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});

		// ---------- Results row

		Label resultLabel = new Label(shell, SWT.None);
		resultLabel.setText("Results:");
		setDefaultGridData(resultLabel, 3);
		Label metricsLabel = new Label(shell, SWT.None);
		metricsLabel.setText("Metrics:");

		resultText = new Text(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.READ_ONLY | SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalSpan = 3;
		resultText.setLayoutData(gd);
		
		metricsText = new Text(shell, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
	//	gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		metricsText.setLayoutData(gd);

		addListeners();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

	private static void addListeners() {
		melodicClusterDropDown.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				fileIdsText.setText(FileReader
						.getFileIdsLine(melodicClusterDropDown.getText()));
			}
		});

		mode1Button.addListener(SWT.Selection, createEnableListener(dirText));
		mode1Button.addListener(SWT.Selection,
				createEnableListener(browserButton));
		mode2Button.addListener(SWT.Selection,
				createEnableListener(fileIdsText));
		mode2Button.addListener(SWT.Selection,
				createEnableListener(melodicClusterDropDown));

		singleLinkageButton.addListener(SWT.Selection,
				createEnableListener(kText));
		avgLinkageButton
				.addListener(SWT.Selection, createEnableListener(kText));
		completeLinkageButton.addListener(SWT.Selection,
				createEnableListener(kText));
		randomKMeansButton.addListener(SWT.Selection,
				createEnableListener(kText));
		firstRandomKMeansButton.addListener(SWT.Selection,
				createEnableListener(kText));
		farthestFirstKMeansButton.addListener(SWT.Selection,
				createEnableListener(kText));

		randomKMeansButton.addListener(SWT.Selection,
				createEnableListener(KMeansLoopText));
		firstRandomKMeansButton.addListener(SWT.Selection,
				createEnableListener(KMeansLoopText));
		farthestFirstKMeansButton.addListener(SWT.Selection,
				createEnableListener(KMeansLoopText));

		DBSCANButton.addListener(SWT.Selection,
				createEnableListener(minPtsText));
		DBSCANButton.addListener(SWT.Selection, createEnableListener(epsText));
	}

	public static void setResultText(String result) {
		resultText.setText(result);
	}
	
	public static void setMetricsText(String result) {
		metricsText.setText(result);
	}

	public static String getDirectory() {
		return dirText.getText();
	}

	public static String getFileIdsLine() {
		return fileIdsText.getText();
	}

	public static int getMode() {
		if (mode1Button.getSelection()) {
			return 1;
		}
		return 2;
	}

	public static int getK() {
		String res = kText.getText();
		if (res == null || res.isEmpty()) {
			return 0;
		}
		return Integer.valueOf(kText.getText());
	}

	public static int getKMeansLoop() {
		String res = KMeansLoopText.getText();
		if (res == null || res.isEmpty()) {
			return 0;
		}
		return Integer.valueOf(KMeansLoopText.getText());
	}

	public static int getMinPts() {
		String res = minPtsText.getText();
		if (res == null || res.isEmpty()) {
			return 0;
		}
		return Integer.valueOf(minPtsText.getText());
	}

	public static double getEps() {
		String res = epsText.getText();
		if (res == null || res.isEmpty()) {
			return 0;
		}
		return Double.valueOf(epsText.getText());
	}

	public static int getComparerId() {
		if (weightedHammingButton.getSelection()) {
			return 0;
		}
		if (manhattanButton.getSelection()) {
			return 1;
		}
		if (euclideanButton.getSelection()) {
			return 2;
		}
		if (intervalDiffButton.getSelection()) {
			return 3;
		}
		if (swapButton.getSelection()) {
			return 4;
		}
		if (chronotinicButton.getSelection()) {
			return 5;
		}
		return 6;
	}

	public static int getClassifierId() {
		if (singleLinkageButton.getSelection()) {
			return 0;
		}
		if (avgLinkageButton.getSelection()) {
			return 1;
		}
		if (completeLinkageButton.getSelection()) {
			return 2;
		}
		if (randomKMeansButton.getSelection()) {
			return 3;
		}
		if (firstRandomKMeansButton.getSelection()) {
			return 4;
		}
		if (farthestFirstKMeansButton.getSelection()) {
			return 5;
		}
		return 6;
	}

	private static Listener createEnableListener(final Control c) {
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				Button button = (Button) event.widget;
				c.setEnabled(button.getSelection());
			}
		};
		return listener;
	}

	private static void setDefaultGridData(Control c) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		c.setLayoutData(gridData);
	}

	private static void setDefaultGridData(Control c, int horizontalSpan) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = horizontalSpan;
		c.setLayoutData(gridData);
	}
}
