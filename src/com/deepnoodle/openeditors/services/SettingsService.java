package com.deepnoodle.openeditors.services;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.deepnoodle.openeditors.actions.daos.SettingsDao;
import com.deepnoodle.openeditors.logging.LogWrapper;
import com.deepnoodle.openeditors.models.editor.Editor;
import com.deepnoodle.openeditors.models.editor.EditorComparator.SortType;
import com.deepnoodle.openeditors.models.settings.EditorSetSettingsModel;
import com.deepnoodle.openeditors.models.settings.EditorSettingsModel;
import com.deepnoodle.openeditors.models.settings.SettingsModel;

public class SettingsService {

	private static LogWrapper log = new LogWrapper(SettingsService.class);

	private SettingsDao settingsDao = SettingsDao.getInstance();

	private SettingsModel settings;

	private static SettingsService instance;

	public static SettingsService getInstance() {
		if (instance == null) {
			instance = new SettingsService();
		}
		return instance;
	}

	public Set<String> openWindowsSet(String fileName) {
		SettingsModel settings = getOrCreateSettings();
		return settings.getEditorSettingsSets().keySet();
	}

	public void saveWindowSet(String setName, boolean includeInProject, Editor[] openWindows) {
		SettingsModel settingsModel = getOrCreateSettings();
		EditorSetSettingsModel editorSettingsSet = settingsModel.getEditorSettingsSet(setName);
		if (editorSettingsSet == null) {
			editorSettingsSet = new EditorSetSettingsModel();
			settingsModel.getEditorSettingsSets().put(setName, editorSettingsSet);
		}
		editorSettingsSet.setName(setName);
		if (settingsModel.getActiveEditorSettingsSet() != null) {
			editorSettingsSet.setSortBy(settingsModel.getActiveEditorSettingsSet().getSortBy());
		} else {
			editorSettingsSet.setSortBy(SortType.ACCESS);
		}
		Map<String, EditorSettingsModel> editorModels = new TreeMap<>();
		for (Editor editor : openWindows) {
			editorModels.put(editor.getName(), new EditorSettingsModel(editor));
		}

		editorSettingsSet.setEditorModels(editorModels);
		settingsModel.setActiveSetName(setName);
		settingsDao.saveSettings(settingsModel);
		log.warn("Save not yet implements");

	}

	public void deleteWindowSet(String setName) {
		SettingsModel settingsModel = getOrCreateSettings();

		if (setName == settingsModel.getActiveSetName()) {
			//TODO create constant for magic string
			settingsModel.setActiveSetName("DEFAULT");
		}

		settingsModel.getEditorSettingsSets().remove(setName);

		settingsDao.saveSettings(settingsModel);

	}

	public SettingsModel getOrCreateSettings() {
		if (settings == null) {
			settings = settingsDao.loadSettings();
		}
		return settings;
	}

}
