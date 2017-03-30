package com.deepnoodle.openeditors.actions.sort;

import org.eclipse.jface.action.Action;

import com.deepnoodle.openeditors.models.editor.EditorComparator;

public class SortByAccessAction extends Action {
	private EditorComparator sorter;

	public SortByAccessAction(EditorComparator sorter) {
		this.sorter = sorter;
		setText("Sort by last access");
		setToolTipText("Sort by last access");
	}

	@Override
	public void run() {
		sorter.setSortBy(EditorComparator.SortType.ACCESS);
	}

}
