package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import model.ImageResizer;
import model.ResizableImage;
import settings.Settings;
import view.ImagePanel;
import view.ListView;

/**
 * Diese Klasse verwaltet alle Aktionen, die aus dem <code>ListView</code> aufgerufen wurden
 * @author Oliver Streuli
 * @see MouseAdapter
 * @see ActionListener
 */
public class ListController extends MouseAdapter implements ActionListener {
	
	private final ListView view;
	private final MainController mainController;
	private final JFileChooser imageChooser;
	
	/**
	 * @param mainController Der Haupt-Kontroller
	 */
	protected ListController(MainController mainController) {
		this.view = new ListView(this);
		this.mainController = mainController;
		this.imageChooser = new JFileChooser(Settings.getInputDir());
		this.imageChooser.setFileFilter(new Filter());
		this.imageChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.imageChooser.setMultiSelectionEnabled(true);
		this.imageChooser.setAcceptAllFileFilterUsed(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("selectAll")) {
			view.selectAllImages(true);
		}
		else if (e.getActionCommand().equals("deselectAll")) {
			view.selectAllImages(false);
		}
		else if (e.getActionCommand().equals("clearList")) {
			view.clearImages(true);
		}
		else if (e.getActionCommand().equals("addImages")) {
			this.selectImages();
		}	
		else if (e.getActionCommand().equals("image.open")) {
			view.getSelectedImagePanel().openImage();
		}
		else if (e.getActionCommand().equals("image.rename")) {
			view.getSelectedImagePanel().renameImage();
		}
		else if (e.getActionCommand().equals("image.turn90Degrees")) {
			mainController.getView().showInformationDialog("notAvailable");
		}
		else if (e.getActionCommand().equals("image.removeFromList")) {
			view.removeImage(view.getSelectedImagePanel(), true);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			ImagePanel image = (ImagePanel) e.getComponent();		
			image.setSelected( image.isSelected() ? false : true );
		}
		else if (e.getButton() == MouseEvent.BUTTON3) {
			ImagePanel image = (ImagePanel) e.getComponent();
			view.getPopupMenu(image).show(image, e.getX(), e.getY());
		}
	}
	
	/**
	 * Diese Klasse zeigt einen Dateiauswahl-Dialog an, in dem man die Bilder auswählen kann, die hinzugefügt werden sollen
	 */
	public void selectImages() {
		if (imageChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
			this.addImages();
		}
	}
	
	/**
	 * Diese Klasse fügt die ausgewählten Bilder hinzu
	 */
	private void addImages() {
		ArrayList<ResizableImage> newImages = new ArrayList<ResizableImage>();
		for (File file : imageChooser.getSelectedFiles()) {
			if (file.isFile()) {
				newImages.add(new ResizableImage(file));
			}
		}
		Settings.setInputDir(imageChooser.getCurrentDirectory().getAbsolutePath());
		Settings.addImages(newImages);
		view.addImages(newImages);
	}

	public ListView getView() {
		return view;
	}
	
	public MainController getMainController() {
		return mainController;
	}
	
	/**
	 * Diese Klasse sorgt dafür, dass nur Ordner und Bilder im Dateiauswahl-Dialog angezeigt werden
	 * @author Oliver Streuli
	 *
	 */
	private class Filter extends FileFilter {
		@Override
		public boolean accept(File f) {
			Boolean ret = false;		
			if (f.isDirectory())
				ret = true;
			else if (f.getName().toLowerCase().endsWith(ImageResizer.IMAGE_JPG))
				ret = true;
			else if (f.getName().toLowerCase().endsWith(ImageResizer.IMAGE_GIF))
				ret = true;
			else if (f.getName().toLowerCase().endsWith(ImageResizer.IMAGE_PNG))
				ret = true;
			return ret;
		}
		@Override
		public String getDescription() {
			return view.getFilterName() + " (" + ImageResizer.IMAGE_JPG + ", " + ImageResizer.IMAGE_GIF + ", " + ImageResizer.IMAGE_PNG + ")";
		}
	}
}
