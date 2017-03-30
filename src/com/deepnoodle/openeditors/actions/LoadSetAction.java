package com.deepnoodle.openeditors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;

import com.deepnoodle.openeditors.logging.LogWrapper;
import com.deepnoodle.openeditors.models.settings.EditorSetSettingsModel;
import com.deepnoodle.openeditors.models.settings.EditorSettingsModel;
import com.deepnoodle.openeditors.models.settings.SettingsModel;
import com.deepnoodle.openeditors.services.EditorService;
import com.deepnoodle.openeditors.services.SettingsService;
import com.deepnoodle.openeditors.views.load.LoadDialogView;
import com.deepnoodle.openeditors.views.openeditors.EditorTableView;

public class LoadSetAction extends Action {
	private static LogWrapper log = new LogWrapper(EditorTableView.class);

	EditorService editorService = EditorService.getInstance();
	SettingsService settingsService = SettingsService.getInstance();
	private IWorkbenchPartSite site;

	public LoadSetAction(IWorkbenchPartSite site) {
		this.site = site;
		setText("Load Set");
		setToolTipText("Load Set");
		setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
	}

	@Override
	public void run() {

		LoadDialogView dialog = new LoadDialogView();
		dialog.create();
		if (dialog.open() == Window.OK) {
			String setName = dialog.getFileName();
			SettingsModel settingsModel = settingsService.getOrCreateSettings();
			settingsModel.setActiveSetName(setName);
			EditorSetSettingsModel editorSettingsSet = settingsModel.getEditorSettingsSet(setName);
			for (EditorSettingsModel editor : editorSettingsSet.getEditorModels().values()) {
				try {
					editorService.openEditor(editor.getFilePath(), site);
					if (editor.isPinned()) {
						editorService.pin(editor.getFilePath());
					}
				} catch (Exception e) {
					log.warn(e);
				}
			}
		}

	}
}
