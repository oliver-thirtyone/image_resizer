package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.ImageResizer;
import settings.Settings;
import view.MenuBarView;

/**
 * Diese Klasse verwaltet alle Aktionen, die aus dem <code>MenuBarView</code> aufgerufen wurden
 * @author Oliver Streuli
 * @see ActionListener
 */
public class MenuBarController implements ActionListener {

	private final ListController listController;
	private final MainController mainController;
	private final OptionsController optionsController;
	private final MenuBarView view;
	
	/**
	 * @param mainController Der Haupt-Kontroller
	 * @param listController Der Kontroller der Bilderliste
	 * @param optionsController Der Kontrollder der Optionen
	 */
	protected MenuBarController(MainController mainController, ListController listController, OptionsController optionsController) {
		this.view = new MenuBarView(this);
		this.mainController = mainController;
		this.listController = listController;
		this.optionsController = optionsController;
	}
	
	public void actionPerformed(ActionEvent e) {
		// Datei
		if (e.getActionCommand().equals("addImages")) {
			listController.selectImages();
		}
		else if (e.getActionCommand().equals("language.german")) {
			Settings.setLanguageFilePath("language/german.xml");
			mainController.getView().loadText();
		}
		else if (e.getActionCommand().equals("language.english")) {
			Settings.setLanguageFilePath("language/english.xml");
			mainController.getView().loadText();
		}
		else if (e.getActionCommand().equals("exit")) {
			mainController.exit();
		}
		
		// Bearbeiten
		else if (e.getActionCommand().equals("selectAll")) {
			listController.getView().selectAllImages(true);
		}
		else if (e.getActionCommand().equals("deselectAll")) {
			listController.getView().selectAllImages(false);
		}
		else if (e.getActionCommand().equals("clearList")) {
			listController.getView().clearImages(true);
		}
		
		// Optionen
		else if (e.getActionCommand().equals("overwriteImages")) {
			optionsController.getView().selectOverwriteType(ImageResizer.OVERWRITE_IMAGES);
			Settings.setOverwriteType(ImageResizer.OVERWRITE_IMAGES);
		}
		else if (e.getActionCommand().equals("askBeforeRun")) {
			optionsController.getView().selectOverwriteType(ImageResizer.ASK_BEFORE_OVERWRITE_IMAGES);
			Settings.setOverwriteType(ImageResizer.ASK_BEFORE_OVERWRITE_IMAGES);
		}
		else if (e.getActionCommand().equals("dontOverwriteImages")) {
			optionsController.getView().selectOverwriteType(ImageResizer.DONT_OVERWRITE_IMAGES);
			Settings.setOverwriteType(ImageResizer.DONT_OVERWRITE_IMAGES);
		}
		else if (e.getActionCommand().equals("selectOutputDir")) {
			optionsController.selectOutputDir();
		}
		
		// Hilfe
		else if (e.getActionCommand().equals("manual")) {
			mainController.getView().showInformationDialog("notAvailable");
		}
		else if (e.getActionCommand().equals("about")) {
			mainController.getView().showInformationDialog("notAvailable");
		}
		else if (e.getActionCommand().equals("visit")) {
			mainController.getView().showInformationDialog("notAvailable");
		}
	}
	
	protected MenuBarView getView() {
		return view;
	}

}
