package view;

import java.util.ArrayList;
import java.util.Observable;

import model.ResizableImage;

import org.jdom.Element;

import settings.Settings;
import classes.ScrollableFlowPanel;
import classes.dialog.ProgressDialog;
import controller.ListController;

/**
 * Diese Klasse fügt die Bilder zur Bilderliste hinzu.
 * @author Oliver Streuli
 * @see Observable
 */
public class ImageAdder extends Observable {
	
	private final ScrollableFlowPanel imageList;
	private final ListController listController;
	private final ProgressDialog progressDialog;
	
	/**
	 * @param imageList Die Liste, in die die Bilder eingefügt werden sollen
	 * @param listController Der Controller der Liste
	 */
	protected ImageAdder(ScrollableFlowPanel imageList, ListController listController) {
		this.imageList = imageList;
		this.listController = listController;
		this.progressDialog = new ProgressDialog(null, "addImages");
		this.addObserver(progressDialog);
	}
	
	/**
	 * Diese Methode fügt neue Bilder zur Liste hinzu.
	 * @param newImages Die neuen Bilder
	 */
	protected void addImages(ArrayList<ResizableImage> newImages) {		
		progressDialog.setMaxValue(newImages.size());
		progressDialog.showDialog();
		for (ResizableImage image : newImages) {
			this.progressDialog.increaseCurrentValue();
			this.progressDialog.setCurrentItem(image);

			this.setChanged();
			this.notifyObservers(progressDialog);
			
			this.imageList.add(new ImagePanel(image, listController));
		}
		progressDialog.dispose();
	}

	/**
	 * Diese Methode lädt den Text
	 */
	protected void loadText() {
		Element progressDialogs = Settings.getLanguageFile().getRootElement().getChild("progressDialogs");
		{
			progressDialog.setTitle(progressDialogs.getChild("addImages").getText());
		}
	}
}
