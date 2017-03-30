package com.deepnoodle.openeditors.views.openeditors;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

import com.deepnoodle.openeditors.logging.LogWrapper;
import com.deepnoodle.openeditors.models.editor.Editor;
import com.deepnoodle.openeditors.models.settings.SettingsModel;
import com.deepnoodle.openeditors.services.SettingsService;

public class EditorRowFormatter {

	private static LogWrapper log = new LogWrapper(EditorTableView.class);

	private SettingsService settingsService = SettingsService.getInstance();

	private static EditorRowFormatter instance;

	public static EditorRowFormatter getInstance() {
		if (instance == null) {
			instance = new EditorRowFormatter();
		}
		return instance;
	}

	public void formatRows(TableItem[] items, Editor activeEditor, Color forgroundColor) {

		SettingsModel settings = settingsService.getOrCreateSettings();

		Color dirtyColor = new Color(Display.getCurrent(), settings.getDirtyColor());
		Color pinnedColor = new Color(Display.getCurrent(), settings.getPinnedColor());
		Color highlightColor = new Color(Display.getCurrent(), settings.getHighlightColor());

		for (TableItem item : items) {
			try {
				Editor editor = ((Editor) item.getData());

				if (editor.isDirty()) {
					item.setForeground(dirtyColor);
				} else if (editor.isPinned()) {
					item.setForeground(pinnedColor);
				} else {
					item.setForeground(forgroundColor);
				}
				if (activeEditor != null && editor.getFilePath().equals(activeEditor.getFilePath())) {
					item.setBackground(highlightColor);
				}
			} catch (Exception e) {
				log.warn(e);
			}
		}
	}
}