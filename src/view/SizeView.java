package view;

import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import model.ImageResizer;
import model.ImageSize;

import org.jdom.Element;

import settings.Settings;
import classes.GridBagHelper;
import controller.SizeController;

/**
 * Diese Methode zeigt das Panel für die Grösse
 * @author Oliver Streuli
 * @see JPanel
 */
public class SizeView extends JPanel {

	private static final long serialVersionUID = 1L;

	private TitledBorder border;
	private ButtonGroup buttonGroup;
	private JCheckBox cbHeight;
	private JComboBox cbStandardSize;
	private JCheckBox cbWidth;
	private JRadioButton defineSize;
	private JFormattedTextField[] height = new JFormattedTextField[2];
	private NumberFormat integerFormat;
	private JLabel lHeight;
	private JLabel lWidth;
	private JRadioButton maximumSize;
	private JLabel[] pixel = new JLabel[5];
	private JRadioButton standardSize;
	
	private JFormattedTextField[] width = new JFormattedTextField[2];
	
	/**
	 * @param controller Der Controller diese Panels
	 */
	public SizeView(SizeController controller) {
		this.setLayout(null);
		
		border = new TitledBorder(GridBagHelper.getStandardBorder(), "size");
		this.setBorder(border);
		
		integerFormat = NumberFormat.getIntegerInstance();
		integerFormat.setMinimumIntegerDigits(1);
		integerFormat.setMaximumIntegerDigits(5);
		
		// Standardgrösse
		buttonGroup = new ButtonGroup();
		standardSize = new JRadioButton("standardSize");
		standardSize.setActionCommand("standardSize");
		standardSize.addActionListener(controller);
		standardSize.setBounds(10, 20, 185, 20);
		buttonGroup.add(standardSize);
		this.add(standardSize);
		{
			cbStandardSize = new JComboBox(Settings.fillStandardSizes());
			cbStandardSize.setActionCommand("standardSize");
			cbStandardSize.addActionListener(controller);
			cbStandardSize.setSelectedItem(Settings.getStandardSize());
			cbStandardSize.setBounds(30, 45, 130, 20);
			this.add(cbStandardSize);
			
			pixel[0] = new JLabel("pixel");
			pixel[0].setBounds(165, 45, 50, 20);
			this.add(pixel[0]);
		}
		
		// Definierte Grösse
		defineSize = new JRadioButton("defineSize");
		defineSize.setActionCommand("defineSize");
		defineSize.addActionListener(controller);
		defineSize.setBounds(10, 75, 185, 20);
		buttonGroup.add(defineSize);
		this.add(defineSize);
		{
			cbWidth = new JCheckBox("width");
			cbWidth.setActionCommand("defineWidth");
			cbWidth.addItemListener(controller);
			cbWidth.setBounds(25, 100, 65, 20);
			this.add(cbWidth);
			
			width[0] = new JFormattedTextField(integerFormat);
			width[0].setBounds(90, 100, 70, 20);
			this.add(width[0]);
			
			pixel[2] = new JLabel("pixel");
			pixel[2].setBounds(165, 100, 50, 20);
			this.add(pixel[2]);
		}
		{
			cbHeight = new JCheckBox("height");
			cbHeight.setActionCommand("defineHeight");
			cbHeight.addItemListener(controller);
			cbHeight.setBounds(25, 120, 65, 20);
			this.add(cbHeight);
			
			height[0] = new JFormattedTextField(integerFormat);
			height[0].setBounds(90, 120, 70, 20);
			this.add(height[0]);
			
			pixel[1] = new JLabel("pixel");
			pixel[1].setBounds(165, 120, 50, 20);
			this.add(pixel[1]);
		}
		
		// Maximale Grösse
		maximumSize = new JRadioButton("maximumSize");
		maximumSize.setActionCommand("maximumSize");
		maximumSize.addActionListener(controller);
		maximumSize.setBounds(10, 150, 185, 20);
		buttonGroup.add(maximumSize);
		this.add(maximumSize);
		{
			lWidth = new JLabel("width");
			lWidth.setBounds(45, 175, 65, 20);
			this.add(lWidth);
			
			width[1] = new JFormattedTextField(integerFormat);
			width[1].setBounds(90, 175, 70, 20);
			this.add(width[1]);
			
			pixel[4] = new JLabel("pixel");
			pixel[4].setBounds(165, 175, 50, 20);
			this.add(pixel[4]);
		}
		{
			lHeight = new JLabel("height");
			lHeight.setBounds(45, 195, 65, 20);
			this.add(lHeight);
			
			height[1] = new JFormattedTextField(integerFormat);
			height[1].setBounds(90, 195, 70, 20);
			this.add(height[1]);
			
			pixel[3] = new JLabel("pixel");
			pixel[3].setBounds(165, 195, 50, 20);
			this.add(pixel[3]);
		}	
	}
	
	/**
	 * Diese Methode aktiviert die "Definierte Grösse" und deaktiviert alle andern
	 */
	public void enableDefineSize() {
		this.disableAll();
		
		cbHeight.setEnabled(true);
		cbWidth.setEnabled(true);
		pixel[1].setEnabled(true);
		pixel[2].setEnabled(true);
		
		if ( Settings.isDefinedWidthSelected() ) {
			cbWidth.setSelected(true);
			width[0].setEnabled(true);
			width[0].requestFocus();
		} else {
			cbWidth.setSelected(false);
		}
		
		if ( Settings.isDefinedHeightSelected() ) {
			cbHeight.setSelected(true);
			height[0].setEnabled(true);
			height[0].requestFocus();
		} else {
			cbHeight.setSelected(false);
		}
	}
	
