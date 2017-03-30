package com.deepnoodle.openeditors.models.editor;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.FileEditorInput;

import com.deepnoodle.openeditors.logging.LogWrapper;

public class Editor {
	private static LogWrapper log = new LogWrapper(Editor.class);

	private IEditorReference reference;
	private Integer naturalPosition;
	private Integer historyPosition;

	private boolean pinned;

	public Editor(IEditorReference reference, Integer naturalPosition) {
		this.reference = reference;

		this.naturalPosition = naturalPosition;

	}

	public IEditorReference getReference() {
		return reference;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}

	private String getDisplayName() {
		return reference.getPartName();

	}

	//TODO There has got to be a better way
	public String getFilePath() {
		String path = null;
		try {
			if (reference.getEditorInput() != null && reference.getEditorInput() instanceof FileEditorInput) {
				FileEditorInput fileEditorInput = ((FileEditorInput) reference.getEditorInput());
				path = fileEditorInput.getFile().getFullPath().toString();
			}
		} catch (Exception e) {
			log.warn(e, "Problem getting filepath");
		}
		if (path == null && reference != null) {
			//Last ditch effort to try and get the filename from the tooltip
			try {
				return reference.getEditorInput().getToolTipText();
			} catch (Exception e) {
				log.warn(e, "Problem getting filepath from tooltip");
			}
		}
		return path;
	}

	public String getContentDescription() {
		return reference.getContentDescription();
	}

	public String getName() {
		return reference.getName();
	}

	public IWorkbenchPage getPage() {
		return reference.getPage();
	}

	public String getTitle() {
		return reference.getTitle();
	}

	public Image getTitleImage() {
		return reference.getTitleImage();
	}

	public boolean isDirty() {
		return reference.isDirty();
	}

	public Integer getNaturalPosition() {
		return naturalPosition;
	}

	public void setNaturalPosition(Integer naturalPosition) {
		this.naturalPosition = naturalPosition;
	}

	public boolean isPinned() {
		return pinned;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}

	public Integer getHistoryPosition() {
		return historyPosition;
	}

	public void setHistoryPosition(Integer historyPosition) {
		this.historyPosition = historyPosition;
	}

}
