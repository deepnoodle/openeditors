package com.deepnoodle.openeditors.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.INavigationHistory;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import com.deepnoodle.openeditors.logging.LogWrapper;
import com.deepnoodle.openeditors.models.editor.Editor;

//rename
public class EditorService {
	private static LogWrapper log = new LogWrapper(EditorService.class);

	private Set<String> pinnedEditors = new HashSet<String>();

	private static EditorService instance;

	public static EditorService getInstance() {
		if (instance == null) {
			instance = new EditorService();
		}
		return instance;
	}

	public void openEditor(Editor editor, IWorkbenchPartSite site) throws PartInitException {
		site.getWorkbenchWindow().getActivePage()
				.openEditor(editor.getReference().getEditorInput(), editor.getReference().getId());
	}

	public void openEditor(String filePath, IWorkbenchPartSite site) throws PartInitException {
		IWorkspaceRoot ws = ResourcesPlugin.getWorkspace().getRoot();
		IPath location = new Path(filePath);
		IFile file = ws.getFile(location);

		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(filePath);
		site.getWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(file),
				desc.getId());

	}

	public void closeEditor(Editor editor, IWorkbenchPartSite site) {
		IEditorPart iEditorPart = editor.getReference().getEditor(true);
		if (iEditorPart != null) {
			site.getWorkbenchWindow().getActivePage()
					.closeEditor(iEditorPart, true);
		}

	}

	public Editor[] buildOpenEditors() {
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IEditorReference[] references = activePage.getEditorReferences();

		INavigationHistory navigationHistories = activePage.getNavigationHistory();
		INavigationLocation[] locations = navigationHistories.getLocations();
		Map<String, Editor> javaEditors = new HashMap<String, Editor>();

		for (int i = 0; i < references.length; i++) {
			IEditorReference reference = references[i];
			Integer naturalPosition = i;

			Editor editor = new Editor(reference, naturalPosition);
			javaEditors.put(editor.getFilePath(), editor);

			//This could be optimized if need be
			for (int h = 0; h < locations.length; h++) {
				try {
					INavigationLocation location = locations[h];
					if (location.getInput() == reference.getEditorInput()) {
						editor.setHistoryPosition(h);
						break;
					}
				} catch (Exception e) {
					log.warn(e);
				}
			}

			if (pinnedEditors.contains(editor.getFilePath())) {
				editor.setPinned(true);
			}

		}
		return javaEditors.values().toArray(new Editor[javaEditors.size()]);
	}

	public void pin(String editorFilePath) {
		if (editorFilePath != null) {
			pinnedEditors.add(editorFilePath);
		}

	}

	public void unpin(String editorFilePath) {
		if (editorFilePath != null) {
			pinnedEditors.remove(editorFilePath);
		}

	}

}
