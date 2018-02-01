package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Diese Klasse ist f�r das Sklaieren und Umbennen der Bilder zust�ndig
 * @author Oliver Streuli
 * @see Observable
 */
public class ImageResizer extends Observable {
	
	// Logiken nach denen die Bilder skaliert werden k�nnen
	public static final int STANDARD_SIZE = 1;
	public static final int DEFINED_SIZE = 2;
	public static final int MAXIMUM_SIZE = 3;
	
	// Die Art, wir und ob die Bilder �berschrieben werden sollen
	public static final int OVERWRITE_IMAGES = 1;
	public static final int ASK_BEFORE_OVERWRITE_IMAGES = 2;
    public static final int DONT_OVERWRITE_IMAGES = 3;
    
    // Die Formate, die ein Bild haben darf
    public static final String IMAGE_JPG = "jpg";
    public static final String IMAGE_GIF = "gif";
	public static final String IMAGE_PNG = "png";
	
	// Die Mindest- und Maximalgr�sse der Bilder
	public static final ImageSize MIN_SIZE = new ImageSize(5, 5);
	public static final ImageSize MAX_SIZE = new ImageSize(3000, 3000);
	
	private static ImageResizer imageResizerObject;
	
	private int count;
	private int current;
	private String currentImage;
	private ArrayList<String> errors;
	private String outputDir;
	
	private int overwriteType;
	private int renameIndex;
	private String renameName;
	
	/**
	 * @param observer Der Beobachter des ImageResizer, der �ber den Fortschritt des aktuellen Skalier-Prozesses informiert wird
	 */
	private ImageResizer(Observer observer) {
		this.errors = new ArrayList<String>();
		this.resetConfiguration();
		this.addObserver(observer);
	}
	
	/**
	 * Diese Methode f�gt neue Fehler hinzu
	 * @param error Der Fehler der enstanden ist
	 */
	public void error(String error) {
		this.errors.add(error);
	}
	
	public int getCount() {
		return count;
	}
	
	public int getCurrent() {
		return current;
	}

	public String getCurrentImage() {
		return currentImage;
	}
	
	public ArrayList<String> getErrors() {
		return errors;
	}

	public String getOutputDir() {
		return outputDir;
	}
	
	public int getOverwriteType() {
		return overwriteType;
	}
	
	public int getRenameIndex() {
		return renameIndex;
	}

	public String getRenameName() {
		return renameName;
	}
	
	/**
	 * Diese Methode setzt alle Konfigurationen zur�ck
	 */
	public void resetConfiguration() {
		this.errors.clear();
		this.outputDir = null;
		this.renameName = null;
		this.renameIndex = -1;
		this.overwriteType = -1;
		
		this.setCount(0);
		this.setCurrent(0);
	}
	
	/**
	 * Diese Funktion f�hrt das Skalieren der Bilder durch
	 * @param images Der Bilder, die skaliert werden sollen
	 * @param type Die Logik, nach der die Bilder skaliert werden sollen
	 * @param size Die Gr�sse, auf die Bilder skaliert werden sollen
	 * @param rename Sollen die Bilder auch umbennant werden?
	 * @return Gibt <code>true</code> zur�ck, falls der Vorgang erfolgreich war
	 */
	public boolean resizeImages(ArrayList<ResizableImage> images, int type, ImageSize size, boolean rename) {	
		// Zielordner gesetzt?
		if (getOutputDir() == null)
			error("noOutputDir");
		
		// Die Art des �berschreibens gesetzt?
		if (getOverwriteType() < 1)
			error("emptyInputs");
		
		// Sollen die Bilder umbenannt werden?
		if (rename) {
			// Name zum Umbenennen gesetzt?
			if (getRenameName() == null)
				error("renameName");
			// Index zum Umbennenen gesetzt?
			if (getRenameIndex() < 0) {
				error("renameIndex");
			}
		}
		
		// Falls Fehler enstanden sind, wird hier abgebrochen
		if (!errors.isEmpty())
			return false;
		
		// Zielgr�sse und Anzahl Bilder setzten
		ImageSize targetSize = setSize(size.getWidth(), size.getHeight());
		this.setCount(images.size());
		
		for (ResizableImage image : images) {			
			File newFile = new File(getOutputDir() + "/" + image.getFile().getName());
			
			// Bild umbenennen, falls gew�hlt
			newFile = (rename) ? rename(newFile, image) : newFile;
			
			// Bild umbennenn, damit das vorhanden Bild nicht �berschrieben wird (falls "Bilder nicht �berschreiben" gew�hlt);
			newFile = (newFile.isFile() && DONT_OVERWRITE_IMAGES == getOverwriteType()) ? dontOverwrite(newFile) : newFile;
			
			// Den �berwacher informatieren, dass nun ein neues Bild bearbeitet wird
			this.increaseCurrent();
			this.setCurrentImage(newFile.getAbsoluteFile().toString());
			this.setChanged();
			this.notifyObservers();
			
			// Bild skalieren
			if (type == STANDARD_SIZE || type == DEFINED_SIZE) {
				if (! image.resize(newFile, targetSize) )
					error("resize");
			}
			else if (type == MAXIMUM_SIZE) {
				if (! image.resizeWithMaximumSize(newFile, targetSize) )
					error("resize");
			}
			
			// Falls Fehler enstanden sind, wird hier abgebrochen
			if (! errors.isEmpty() )
				break;
		}	
		return errors.isEmpty() ? true : false;
	}
	
