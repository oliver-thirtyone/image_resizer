package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import model.ImageResizer;
import settings.Settings;
import view.SizeView;

/**
 * Diese Klasse verwaltet alle Aktionen, die aus dem <code>SizeView</code> aufgerufen wurden
 * @author Oliver Streuli
 *
 */
public class SizeController implements ActionListener, ItemListener {
	
	private final SizeView view;
	
	protected SizeController() {
		this.view = new SizeView(this);	
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("standardSize")) {
			Settings.setSizeType(ImageResizer.STANDARD_SIZE);
			view.enableStandardSize();
		}
		else if (e.getActionCommand().equals("defineSize")) {
			Settings.setSizeType(ImageResizer.DEFINED_SIZE);
			view.enableDefineSize();
		}
		else if (e.getActionCommand().equals("maximumSize")) {
			Settings.setSizeType(ImageResizer.MAXIMUM_SIZE);
			view.enableMaximumSize();
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		JCheckBox cb = (JCheckBox) e.getItemSelectable();
		if (cb.getActionCommand().equals("defineHeight")) {
			if ( cb.isSelected() ) {
					Settings.setDefinedHeightSelected(true);
			}
			else {
				if (Settings.isDefinedWidthSelected())
					Settings.setDefinedHeightSelected(false);
			}
			view.enableDefineSize();
		}
		else if (cb.getActionCommand().equals("defineWidth")) {
			if ( cb.isSelected() ) {
					Settings.setDefinedWidthSelected(true);
			}
			else {
				if (Settings.isDefinedHeightSelected())
					Settings.setDefinedWidthSelected(false);
			}
			view.enableDefineSize();
		}	
	}

	protected SizeView getView() {
		return view;
	}
}
