package com.deepnoodle.openeditors.views.openeditors;

import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;

import com.deepnoodle.openeditors.logging.LogWrapper;
import com.deepnoodle.openeditors.models.editor.EditorComparator;
import com.deepnoodle.openeditors.models.editor.EditorComparator.SortType;
import com.deepnoodle.openeditors.models.editor.IEditor;
import com.deepnoodle.openeditors.services.EditorService;
import com.deepnoodle.openeditors.services.SettingsService;

public class EditorTableView implements IDoubleClickListener {
	private static LogWrapper log = new LogWrapper(EditorTableView.class);

	private SettingsService settingsService = SettingsService.getInstance();

	private EditorRowFormatter editorRowFormatter = EditorRowFormatter.getInstance();

	private EditorService openEditorService = EditorService.getInstance();

	private TableViewer tableViewer;

	private EditorComparator editorComparator;

	private IWorkbenchPartSite site;

	private IEditor activeEditor;

	public EditorTableView(Composite parent, IWorkbenchPartSite site, IViewSite iViewSite) {
		tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		this.site = site;

		//Build sorter
		SortType sortBy = SortType.ACCESS;
		editorComparator = new EditorComparator(sortBy);
		tableViewer.setComparator(editorComparator);

		tableViewer.setContentProvider(new EditorViewContentProvider());
		tableViewer.setLabelProvider(new EditorViewLabelProvider());
		tableViewer.setInput(iViewSite);

		tableViewer.addDoubleClickListener(this);

	}

	public void refresh() {
		try {
			if (tableViewer.getControl() != null && !tableViewer.getControl().isDisposed()) {
				tableViewer.refresh();
				TableItem[] items = tableViewer.getTable().getItems();
				tableViewer.setSelection(StructuredSelection.EMPTY);
				editorRowFormatter.formatRows(items, activeEditor, tableViewer.getTable().getForeground(),
						tableViewer.getTable().getBackground());
			}
		} catch (Exception e) {
			log.warn(e);
		}
	}

	public void setSortBy(EditorComparator.SortType sortBy) {
		editorComparator.setSortBy(sortBy);
		settingsService.getActiveEditorSettingsSet().setSortBy(sortBy);
		refresh();
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		List<IEditor> editors = getSelections();
		for (IEditor editor : editors) {
			try {
				openEditorService.openEditor(editor, site);
			} catch (Exception e) {
				log.warn(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<IEditor> getSelections() {
		return tableViewer.getStructuredSelection().toList();
	}

	public IEditor getSelection() {
		return (IEditor) tableViewer.getStructuredSelection().getFirstElement();
	}

	public EditorComparator getSorter() {
		return editorComparator;
	}

	public Control getTable() {
		return tableViewer.getTable();
	}

	public void setActivePart(IWorkbenchPart activePart) {
		TableItem[] items = tableViewer.getTable().getItems();
		for (TableItem item : items) {
			IEditor editor = ((IEditor) item.getData());
			if (editor.isOpened()
					&& editor.getReference() != null
					&& editor.getReference().getPart(false) == activePart) {
				activeEditor = editor;
			}
		}
	}

	public IWorkbenchPartSite getSite() {
		return site;
	}

}
