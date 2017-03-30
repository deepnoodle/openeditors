package com.deepnoodle.openeditors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.deepnoodle.openeditors.services.EditorService;
import com.deepnoodle.openeditors.services.SettingsService;
import com.deepnoodle.openeditors.views.save.SaveDialogView;

public class SaveSetAction extends Action {

	private SettingsService settingsService = SettingsService.getInstance();

	private EditorService openEditorService = EditorService.getInstance();

	public SaveSetAction() {
		setText("Save Set");
		setToolTipText("Save Set");
		setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));
	}

	@Override
	public void run() {
		SaveDialogView dialog = new SaveDialogView(new Shell());
		dialog.create();
		if (dialog.open() == Window.OK) {
			settingsService.saveWindowSet(dialog.getFileName(),
					dialog.isSaveInProject(),
					openEditorService.buildOpenEditors());
		}

	}

}
