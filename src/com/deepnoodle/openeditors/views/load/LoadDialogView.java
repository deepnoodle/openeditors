package com.deepnoodle.openeditors.views.load;

import java.util.Map;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.deepnoodle.openeditors.models.settings.EditorSetSettingsModel;
import com.deepnoodle.openeditors.services.SettingsService;

//TODO clean this up
public class LoadDialogView extends TitleAreaDialog {
	private static final int DELETE = 3;

	private SettingsService settingsService = SettingsService.getInstance();

	private String fileName;

	private List list;

	public LoadDialogView() {
		super(new Shell());

	}

	@Override
	public void create() {
		super.create();
		setTitle("Open");
		setMessage("Please pick an item from the list.");
		setDialogHelpAvailable(false);
		setHelpAvailable(false);
		setTitleImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout(2, true);
		parent.setLayout(layout);

		createSetList(parent);

		return parent;
	}

	private void createSetList(Composite parent) {
		Map<String, EditorSetSettingsModel> editorSets = settingsService.getOrCreateSettings().getEditorSettingsSets();
		List list = new List(parent, 1);
		for (String setName : editorSets.keySet()) {
			if (!setName.equals("DEFAULT")) {
				list.add(setName);
			}
		}
		list.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		createOkButton(parent, OK, "Open", true);

		Button deleteButton = createButton(parent, DELETE, "Delete", false);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					settingsService.deleteWindowSet(getSelectedSetName());
					close();
				}
			}
		});

		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);

		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	protected Button createOkButton(Composite parent, int id,
			String label,
			boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					okPressed();
				}
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	private boolean isValidInput() {
		//TODO add validation
		return true;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private String getSelectedSetName() {
		String[] selections = list.getSelection();
		if (selections != null && selections.length >= 1) {
			return selections[0];
		} else {
			return null;
		}
	}

	@Override
	protected void okPressed() {
		fileName = getSelectedSetName();
		super.okPressed();
	}

	public String getFileName() {
		return fileName;
	}

}