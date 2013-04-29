/**
 * 
 */
package hu.bme.cs.music.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Rhythm classifier");
		shell.setSize(750, 500);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		shell.setLayout(gridLayout);

		// ---------- First row

		Button mode1Button = new Button(shell, SWT.RADIO | SWT.LEFT);
		mode1Button.setText("Working directory: ");
		mode1Button.setSelection(true);

		final Text nameText = new Text(shell, SWT.BORDER);
		setDefaultGridData(nameText);
		nameText.setText("data/szeke/nya");

		final Button browserButton = new Button(shell, SWT.PUSH);
		browserButton.setText("Browse");
		browserButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				DirectoryDialog dlg = new DirectoryDialog(shell);
				dlg.setFilterPath(nameText.getText());
				dlg.setText("Choose working directory");
				String dir = dlg.open();
				if (dir != null) {
					nameText.setText(dir);
				}
			}
		});

		// ---------- Second row

		Button mode2Button = new Button(shell, SWT.RADIO | SWT.LEFT);
		mode2Button.setText("Song IDs: ");
		mode2Button.setSelection(false);

		final Text idText = new Text(shell, SWT.BORDER);
		setDefaultGridData(idText,2);
		idText.setText("250 278 279 287 1033 1462 249");
		idText.setEnabled(false);
		
		// ---------- Third row

		Group comparersGroup = new Group(shell, SWT.SHADOW_IN);
		comparersGroup.setText("Comparer method");
		comparersGroup.setLayout(new RowLayout(SWT.VERTICAL));
		
		Button weightedHammingButton = new Button(comparersGroup, SWT.RADIO);
		weightedHammingButton.setText("Weighted Hamming");
		weightedHammingButton.setSelection(true);
		
		Button euclideanButton = new Button(comparersGroup, SWT.RADIO);
		euclideanButton.setText("Euclidean");
		
		Button intervalDiffButton = new Button(comparersGroup, SWT.RADIO);
		intervalDiffButton.setText("Interval Difference");
		
		Button swapButton = new Button(comparersGroup, SWT.RADIO);
		swapButton.setText("Swap");
		
		Button chronotinicButton = new Button(comparersGroup, SWT.RADIO);
		chronotinicButton.setText("Chronotonic");
		
		Button continousChronotonicButton = new Button(comparersGroup, SWT.RADIO);
		continousChronotonicButton.setText("Continous Chronotonic");
		
		Group classifiersGroup = new Group(shell, SWT.SHADOW_IN);
		classifiersGroup.setText("Classifier method");
		classifiersGroup.setLayout(new RowLayout(SWT.VERTICAL));
		
		Button singleLinkageButton = new Button(classifiersGroup, SWT.RADIO);
		singleLinkageButton.setText("Sinlge Linkage");
		
		Button avgLinkageButton = new Button(classifiersGroup, SWT.RADIO);
		avgLinkageButton.setText("Average Linkage");
		
		Button completeLinkageButton = new Button(classifiersGroup, SWT.RADIO);
		completeLinkageButton.setText("Complete Linkage");
		completeLinkageButton.setSelection(true);
		
		Button randomKMeansButton = new Button(classifiersGroup, SWT.RADIO);
		randomKMeansButton.setText("Random K-Means");
		
		Button firstRandomKMeansButton = new Button(classifiersGroup, SWT.RADIO);
		firstRandomKMeansButton.setText("First Random K-Means");
		
		Button farthestFirstKMeansButton = new Button(classifiersGroup, SWT.RADIO);
		farthestFirstKMeansButton.setText("Farthest First K-Means");
		
		Button DBSCANButton = new Button(classifiersGroup, SWT.RADIO);
		DBSCANButton.setText("DBSCAN");
		
		Group paramsGroup = new Group(shell, SWT.SHADOW_IN);
		paramsGroup.setText("Parameters");
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		paramsGroup.setLayout(gridLayout);
		
		new Label(paramsGroup, SWT.None).setText("K: ");

		final Text kText = new Text(paramsGroup, SWT.BORDER);
		setDefaultGridData(kText);
		
		new Label(paramsGroup, SWT.None).setText("minPts: ");
		
		final Text minPtsText = new Text(paramsGroup, SWT.BORDER);
		setDefaultGridData(minPtsText);
		minPtsText.setEnabled(false);
		
		new Label(paramsGroup, SWT.None).setText("eps: ");
		
		final Text epsText = new Text(paramsGroup, SWT.BORDER);
		setDefaultGridData(epsText);
		epsText.setEnabled(false);
		
		final Button startButton = new Button(paramsGroup, SWT.PUSH);
		startButton.setText("Classify!");
		setDefaultGridData(startButton,2);
		
		// ---------- Results row
		
		Label resultLabel = new Label(shell, SWT.None);
		resultLabel.setText("Results:");
		setDefaultGridData(resultLabel,3);
		
		Text resultText = new Text(shell, SWT.READ_ONLY | SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		resultText.setLayoutData(gridData);
		
		//Listeners
		
		mode1Button.addListener(SWT.Selection, createEnableListener(nameText));
		mode1Button.addListener(SWT.Selection, createEnableListener(browserButton));
		mode2Button.addListener(SWT.Selection, createEnableListener(idText));
		
		singleLinkageButton.addListener(SWT.Selection, createEnableListener(kText));
		avgLinkageButton.addListener(SWT.Selection, createEnableListener(kText));
		completeLinkageButton.addListener(SWT.Selection, createEnableListener(kText));
		randomKMeansButton.addListener(SWT.Selection, createEnableListener(kText));
		firstRandomKMeansButton.addListener(SWT.Selection, createEnableListener(kText));
		farthestFirstKMeansButton.addListener(SWT.Selection, createEnableListener(kText));
		
		DBSCANButton.addListener(SWT.Selection, createEnableListener(minPtsText));
		DBSCANButton.addListener(SWT.Selection, createEnableListener(epsText));
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

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