	/**
	 * Bennent das Bild um und f�gt den Index hinzu
	 * @param file Das File, welches erstellt wird
	 * @param image Das Bild, welches umbenennt wird
	 * @return Das umbennante File 
	 */
	private File rename(File file, ResizableImage image) {
		String newName = file.getName().replaceFirst(image.getName(), getRenameName()+getRenameIndex());
		increaseRenameIndex();
		return new File(file.getParentFile().getAbsolutePath() + "/" + newName);
	}
	
	/**
	 * Sorgt daf�r, dass die Bilder nicht �berschrieben werden
	 * @param file Das File, das nicht �berschrieben werden soll
	 * @return Das umbennante File 
	 */
	private File dontOverwrite(File file) {
		String newFile = "";
		int index = 1;
			
		do {
			String fileName = file.getName();
			String name = fileName.substring(0, fileName.length() - 4);
			String format = fileName.substring(fileName.length() - 4, fileName.length()).toLowerCase();
			
			newFile = file.getParentFile().getAbsolutePath() + "/" + name  + "(" + index + ")" + format;
			index++;
		} while (new File(newFile).isFile());
		
		return new File(newFile);
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public void setOverwriteType(int overwriteType) {
		this.overwriteType = overwriteType;
	}

	public void setRenameIndex(int renameIndex) {
		renameIndex = (renameIndex < 0)		? 0		: renameIndex;
		renameIndex = (renameIndex > 10000)	? 10000	: renameIndex;
		this.renameIndex = renameIndex;
	}

	public void setRenameName(String renameName) {
		this.renameName = renameName;
	}

	private void increaseCurrent() {
		this.setCurrent(getCurrent() + 1);
	}
	
	private void increaseRenameIndex() {
		this.setRenameIndex(getRenameIndex() + 1);
	}

	private void setCount(int count) {
		this.setCurrent(0);
		this.count = count;
	}

	private void setCurrent(int current) {
		this.current = current;
	}

	private void setCurrentImage(String currentImage) {
		this.currentImage = currentImage;
	}
	
	/**
	 * Erstellt eine valide Gr�sse, die innerhablt der Minimal- und Maximalgr�sse ist
	 * @param width Die gew�nschte Breite
	 * @param height Die gew�nschte H�he
	 * @return Eine valide Gr�sse
	 */
	private ImageSize setSize(int width, int height) {
		if (width != -1) {
			width = (width < MIN_SIZE.getWidth()) ? MIN_SIZE.getWidth() : width;
			width = (width > MAX_SIZE.getWidth()) ? MAX_SIZE.getWidth() : width;
		}
		if (height != -1) {
			height = (height < MIN_SIZE.getHeight()) ? MIN_SIZE.getHeight() : height;
			height = (height > MAX_SIZE.getHeight()) ? MAX_SIZE.getHeight() : height;
		}
		return new ImageSize(width, height);
	}
	
	/**
	 * Singleton
	 */
	public static ImageResizer getImageResizer(Observer observer) {
		if (imageResizerObject == null) {
			imageResizerObject = new ImageResizer(observer);
		}
		return imageResizerObject;
	}	
}
