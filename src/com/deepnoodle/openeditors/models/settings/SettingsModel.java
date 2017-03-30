package com.deepnoodle.openeditors.models.settings;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;

public class SettingsModel {

	private RGB highlightColor = new RGB(219, 219, 219);
	private RGB pinnedColor = new RGB(60, 15, 175);
	private RGB dirtyColor = new RGB(204, 0, 0);

	private String activeSetName = "DEFAULT";
	private Map<String, EditorSetSettingsModel> editorSettingsSets = new HashMap<>();

	public SettingsModel() {
		if (editorSettingsSets.get(activeSetName) == null) {
			editorSettingsSets.put(activeSetName, new EditorSetSettingsModel());
		}
	}

	public RGB getHighlightColor() {
		return highlightColor;
	}

	public void setHighlightColor(RGB highlightColor) {
		this.highlightColor = highlightColor;
	}

	public RGB getPinnedColor() {
		return pinnedColor;
	}

	public void setPinnedColor(RGB pinnedColor) {
		this.pinnedColor = pinnedColor;
	}

	public RGB getDirtyColor() {
		return dirtyColor;
	}

	public void setDirtyColor(RGB dirtyColor) {
		this.dirtyColor = dirtyColor;
	}

	public String getActiveSetName() {
		return activeSetName;
	}

	public void setActiveSetName(String currentSetName) {
		activeSetName = currentSetName;
	}

	public EditorSetSettingsModel getActiveEditorSettingsSet() {
		return getEditorSettingsSets().get(getActiveSetName());
	}

	public Map<String, EditorSetSettingsModel> getEditorSettingsSets() {
		return editorSettingsSets;
	}

	public void setEditorSettingsSets(Map<String, EditorSetSettingsModel> editorSettingsSets) {
		this.editorSettingsSets = editorSettingsSets;
	}

	public EditorSetSettingsModel getEditorSettingsSet(String name) {
		return getEditorSettingsSets().get(name);
	}

}
