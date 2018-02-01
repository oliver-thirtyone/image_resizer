package settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import model.ImageResizer;
import model.ImageSize;
import model.ResizableImage;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Diese Klasse verwaltet die Eigenschaften, die der Benutzer gewählt hat
 * @author Oliver Streuli
 *
 */
public class Settings {
	
	private static final Settings settingsObject  = new Settings();
	private Boolean definedHeightSelected;
	private ImageSize definedSize;
	private Boolean definedWidthSelected;
	
	private Preferences imagePreferences;
	private ArrayList<ResizableImage> images;
	private String inputDir;
	
	private Document languageFile;
	private String languageFilePath;
	private ImageSize maximumSize;
	private String outputDir;
	private int overwriteType;
	private Preferences preferences;
	private Boolean rename;
	
	private String renameName;
	private Integer renameNumberFrom;
	private int sizeType;
	
	private ImageSize standardSize;
		
	private Settings() {		
		this.preferences = Preferences.userRoot().node("/ImageResizer");
		this.imagePreferences = Preferences.userRoot().node("/ImageResizer/images");
		this.languageFile = new Document();
		this.images = new ArrayList<ResizableImage>();
		
		this.loadUserSettings();
		this.loadImages();
		
		// Size
		this.definedHeightSelected = (definedSize.getHeight() != -1) ? true : false;
		this.definedWidthSelected  = (definedSize.getWidth()  != -1) ? true : false;
	}
	
