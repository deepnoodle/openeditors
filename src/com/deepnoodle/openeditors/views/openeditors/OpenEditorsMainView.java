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

import com.deepnoodle.openeditors.actions.ManageSetsAction;
import com.deepnoodle.openeditors.actions.SaveSetAction;
import com.deepnoodle.openeditors.actions.SortAction;
import com.deepnoodle.openeditors.models.editor.EditorComparator;
import com.deepnoodle.openeditors.models.editor.IEditor;
import com.deepnoodle.openeditors.services.EditorService;
import com.deepnoodle.openeditors.services.SettingsService;

//TODO clean this up
public class OpenEditorsMainView extends ViewPart {

	private EditorService editorService = EditorService.getInstance();
	private SettingsService settingsService = SettingsService.getInstance();

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

		EditorSetComboControl editorSetComboControl = new EditorSetComboControl(editorTableView);

		Action loadSetAction = new ManageSetsAction(editorSetComboControl);
		Action saveSetAction = new SaveSetAction(editorSetComboControl);

		Action sortByAccessAction = new SortAction(editorTableView,
				EditorComparator.SortType.ACCESS,
				"Sort by Last Access",
				"Sorts the tabs by last access using eclipse navigation history");
		Action sortByNameAction = new SortAction(editorTableView,
				EditorComparator.SortType.NAME,
				"Sort by Name",
				"Sorts the tabs by name");
		Action sortByPathAction = new SortAction(editorTableView,
				EditorComparator.SortType.PATH,
				"Sort by Path",
				"Sorts the tabs by full path");

		IActionBars bars = getViewSite().getActionBars();
		IMenuManager menuManager = bars.getMenuManager();
		menuManager.add(loadSetAction);
		menuManager.add(new Separator());
		menuManager.add(sortByNameAction);
		menuManager.add(sortByPathAction);

		//TODO fix and add back in
		//menuManager.add(sortByAccessAction);

		IToolBarManager toolbarManager = bars.getToolBarManager();

		toolbarManager.add(editorSetComboControl);
		toolbarManager.add(loadSetAction);
		toolbarManager.add(saveSetAction);

		//Create a builder somewhere else?
		final Menu contextMenu = new Menu(parent);
		editorTableView.getTable().setMenu(contextMenu);

		//TODO extract to own class?
		final MenuItem pinMenuItem = new MenuItem(contextMenu, SWT.None);
		pinMenuItem.setText("Pin");
		pinMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				List<IEditor> editors = editorTableView.getSelections();
				for (IEditor editor : editors) {
					editor.setPinned(true);
					settingsService.saveSettings();
				}
				editorTableView.refresh();
			}
		});

		final MenuItem unpinMenuItem = new MenuItem(contextMenu, SWT.None);
		unpinMenuItem.setText("Un-Pin");
		unpinMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				List<IEditor> editors = editorTableView.getSelections();
				for (IEditor editor : editors) {
					editor.setPinned(false);
					settingsService.saveSettings();
				}
				editorTableView.refresh();
			}

		});

		final MenuItem closeMenuItem = new MenuItem(contextMenu, SWT.None);
		closeMenuItem.setText("Close and Remove");
		closeMenuItem.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				List<IEditor> editors = editorTableView.getSelections();
				for (IEditor editor : editors) {
					if (editor.isOpened()) {
						editorService.closeEditor(editor, getSite());
					}
					settingsService.getActiveEditorSettingsSet().getEditorModels().remove(editor.getFilePath());
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