	/**
	 * Diese Methode aktiviert die "Maximale Grösse" und deaktiviert alle andern
	 */
	public void enableMaximumSize() {
		this.disableAll();
		lHeight.setEnabled(true);
		height[1].setEnabled(true);
		pixel[3].setEnabled(true);
		height[1].requestFocus();
		
		lWidth.setEnabled(true);
		width[1].setEnabled(true);	
		pixel[3].setEnabled(true);
		pixel[4].setEnabled(true);
	}
	
	/**
	 * Diese Methode aktiviert die "Standardgrösse" und deaktiviert alle andern
	 */
	public void enableStandardSize() {
		this.disableAll();
		cbStandardSize.setEnabled(true);
		pixel[0].setEnabled(true);	
	}
	
	/**
	 * Diese Methode lädt den Text
	 */
	public void loadText() {
		Element size = Settings.getLanguageFile().getRootElement().getChild("panels").getChild("size"); 
		{			
			border.setTitle(size.getAttributeValue("name"));	
			standardSize.setText(size.getChild("standardSize").getText());
			defineSize.setText(size.getChild("defineSize").getText());
			maximumSize.setText(size.getChild("maximumSize").getText());
			cbHeight.setText(size.getChild("height").getText());
			cbWidth.setText(size.getChild("width").getText());
			lHeight.setText(size.getChild("height").getText());
			lWidth.setText(size.getChild("width").getText());
			pixel[0].setText(size.getChild("pixel").getText());
			pixel[1].setText(size.getChild("pixel").getText());
			pixel[2].setText(size.getChild("pixel").getText());
			pixel[3].setText(size.getChild("pixel").getText());
			pixel[4].setText(size.getChild("pixel").getText());
		}
	}
	
	/**
	 * Diese Methode deaktiviert alle Felder
	 */
	private void disableAll() {
		cbStandardSize.setEnabled(false);
		cbHeight.setEnabled(false);
		cbWidth.setEnabled(false);
		lHeight.setEnabled(false);
		lWidth.setEnabled(false);
		height[0].setEnabled(false);
		height[1].setEnabled(false);
		width[0].setEnabled(false);
		width[1].setEnabled(false);
		pixel[0].setEnabled(false);
		pixel[1].setEnabled(false);
		pixel[2].setEnabled(false);
		pixel[3].setEnabled(false);
		pixel[4].setEnabled(false);
	}
	
	/**
	 * Diese Methode lädt die Konfiguration
	 */
	protected void loadConfiguration() {
		this.cbStandardSize.setSelectedItem(Settings.getStandardSize());	
		this.width[0].setText( (Settings.getDefinedSize().getWidth()   > ImageResizer.MIN_SIZE.getWidth())  ? Settings.getDefinedSize().getWidth().toString() : "");
		this.width[1].setText( (Settings.getMaximumSize().getWidth()   > ImageResizer.MIN_SIZE.getWidth())  ? Settings.getMaximumSize().getWidth().toString() : "");
		this.height[0].setText( (Settings.getDefinedSize().getHeight() > ImageResizer.MIN_SIZE.getHeight()) ? Settings.getDefinedSize().getHeight().toString() : "");
		this.height[1].setText( (Settings.getMaximumSize().getHeight() > ImageResizer.MIN_SIZE.getHeight()) ? Settings.getMaximumSize().getHeight().toString() : "");
		
		if ( Settings.getSizeType() == ImageResizer.STANDARD_SIZE ) {
			this.standardSize.setSelected(true);
			this.enableStandardSize();		
		}
		else if ( Settings.getSizeType() == ImageResizer.DEFINED_SIZE ) {
			this.defineSize.setSelected(true);
			this.enableDefineSize();
		}
		else if ( Settings.getSizeType() == ImageResizer.MAXIMUM_SIZE ) {
			this.maximumSize.setSelected(true);
			this.enableMaximumSize();
		}
	}
	
	/**
	 * Diese Methode validiert und speichert die einegebebene Grösse
	 * @return Gibt <code>true</code> zurück falls die Grösse valid ist, anstonsten <code>false</code>
	 */
	protected boolean saveEnterdSize() {
		boolean ret = true;
		
		{
			Settings.setStandardSize( (ImageSize) cbStandardSize.getSelectedItem() );
		}
		
		if ( (!width[0].getText().isEmpty() && cbWidth.isSelected()) || (!height[0].getText().isEmpty() && cbHeight.isSelected()) ){
			Integer w = cbWidth.isSelected() ? new Integer(width[0].getText()) : new Integer(-1);
			Integer h = cbHeight.isSelected() ? new Integer(height[0].getText()) : new Integer(-1);
			
			Settings.setDefinedSize(new ImageSize(w, h));
		}
		else if ( width[0].getText().isEmpty() && height[0].getText().isEmpty() && Settings.getSizeType() == ImageResizer.DEFINED_SIZE ) {
			ret = false;
		}
		
		if ( !width[1].getText().isEmpty() && !height[1].getText().isEmpty() ){
			Integer w = new Integer(width[1].getText());
			Integer h = new Integer(height[1].getText());		
			Settings.setMaximumSize(new ImageSize(w, h));
		}
		else if ( Settings.getSizeType() == ImageResizer.MAXIMUM_SIZE ) {
			ret = false;
		}
		
		return ret;
	}
	
}
