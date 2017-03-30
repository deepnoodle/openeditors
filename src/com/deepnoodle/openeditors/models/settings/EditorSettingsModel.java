package com.deepnoodle.openeditors.models.settings;

import com.deepnoodle.openeditors.models.editor.Editor;

public class EditorSettingsModel {
	private String filePath;
	private String name;
	private boolean pinned;

	public EditorSettingsModel(Editor editor) {
		filePath = editor.getFilePath();
		name = editor.getName();
		pinned = editor.isPinned();
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String path) {
		filePath = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPinned() {
		return pinned;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}

}
