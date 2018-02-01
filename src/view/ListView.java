package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import model.ResizableImage;

import org.jdom.Element;

import settings.Settings;
import classes.GridBagHelper;
import classes.ScrollableFlowPanel;
import controller.ListController;

/**
 * Diese Klasse zeigt die Bilderliste an
 * @author Oliver Streuli
 * @see JPanel
 */
public class ListView extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JButton addImages;
	private JPanel buttons;
	private JButton clearList;
	private JButton deselectAll;
	private String filterName;
	private ImageAdder imageAdder;
	private ScrollableFlowPanel imageList;
	
	private JPopupMenu popupMenu;
	private JMenuItem open;
	private JMenuItem rename;
	private JMenuItem turn90Degrees;
	private JMenuItem removeFromList;
	
	private JScrollPane scrollPane;
	private JButton selectAll;
	private ImagePanel selectedImagePanel;
	
	/**
	 * @param controller Der Controller diese Panels
	 */
	public ListView(ListController controller) {
		this.setLayout(new BorderLayout());
		
		this.imageList = new ScrollableFlowPanel();
		this.imageAdder = new ImageAdder(imageList, controller);
		this.scrollPane = new JScrollPane(imageList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);	
		
		this.filterName = "images";
		this.selectedImagePanel = null;
		
		// Das PopupMenu erstellenb
		this.popupMenu = new JPopupMenu();
		{
			open = new JMenuItem("open");
			open.setActionCommand("image.open");
			open.addActionListener(controller);
			popupMenu.add(open);
		}
		{
			rename = new JMenuItem("rename");
			rename.setActionCommand("image.rename");
			rename.addActionListener(controller);
			popupMenu.add(rename);
		}
		{
			turn90Degrees = new JMenuItem("turn90Degrees");
			turn90Degrees.setActionCommand("image.turn90Degrees");
			turn90Degrees.addActionListener(controller);
			popupMenu.add(turn90Degrees);
		}
		{
			removeFromList = new JMenuItem("removeFromList");
			removeFromList.setActionCommand("image.removeFromList");
			removeFromList.addActionListener(controller);
			popupMenu.add(removeFromList);
		}
		
		// Die Buttons erstellen
		GridBagLayout buttonsLayout = new GridBagLayout();
		this.buttons = new JPanel(buttonsLayout);
		{
			this.selectAll = new JButton("selectAll");
			this.selectAll.setActionCommand("selectAll");
			this.selectAll.addActionListener(controller);
			buttonsLayout.setConstraints(selectAll, GridBagHelper.setGridBagConstraints(0, 0));
			this.buttons.add(selectAll);
		}
		{
			this.deselectAll = new JButton("deselectAll");
			this.deselectAll.setActionCommand("deselectAll");
			this.deselectAll.addActionListener(controller);
			buttonsLayout.setConstraints(deselectAll, GridBagHelper.setGridBagConstraints(1, 0));
			this.buttons.add(deselectAll);
		}
		{
			this.clearList = new JButton("clearList");
			this.clearList.setActionCommand("clearList");
			this.clearList.addActionListener(controller);
			buttonsLayout.setConstraints(clearList, GridBagHelper.setGridBagConstraints(2, 0));
			this.buttons.add(clearList);
		}
		{
			this.addImages = new JButton("addImages");
			this.addImages.setActionCommand("addImages");
			this.addImages.addActionListener(controller);
			buttonsLayout.setConstraints(addImages, GridBagHelper.setGridBagConstraints(0, 1, 3, 1));
			this.buttons.add(addImages);
		}
				
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(buttons, BorderLayout.SOUTH);
	}
	
	/**
	 * Diese Methode fügt die neuen Bilder zur Liste hinzu
	 * @param newImages Die neuen Bilder
	 */
	public void addImages(ArrayList<ResizableImage> newImages) {
		this.imageAdder.addImages(newImages); 
		this.imageList.setBounds(getX(), getY(), getWidth(), getHeight());
	}
	
	/**
	 * Diese Methode löscht alle Bilder
	 * @param revalidate Hier wird angegeben, ob die Liste nach dem entfernen neu gezeichnet werden soll
	 */
	public void clearImages(boolean revalidate) {
		for (Component comp : imageList.getComponents()) {
			this.removeImage((ImagePanel) comp, false);
		}
		if (revalidate)
			this.revalidate();
	}
	
	public String getFilterName() {
		return filterName;
	}
	
	public JPopupMenu getPopupMenu(ImagePanel imagePanel) {
		selectedImagePanel = imagePanel;
		return popupMenu;
	}
	
	public ImagePanel getSelectedImagePanel() {
		return selectedImagePanel;
	}
	
	/**
	 * Diese Methode gibt die ausgewählten Bilder zurück
	 * @return Die ausgewählten Bilder
	 */
	public ArrayList<ResizableImage> getSelectedImages() {
		ArrayList<ResizableImage> selectedImages = new ArrayList<ResizableImage>();	
		for (Component comp : imageList.getComponents()) {
			ImagePanel panel = (ImagePanel) comp;
			if (panel.isSelected())
				selectedImages.add(panel.getImage());
		}
		return selectedImages;
	}
	
	/**
	 * Diese Methode lädt die Konfiguration
	 */
	public void loadConfiguration(boolean loadLastImages) {
		if (loadLastImages) {
			this.addImages(Settings.getImages());
		}
		else {
			Settings.clearImages();
		}
	}
	
	/**
	 * Diese Methode lädt den Text
	 */
	public void loadText() {
		Element imageList = Settings.getLanguageFile().getRootElement().getChild("panels").getChild("imageList"); 
		{
			selectAll.setText(imageList.getChild("selectAll").getText());
			deselectAll.setText(imageList.getChild("deselectAll").getText());
			clearList.setText(imageList.getChild("clearList").getText());
			addImages.setText(imageList.getChild("addImages").getText());
			filterName = new String(imageList.getChild("images").getText());
		}
		Element menu = Settings.getLanguageFile().getRootElement().getChild("panels").getChild("imageList").getChild("menu"); 
		{
			open.setText(menu.getChild("open").getText());
			rename.setText(menu.getChild("rename").getText());
			turn90Degrees.setText(menu.getChild("turn90Degrees").getText());
			removeFromList.setText(menu.getChild("removeFromList").getText());
		}
		imageAdder.loadText();
	}
	
	/**
	 * Diese Methode löscht ein einzelnes Bild aus der Bilderliste
	 * @param selectedImagePanel Das Bild, das gelöscht werden soll
	 * @param revalidate Hier wird angegeben, ob die Liste nach dem entfernen neu gezeichnet werden soll
	 */
	public void removeImage(ImagePanel selectedImagePanel, boolean revalidate) {
		Settings.removeImage(selectedImagePanel.getImage());
		imageList.remove(selectedImagePanel);
		if (revalidate)
			this.revalidate();
	}
	
	/**
	 * Diese Methode selektiert und deselektiert alle Bilder
	 * @param select Falls <code>true</code> werden alle Bilder sekeltiert, ansonsten desekeltiert
	 */
	public void selectAllImages(Boolean select) {
		for (Component comp : imageList.getComponents()) {
			ImagePanel panel = (ImagePanel) comp;
			panel.setSelected(select);
		}
	}
}
