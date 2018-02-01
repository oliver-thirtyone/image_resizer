package view;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import model.ImageResizer;

import org.jdom.Element;

import settings.Settings;
import classes.GridBagHelper;
import controller.OptionsController;

/**
 * Diese Methode zeigt das Panel für die Optionen an
 * @author Oliver Streuli
 * @see JPanel
 */
public class OptionsView extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JRadioButton askBeforeRun;
	private TitledBorder border;
	private JButton bSelectOutputDir;
	private ButtonGroup buttonGroup;
	private JRadioButton dontOverwriteImages;
	private JLabel lOutputDir;
	private JTextField outputDir;
	private JRadioButton overwriteImages;
	private JLabel overwriteImagesLabel;
	
	/**
	 * @param controller Der Controller diese Panels
	 */
	public OptionsView(OptionsController controller) {
		this.setLayout(null);
		
		border = new TitledBorder(GridBagHelper.getStandardBorder(), "options");
		this.setBorder(border);
		
		// Bilder überschreiben
		overwriteImagesLabel = new JLabel("overwriteImages");
		overwriteImagesLabel.setBounds(10, 20, 185, 20);
		this.add(overwriteImagesLabel);		
		buttonGroup = new ButtonGroup();
		{
			overwriteImages = new JRadioButton("overwriteImages");
			overwriteImages.setActionCommand("overwriteImages");
			overwriteImages.addActionListener(controller);
			overwriteImages.setBounds(20, 45, 220, 20);
			buttonGroup.add(overwriteImages);
			this.add(overwriteImages);
			
			askBeforeRun = new JRadioButton("askBeforeRun");
			askBeforeRun.setActionCommand("askBeforeRun");
			askBeforeRun.addActionListener(controller);
			askBeforeRun.setBounds(20, 65, 220, 20);
			buttonGroup.add(askBeforeRun);
			this.add(askBeforeRun);
			
			dontOverwriteImages = new JRadioButton("dontOverwriteImages");
			dontOverwriteImages.setActionCommand("dontOverwriteImages");
			dontOverwriteImages.addActionListener(controller);
			dontOverwriteImages.setBounds(20, 85, 220, 20);
			buttonGroup.add(dontOverwriteImages);
			this.add(dontOverwriteImages);
		}
		
		// Zielordner
		lOutputDir = new JLabel("outputDir");
		lOutputDir.setBounds(10, 115, 185, 20);
		this.add(lOutputDir);
		{
			outputDir = new JTextField(Settings.getLanguageFilePath());
			outputDir.setActionCommand("outputDir");
			outputDir.addActionListener(controller);
			outputDir.setEditable(false);
			outputDir.setBounds(10, 140, 180, 20);
			this.add(outputDir);
			
			ImageIcon buttonImage = new ImageIcon("image/open.gif");
			
			bSelectOutputDir = new JButton(buttonImage);
			bSelectOutputDir.setActionCommand("selectOutputDir");
			bSelectOutputDir.addActionListener(controller);
			bSelectOutputDir.setBounds(193, 140, 30, 19);
			this.add(bSelectOutputDir);
		}	
	}
	
	/**
	 * Diese Methode lädt den Text
	 */
	public void loadText() {
		Element options = Settings.getLanguageFile().getRootElement().getChild("panels").getChild("options"); 
		{			
			border.setTitle(options.getAttributeValue("name"));
			overwriteImagesLabel.setText(options.getChild("overwriteImages").getAttributeValue("name"));
			overwriteImages.setText(options.getChild("overwriteImages").getChild("overwriteImages").getText());
			askBeforeRun.setText(options.getChild("overwriteImages").getChild("askBeforeRun").getText());
			dontOverwriteImages.setText(options.getChild("overwriteImages").getChild("dontOverwriteImages").getText());
			lOutputDir.setText(options.getChild("outputDir").getText());
		}
	}
	
	/**
	 * Diese Methode erneueret den Zielordner
	 */
	public void refreshOutputDir() {
		this.outputDir.setText( Settings.getOutputDir() );
	}
	
	/**
	 * Diese Methode wählt die Art des Überscheibens
	 * @param overwriteType Die gewählte Art des überscheibens
	 */
	public void selectOverwriteType(int overwriteType) {
		if (overwriteType == ImageResizer.OVERWRITE_IMAGES)
			this.overwriteImages.setSelected(true);
		else if (overwriteType == ImageResizer.ASK_BEFORE_OVERWRITE_IMAGES)
			this.askBeforeRun.setSelected(true);
		else if (overwriteType == ImageResizer.DONT_OVERWRITE_IMAGES)
			this.dontOverwriteImages.setSelected(true);
	}
	
	/**
	 * Diese Methode lädt die Konfiguration
	 */
	protected void loadConfiguration() {	
		if (Settings.getOverwriteType() == ImageResizer.OVERWRITE_IMAGES) {
			this.overwriteImages.setSelected(true);	
		}
		else if (Settings.getOverwriteType() == ImageResizer.ASK_BEFORE_OVERWRITE_IMAGES) {
			this.askBeforeRun.setSelected(true);
		}
		else if (Settings.getOverwriteType() == ImageResizer.DONT_OVERWRITE_IMAGES) {
			this.dontOverwriteImages.setSelected(true);
		}
		this.refreshOutputDir();
	}
}
