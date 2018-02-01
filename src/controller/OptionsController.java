package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import model.ImageResizer;
import settings.Settings;
import view.OptionsView;

/**
 * Diese Klasse verwaltet alle Aktionen, die aus dem <code>OptionsView</code> aufgerufen wurden
 * @author Oliver Streuli
 * @see ActionListener
 */
public class OptionsController implements ActionListener {

	private final OptionsView view;
	private final JFileChooser dirChooser;
	
	protected OptionsController() {
		this.view = new OptionsView(this);
		this.dirChooser = new JFileChooser(Settings.getOutputDir());
		this.dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.dirChooser.setMultiSelectionEnabled(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("overwriteImages")) {
			Settings.setOverwriteType(ImageResizer.OVERWRITE_IMAGES);
		}
		else if (e.getActionCommand().equals("askBeforeRun")) {
			Settings.setOverwriteType(ImageResizer.ASK_BEFORE_OVERWRITE_IMAGES);
		}
		else if (e.getActionCommand().equals("dontOverwriteImages")) {
			Settings.setOverwriteType(ImageResizer.DONT_OVERWRITE_IMAGES);
		}
		else if (e.getActionCommand().equals("selectOutputDir")) {
			this.selectOutputDir();
		}
	}
	
	public void selectOutputDir() {
		if (dirChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
			Settings.setOutputDir(dirChooser.getSelectedFile().getAbsolutePath());
			view.refreshOutputDir();				
		}
	}
	
	protected OptionsView getView() {
		return view;
	}
}
