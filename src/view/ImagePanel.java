package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.ImageSize;
import model.ResizableImage;
import controller.ListController;

/**
 * Für jedes Bild in der BilderListe wird ein <ImagePanel> angezeigt
 * @author Oliver Streuli
 * @see JPanel
 */
public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final ImageSize size = new ImageSize(100, 100);
	private final Dimension dimension = new Dimension(size.getWidth(), size.getHeight() + 30);
	private final ListController controller;
	
	private ResizableImage image;
	private JCheckBox imageCheckbox;
	private JLabel imageLabel;
	
	/**
	 * @param image Das Bild, dass in diesem Panel angezeigt wird
	 * @param controller Der Kontroller, der dieses Panel verwaltet
	 */
	public ImagePanel(ResizableImage image, ListController controller) {
		this.setLayout(null);
		this.setPreferredSize(dimension);
		this.addMouseListener(controller);
		
		this.image = image;
		this.controller = controller;
		
		this.imageLabel = new JLabel(addImage());
		this.imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.imageLabel.setBounds(0, 0, size.getWidth(), size.getHeight());
		
		this.imageCheckbox = new JCheckBox(image.getName());
		this.imageCheckbox.setBounds(0, size.getHeight() + 5, size.getWidth(), 20);
		this.imageCheckbox.setSelected(true);
		
		this.add(imageLabel);
		this.add(imageCheckbox);
	}
	
	public ResizableImage getImage() {
		return image;
	}
	
	public boolean isSelected() {
		return imageCheckbox.isSelected();
	}
	
	/**
	 * Diese Methode öffnet das Bild, mit dem standard Bild-Anzeige Programm
	 */
	public void openImage() {
		try {
			// In dieser Version funktioniert diese Aktion nur unter Windows
			Runtime.getRuntime().exec("cmd /c \"" + this.getImage().getPath() + "\"");
		} catch (IOException e1) {
			controller.getMainController().getView().showErrorDialog("openImage");
			e1.printStackTrace();
		}
	}
	
	/**
	 * Diese Methode benennt das Bild um
	 */
	public void renameImage() {
		String renameTo = controller.getMainController().getView().showInputDialog("renameImage");
		
		if (renameTo == null)
			return;
		
		if (! getImage().rename(renameTo) )
			controller.getMainController().getView().showErrorDialog("renameImage");
		else
			imageCheckbox.setText(image.getName());
	}
	
	public void setSelected(boolean selected) {
		imageCheckbox.setSelected(selected);
	}
	
	/**
	 * Diese Methode erstellt ein Bild aus der Bilddatei, skalliert es so, dass es in ein Feld von 100*100 Pixel passt und gib es zurück
	 * @return Das Bild aus der Datei
	 */
	private ImageIcon addImage() {
		int width = -1, height = -1;
		ImageIcon icon = new ImageIcon(image.getPath());
		Image image = icon.getImage();
		
		if(image.getWidth(null) > image.getHeight(null))
			width = size.getWidth();
		else
			height = size.getHeight();
	
		return new ImageIcon( image.getScaledInstance(width, height, Image.SCALE_FAST) );
	}
}