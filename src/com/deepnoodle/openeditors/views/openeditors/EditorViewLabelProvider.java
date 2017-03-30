package com.deepnoodle.openeditors.views.openeditors;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.deepnoodle.openeditors.models.editor.Editor;

class EditorViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public String getColumnText(Object obj, int index) {
		return getText(obj);
	}

	@Override
	public Image getColumnImage(Object obj, int index) {
		return getImage(obj);
	}

	@Override
	public Image getImage(Object obj) {
		Image image;
		if (obj instanceof Editor) {
			image = ((Editor) obj).getReference().getTitleImage();
		} else {
			image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
		return image;
	}

}