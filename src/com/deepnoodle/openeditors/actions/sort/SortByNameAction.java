package com.deepnoodle.openeditors.actions.sort;

import org.eclipse.jface.action.Action;

import com.deepnoodle.openeditors.models.editor.EditorComparator;

public class SortByNameAction extends Action {
	private EditorComparator sorter;

	public SortByNameAction(EditorComparator sorter) {
		this.sorter = sorter;
		setText("Sort by name");
		setToolTipText("Sort by name");
	}

	@Override
	public void run() {
		sorter.setSortBy(EditorComparator.SortType.NAME);
	}

}