	/**
	 * Diese Klasse lädt die zuletzt gewählten Bilder
	 */
	private void loadImages() {
		String[] keys;
		try {
			keys = imagePreferences.keys();
		} catch (BackingStoreException e) {
			keys = new String[0];
			e.printStackTrace();
		}
		
		for (String key : keys) {
			File imageFile = new File( preferences.get(key, key) );
			if (imageFile.isFile())
				this.images.add( new ResizableImage(imageFile) );
		}
		
		try {
			this.imagePreferences.clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Diese Klasse lädt die Sprache
	 * @param xmlFile Pfad zur XML-Datei, das die Sprache enthält
	 * @return Gibt <code>true</code> zurück, falls die Sprache geladen werden konnte
	 */
	private boolean loadLanguageFile(String xmlFile) {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		boolean ret = false;

		try {
			doc = builder.build( new FileInputStream(xmlFile) );
		} catch (FileNotFoundException e) {
			System.err.println("Language file not found!");
			e.printStackTrace();
		} catch (JDOMException e) {
			System.err.println("Error while reading language file!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Couldn't read language file!");
			e.printStackTrace();
		}
		
		if ( doc != null ) {
			this.languageFile = doc;
			ret = true;
		}
		return ret;
	}
	
	/**
	 * Lädt die zuletzt gewählten Benutzereingaben
	 */
	private void loadUserSettings() {
		// Global
		{
			this.languageFilePath = preferences.get("languageFilePath", "language/german.xml");
			this.inputDir = preferences.get("inputDir", "");
			this.outputDir = preferences.get("outputDir", "C:/Thumbnails");
		}
		
		// Size
		{
			this.sizeType = preferences.getInt("sizeType", ImageResizer.STANDARD_SIZE);
			
			int standardWidth = preferences.getInt("standardWidth", 100);
			int standardHeight = preferences.getInt("standardHeight", 100);
			this.standardSize = new ImageSize(standardWidth, standardHeight);
			
			int definedWidth = preferences.getInt("definedWidth", -1);
			int definedHeight = preferences.getInt("definedHeight", -1);
			this.definedSize = new ImageSize(definedWidth, definedHeight);
			
			int maximumWidth = preferences.getInt("maximumWidth", -1);
			int maximumHeight = preferences.getInt("maximumHeight", -1);
			this.maximumSize = new ImageSize(maximumWidth, maximumHeight);
		}
		
		// Name
		{
			this.rename = preferences.getBoolean("rename", false);
			this.renameName = preferences.get("renameName", "");
			this.renameNumberFrom = preferences.getInt("renameNumberFrom", -1);
		}
		
		// Options
		{
			this.overwriteType = preferences.getInt("overwriteType", ImageResizer.ASK_BEFORE_OVERWRITE_IMAGES);
		}
	}
	
	/**
	 * Speichert die Bilder, damit sie beim nächsten Start noch vorhanden sind
	 */
	private void saveImages() {
		for (ResizableImage image : images) {
			this.imagePreferences.put(image.getPath(), image.getPath());
		}
	}
	
	/**
	 * Speichert die Benutzereingaben, damit sie beim nächsten Start noch vorhanden sind
	 */
	private void saveUserSettings() {
		// Global
		{
			this.preferences.put("languageFilePath", Settings.getLanguageFilePath());
			this.preferences.put("inputDir", Settings.getInputDir());
			this.preferences.put("outputDir", Settings.getOutputDir());
		}
		
		// Size
		{
			this.preferences.putInt("sizeType", ImageResizer.STANDARD_SIZE);
			
			this.preferences.putInt("standardWidth", Settings.getStandardSize().getWidth());
			this.preferences.putInt("standardHeight", Settings.getStandardSize().getHeight());
			
			this.preferences.putInt("definedWidth", Settings.getDefinedSize().getWidth());
			this.preferences.putInt("definedHeight", Settings.getDefinedSize().getHeight());
			
			this.preferences.putInt("maximumWidth", Settings.getMaximumSize().getWidth());
			this.preferences.putInt("maximumHeight", Settings.getMaximumSize().getHeight());
		}
		
		// Name
		{
			this.preferences.putBoolean("rename", Settings.isRename());
			this.preferences.put("renameName", Settings.getRenameName());
			this.preferences.putInt("renameNumberFrom", Settings.getRenameNumberFrom());
		}
		
		// Options
		{
			this.preferences.putInt("overwriteType", Settings.getOverwriteType());
		}
	}

	public static void addImages(ArrayList<ResizableImage> newImages) {
		for (ResizableImage image : newImages) {
			settingsObject.images.add(image);
		}	
	}
	
	public static void clearImages() {
		settingsObject.images.clear();
	}
	
	public static ImageSize[] fillStandardSizes() {
		return new ImageSize[] {
				new ImageSize(100, 100),
				new ImageSize(150, 150),
				new ImageSize(300, 300),
				new ImageSize(640, 480),
				new ImageSize(800, 600),
				new ImageSize(1024, 768),
				new ImageSize(1152, 864),
				new ImageSize(1280, 720),
				new ImageSize(1280, 768),
				new ImageSize(1280, 960),
				new ImageSize(1280, 1024),
				new ImageSize(1600, 1200)
		};
	}
	
	public static ImageSize getDefinedSize() {
		return settingsObject.definedSize;
	}
	
	public static ArrayList<ResizableImage> getImages() {
		return settingsObject.images;
	}

	public static String getInputDir() {
		return settingsObject.inputDir;
	}
	
	public static Document getLanguageFile() {			
		return settingsObject.languageFile;
	}
	
	public static String getLanguageFilePath() {
		return settingsObject.languageFilePath;
	}
	
	public static ImageSize getMaximumSize() {
		return settingsObject.maximumSize;
	}

	public static String getOutputDir() {
		return settingsObject.outputDir;
	}
		
	public static int getOverwriteType() {
		return settingsObject.overwriteType;
	}
	
	public static String getRenameName() {
		return settingsObject.renameName;
	}
	
	public static Integer getRenameNumberFrom() {
		return settingsObject.renameNumberFrom;
	}
	
	public static int getSizeType() {
		return settingsObject.sizeType;
	}
	
	public static ImageSize getStandardSize() {
		return settingsObject.standardSize;
	}

	public static Boolean isDefinedHeightSelected() {
		return settingsObject.definedHeightSelected;	
	}

	public static Boolean isDefinedWidthSelected() {
		return settingsObject.definedWidthSelected;	
	}
	
	public static Boolean isRename() {
		return settingsObject.rename;
	}
	
	public static boolean loadLanguage() {
		return Settings.loadLanguage(settingsObject);
	}
	
	public static void removeImage(ResizableImage image) {
		settingsObject.images.remove(image);
	}

	public static void saveSettings() {
		settingsObject.saveUserSettings();
		settingsObject.saveImages();
	}
	
	public static void setDefinedHeightSelected(Boolean definedHeightSelected) {
		settingsObject.definedHeightSelected = definedHeightSelected;	
	}

	public static void setDefinedSize(ImageSize size) {
		settingsObject.definedSize = size;
	}

	public static void setDefinedWidthSelected(Boolean definedWidthSelected) {
		settingsObject.definedWidthSelected = definedWidthSelected;	
	}

	public static void setInputDir(String inputDir) {
		settingsObject.inputDir = inputDir;
	}

	public static void setLanguageFilePath(String languageFilePath) {
		settingsObject.languageFilePath = languageFilePath;
	}

	public static void setMaximumSize(ImageSize size) {
		settingsObject.maximumSize = size;
	}

	public static void setOutputDir(String outputDir) {
		settingsObject.outputDir = outputDir;
	}

	public static void setOverwriteType(int overwriteType) {
		settingsObject.overwriteType = overwriteType;
	}

	public static void setRename(Boolean rename) {
		settingsObject.rename = rename;
	}

	public static void setRenameName(String renameName) {
		settingsObject.renameName = renameName;
	}
	
	public static void setRenameNumberFrom(Integer renameNumberFrom) {
		settingsObject.renameNumberFrom = renameNumberFrom;
	}
	
	public static void setSizeType(int sizeType) {
		settingsObject.sizeType = sizeType;
	}
	
	public static void setStandardSize(ImageSize size) {
		settingsObject.standardSize = size;
	}
	
	private static boolean loadLanguage(Settings settings) {
		return settings.loadLanguageFile(getLanguageFilePath());
	}
}
