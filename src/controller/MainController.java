package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;

import model.ImageResizer;
import model.ImageSize;
import settings.Settings;
import view.ListView;
import view.MainView;
import view.MenuBarView;
import view.NameView;
import view.OptionsView;
import view.SizeView;

/**
 * Diese Klasse verwaltet das ganze Programm
 * @author Oliver Streuli
 * @see Observer
 * @see Observable
 */
public class MainController extends Observable implements Observer {
	
	private static MainController mainControllerObject;

	private final ImageResizer imageResizer;
	private final ListController listController;
	private final MainView mainView;
	private final MenuBarController menuBarController;
	private final NameController nameController;
	
	private final OptionsController optionsController;

	private final SizeController sizeController;
	
	private MainController() {
		this.imageResizer = ImageResizer.getImageResizer(this);
		
		this.listController = new ListController(this);
		this.sizeController = new SizeController();
		this.nameController = new NameController();
		this.optionsController = new OptionsController();
		this.menuBarController = new MenuBarController(this, listController, optionsController);

		this.mainView = MainView.getMainView(this);
		this.addObserver(mainView.getProgressDialog());
	}
	
	public void exit() {
		this.mainView.dispose();
	}
	
	public ListView getListView() {
		return listController.getView();
	}
	
	public MenuBarView getMenuBarView() {
		return menuBarController.getView();
	}
	
	public NameView getNameView() {
		return nameController.getView();
	}
	
	public OptionsView getOptionsView() {
		return optionsController.getView();
	}
	
	public SizeView getSizeView() {
		return sizeController.getView();
	}

	public MainView getView() {
		return mainView;
	}
	
	public void update(Observable o, Object arg) {
		mainView.updateProgressDialog(imageResizer.getCurrent(), imageResizer.getCurrentImage());
		this.setChanged();
		this.notifyObservers(mainView.getProgressDialog());
	}
	
	/**
	 * Diese Klasse starten das skalieren der Bilder
	 */
	private void runImageResizer() {
		// Bilder ausgewählt?
		if (mainView.getSelectedImages().size() < 1) {
			mainView.showInformationDialog("noImagesSelected");
			return;
		}
			
		// Konfiguration zurücksetzten
		imageResizer.resetConfiguration();
		
		// Sind die Benutzereingaben korrekt?
		if (!mainView.saveUserEntries()) {
			mainView.showErrorDialog("emptyInputs");
			return;
		}
		
		// Existiert der Zielordner? Wenn Nein, soll er erstellt werden?
		if ( !(new File(Settings.getOutputDir()).isDirectory()) ) {
			if (mainView.showQuestionDialog("outputDir")) {
				new File(Settings.getOutputDir()).mkdirs();
				imageResizer.setOutputDir(Settings.getOutputDir());
			}
			else {
				return;
			}
		}
		else {
			imageResizer.setOutputDir(Settings.getOutputDir());
		}
		
		// Sollen die Bilder überschrieben werden, falls sie den gleichen Namen wie die neu generierten Bilder haben?
		if ( Settings.getOverwriteType() == ImageResizer.ASK_BEFORE_OVERWRITE_IMAGES ) {
			imageResizer.setOverwriteType( mainView.showQuestionDialog("overwriteImages") ? ImageResizer.OVERWRITE_IMAGES : ImageResizer.DONT_OVERWRITE_IMAGES );
		}
		else {
			imageResizer.setOverwriteType(Settings.getOverwriteType());
		}
		
		// Sollen die Bilder umbennant werden?
		if (Settings.isRename()) {
			imageResizer.setRenameName(Settings.getRenameName());
			imageResizer.setRenameIndex(Settings.getRenameNumberFrom());
		}
		
		// Nach welcher Logik sollen die Bilder verkleinert werden?
		ImageSize size = null;
		if (Settings.getSizeType() == ImageResizer.STANDARD_SIZE)
			size = Settings.getStandardSize();
		else if (Settings.getSizeType() == ImageResizer.DEFINED_SIZE)
			size = Settings.getDefinedSize();
		else if (Settings.getSizeType() == ImageResizer.MAXIMUM_SIZE)
			size = Settings.getMaximumSize();
		else
			return;
		
		// Zeigt den Fortschritts-Dialog an
		mainView.showProgressDialog(true, mainView.getSelectedImages().size());
		
		// Startet das Skalieren der Bilder
		boolean successful = imageResizer.resizeImages(mainView.getSelectedImages(), Settings.getSizeType(), size, Settings.isRename());
		
		// Schliesst den Fortschritts-Dialog an
		mainView.showProgressDialog(false, 0);
		
		// War das Skalieren erfolgreich?
		if (!successful) {
			for (String error : imageResizer.getErrors()) {
				mainView.showErrorDialog(error);
			}
		}
		else {
			mainView.showInformationDialog("successfulResize");
		}
	}
	
	/**
	 * Singleton
	 * @return mainControllerObject
	 */
	public static MainController getMainController() {
		if (mainControllerObject == null) {
			mainControllerObject = new MainController();
		}
		return mainControllerObject;
	}
	
	/**
	 * Diese Klasse verwaltet den "Ausführen"-Button
	 * @author Oliver Streuli
	 *
	 */
	public class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ( e.getActionCommand().equals("run") ) {
				MainController.this.runImageResizer();
			}			
		}	
	}
	
	/**
	 * Diese Klasse sorgt dafür, dass das Programm beenden wird, falls "Escpae" gedrückt wird
	 * @author Oliver Streuli
	 *
	 */
	public class EscapeAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {
			MainController.this.exit();
		}
	}
}
