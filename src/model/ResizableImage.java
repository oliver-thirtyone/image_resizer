package model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Diese Klasse enthält die skalierbaren Bilder
 * @author Oliver Streuli
 *
 */
public class ResizableImage {

	private File file;
	private String name;
	private String path;
	private ImageSize size;
	
	/**
	 * @param file Die Datei, die das Bild enthält
	 */
	public ResizableImage(File file) {
		this.loadImageAttributes(file);
	}

	private void loadImageAttributes(File file) {
		this.setFile(file);
		this.setSize(file);
		this.setName(file);
		this.setPath(file);
	}

	public File getFile() {
		return file;
	}

	public int getHeight() {
		return size.getHeight();
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public ImageSize getSize() {
		return size;
	}

	public int getWidth() {
		return size.getWidth();
	}
	
	/**
	 * Diese Methode benennt das Bild um
	 * @param name Der neue Name
	 * @return Gibt <code>true</code> zurück, falls das Umbennen erfolgreich war.
	 */
	public boolean rename(String name) {
		boolean ret = false;
		
		String newName = file.getName().replaceFirst(getName(), name);
		File renamedFile = new File(file.getParentFile().getAbsolutePath() + "/" + newName);
		
		if (file.renameTo(renamedFile)) {
			ret = true;
			loadImageAttributes(renamedFile);
		}

		return ret;
	}
	
	/**
	 * Diese Methode skaliert das Bild
	 * @param newFile Die Bild-Datei, die erstellt werden soll
	 * @param targetSize Die Grösse, die das Bild haben soll
	 * @return Gibt <code>true</code> zurück, falls das Skalieren erfolgreich war.
	 */
	public boolean resize(File newFile, ImageSize targetSize) {
		boolean ret = true;

		String path = file.getAbsolutePath();
		String format = path.substring(path.length() - 3, path.length()).toLowerCase();
		
		ImageIcon image = new ImageIcon(path);
		ImageIcon thumbnail = new ImageIcon(image.getImage().getScaledInstance(targetSize.getWidth(), targetSize.getHeight(), Image.SCALE_SMOOTH));

		BufferedImage bi = new BufferedImage(thumbnail.getIconWidth(), thumbnail.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics graphic = bi.getGraphics();
		graphic.drawImage(thumbnail.getImage(), 0, 0, null);

		try {
			ImageIO.write(bi, format, newFile);
		} catch (IOException e) {
			e.printStackTrace();
			ret = false;
		}

		return ret;
	}
	
	/**
	 * Diese Methode ermittelt die entsprechende Zielgrösse und ruft anschliessend die Methode auf, die das Bild skaliert
	 * @param newFile Die Bild-Datei, die erstellt werden soll
	 * @param targetSize Die Grösse, die das Bild haben soll
	 * @return Gibt <code>true</code> zurück, falls das Skalieren erfolgreich war.
	 */
	public boolean resizeWithMaximumSize(File newFile, ImageSize targetSize) {
		int width = -1;
		int height = -1;

		if (this.getWidth() > this.getHeight())
			width = targetSize.getWidth();
		else
			height = targetSize.getHeight();

		return resize(newFile, new ImageSize(width, height));
	}

	public void setSize(File file) {
		ImageIcon imageicon = new ImageIcon(file.getName());
		this.size = new ImageSize(imageicon.getIconWidth(), imageicon.getIconHeight());
	}

	@Override
	public String toString() {
		return getPath();
	}

	private void setFile(File file) {
		this.file = file;
	}

	private void setName(File file) {
		String tmp = file.getName();
		this.name = tmp.substring(0, tmp.length() - 4);
	}

	private void setPath(File file) {
		this.path = file.getAbsolutePath();
	}
}
