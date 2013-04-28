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
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		nameText.setLayoutData(gridData);
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
		Listener radioFunction1 = new Listener() {
			public void handleEvent(Event event) {
				Button button = (Button) event.widget;
				nameText.setEnabled(button.getSelection());
				browserButton.setEnabled(button.getSelection());
			}
		};
		mode1Button.addListener(SWT.Selection, radioFunction1);

		// ---------- Second row

		Button mode2Button = new Button(shell, SWT.RADIO | SWT.LEFT);
		mode2Button.setText("Song IDs: ");
		mode2Button.setSelection(false);

		final Text idText = new Text(shell, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		idText.setLayoutData(gridData);
		idText.setText("250 278 279 287 1033 1462 249");
		idText.setEnabled(false);

		Listener radioFunction2 = new Listener() {
			public void handleEvent(Event event) {
				Button button = (Button) event.widget;
				idText.setEnabled(button.getSelection());
			}
		};
		mode2Button.addListener(SWT.Selection, radioFunction2);

		Group comparersGroup = new Group(shell, SWT.SHADOW_IN);
		comparersGroup.setText("Comparer method");
		comparersGroup.setLayout(new RowLayout(SWT.VERTICAL));
		new Button(comparersGroup, SWT.RADIO).setText("Weighted Hamming");
		new Button(comparersGroup, SWT.RADIO).setText("Euclidean");
		new Button(comparersGroup, SWT.RADIO).setText("Interval Difference");
		new Button(comparersGroup, SWT.RADIO).setText("Swap");
		new Button(comparersGroup, SWT.RADIO).setText("Chronotonic");
		new Button(comparersGroup, SWT.RADIO).setText("Continous Chronotonic");
		
		Group classifiersGroup = new Group(shell, SWT.SHADOW_IN);
		classifiersGroup.setText("Classifier method");
		classifiersGroup.setLayout(new RowLayout(SWT.VERTICAL));
		new Button(classifiersGroup, SWT.RADIO).setText("Sinlge Linkage");
		new Button(classifiersGroup, SWT.RADIO).setText("Average Linkage");
		new Button(classifiersGroup, SWT.RADIO).setText("Complete Linkage");
		new Button(classifiersGroup, SWT.RADIO).setText("Random K-Means");
		new Button(classifiersGroup, SWT.RADIO).setText("First Random K-Means");
		new Button(classifiersGroup, SWT.RADIO).setText("Farthest First K-Means");
		new Button(classifiersGroup, SWT.RADIO).setText("DBSCAN");
		
		Group paramssGroup = new Group(shell, SWT.SHADOW_IN);
		paramssGroup.setText("Parameters");
		paramssGroup.setLayout(new RowLayout(SWT.VERTICAL));
		new Label(paramssGroup, SWT.None).setText("K: ");

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}
}
