package com.deepnoodle.openeditors.actions.sort;

import org.eclipse.jface.action.Action;

import com.deepnoodle.openeditors.models.editor.EditorComparator;

public class SortByPathAction extends Action {
	private EditorComparator sorter;

	public SortByPathAction(EditorComparator sorter) {
		this.sorter = sorter;
		setText("Sort by path");
		setToolTipText("Sort by path");
	}

	@Override
	public void run() {
		sorter.setSortBy(EditorComparator.SortType.PATH);
	}

}
