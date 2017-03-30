package com.deepnoodle.openeditors.views.openeditors;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ViewPart;

import com.deepnoodle.openeditors.actions.LoadSetAction;
import com.deepnoodle.openeditors.actions.SaveSetAction;
import com.deepnoodle.openeditors.actions.sort.SortByAccessAction;
import com.deepnoodle.openeditors.actions.sort.SortByNameAction;
import com.deepnoodle.openeditors.actions.sort.SortByPathAction;
import com.deepnoodle.openeditors.logging.LogWrapper;
import com.deepnoodle.openeditors.models.editor.Editor;
import com.deepnoodle.openeditors.services.EditorService;

public class EditorsView extends ViewPart {
	private static LogWrapper log = new LogWrapper(EditorsView.class);

	private EditorService editorService = EditorService.getInstance();

	private EditorTableView editorTableView;

	@Override
	@PostConstruct
	public void createPartControl(Composite parent) {

		final IWorkbenchWindow workbenchWindow = getSite().getWorkbenchWindow();

		//Build the editor view
		editorTableView = new EditorTableView(parent, getSite(), getViewSite());

		PartListener listener = new PartListener(editorTableView);
		workbenchWindow.getPartService().addPartListener(listener);

		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);

		// Create the help context id for the viewer's control
		//PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "bickley.viewer");
		//build actions
		//Create a builder somewhere else
		Action saveSetAction = new SaveSetAction();
		Action loadSetAction = new LoadSetAction(getSite());
		Action sortByAccessAction = new SortByAccessAction(editorTableView.getSorter());
		Action sortByNameAction = new SortByNameAction(editorTableView.getSorter());
		Action sortByPathAction = new SortByPathAction(editorTableView.getSorter());

		//hookContextMenu();

		IActionBars bars = getViewSite().getActionBars();
		IMenuManager menuManager = bars.getMenuManager();
		menuManager.add(saveSetAction);
		menuManager.add(loadSetAction);
		menuManager.add(new Separator());
		menuManager.add(sortByNameAction);
		menuManager.add(sortByPathAction);
		menuManager.add(sortByAccessAction);

		IToolBarManager toolbarManager = bars.getToolBarManager();
		toolbarManager.add(saveSetAction);
		toolbarManager.add(loadSetAction);

		//Create a builder somewhere else
		final Menu contextMenu = new Menu(parent);
		editorTableView.getTable().setMenu(contextMenu);

		final MenuItem pinMenuItem = new MenuItem(contextMenu, SWT.None);
		pinMenuItem.setText("pin");
		pinMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				List<Editor> editors = editorTableView.getSelections();
				for (Editor editor : editors) {
					editorService.pin(editor.getFilePath());
				}
				editorTableView.refresh();
			}
		});

		final MenuItem unpinMenuItem = new MenuItem(contextMenu, SWT.None);
		unpinMenuItem.setText("un-pin");
		unpinMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				List<Editor> editors = editorTableView.getSelections();
				for (Editor editor : editors) {
					editorService.unpin(editor.getFilePath());
				}
				editorTableView.refresh();
			}

		});

		final MenuItem closeMenuItem = new MenuItem(contextMenu, SWT.None);
		closeMenuItem.setText("close");
		closeMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				List<Editor> editors = editorTableView.getSelections();
				for (Editor editor : editors) {
					editorService.closeEditor(editor, getSite());
				}
				editorTableView.refresh();
			}
		});

	}

	@Override
	public void setFocus() {
		// Do nothing

	}

}
