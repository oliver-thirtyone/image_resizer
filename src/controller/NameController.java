package controller;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import settings.Settings;
import view.NameView;

/**
 * Diese Klasse verwaltet alle Aktionen, die aus dem <code>NameView</code> aufgerufen wurden
 * @author Oliver Streuli
 * @see ItemListener
 */
public class NameController implements ItemListener {

	private final NameView view;
	
	protected NameController() {
		this.view = new NameView(this);	
	}
	
	public void itemStateChanged(ItemEvent e) {
		JCheckBox cb = (JCheckBox) e.getItemSelectable();
		if (cb.getActionCommand().equals("rename")) {
			if ( cb.isSelected() )
				Settings.setRename(true);
			else
				Settings.setRename(false);
			view.enableRename();
		}	
	}

	protected NameView getView() {
		return view;
	}

}
