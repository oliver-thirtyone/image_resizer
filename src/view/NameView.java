package view;

import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jdom.Element;

import settings.Settings;
import classes.GridBagHelper;
import controller.NameController;

/**
 * Diese Methode zeigt das Panel für das Umebennen an
 * @author Oliver Streuli
 * @see JPanel
 */
public class NameView extends JPanel {

	private static final long serialVersionUID = 1L;

	private TitledBorder border;
	private NumberFormat integerFormat;
	private JLabel lName;
	private JLabel lNumberFrom;
	private JTextField name;
	private JTextField numberFrom;
	
	private JCheckBox rename;
	
	/**
	 * @param controller Der Controller diese Panels
	 */
	public NameView(NameController controller) {
		this.setLayout(null);
		
		border = new TitledBorder(GridBagHelper.getStandardBorder(), "name");
		this.setBorder(border);
		
		integerFormat = NumberFormat.getIntegerInstance();
		integerFormat.setMinimumIntegerDigits(1);
		integerFormat.setMaximumIntegerDigits(5);
		
		rename = new JCheckBox("rename");
		rename.setActionCommand("rename");
		rename.addItemListener(controller);
		rename.setBounds(10, 20, 185, 20);
		this.add(rename);
		{
			lName = new JLabel("name");
			lName.setBounds(30, 45, 35, 20);
			this.add(lName);
			
			name = new JTextField();
			name.setBounds(70, 45, 125, 20);
			this.add(name);
		}
		{
			lNumberFrom = new JLabel("numberFrom");
			lNumberFrom.setBounds(30, 65, 105, 20);
			this.add(lNumberFrom);
			
			numberFrom = new JFormattedTextField(integerFormat);
			numberFrom.setBounds(130, 65, 65, 20);
			this.add(numberFrom);
		}
	}
	
	/**
	 * Diese Methode aktiviert das Umebennen
	 */
	public void enableRename() {
		Boolean enabled = Settings.isRename();
		lName.setEnabled(enabled);
		name.setEnabled(enabled);
		lNumberFrom.setEnabled(enabled);
		numberFrom.setEnabled(enabled);
	}
	
	/**
	 * Diese Methode lädt den Text
	 */
	public void loadText() {
		Element name = Settings.getLanguageFile().getRootElement().getChild("panels").getChild("name"); 
		{			
			border.setTitle(name.getAttributeValue("name"));
			rename.setText(name.getChild("rename").getText());
			lName.setText(name.getChild("name").getText());
			lNumberFrom.setText(name.getChild("numberFrom").getText());
		}
	}
	
	/**
	 * Diese Methode lädt die Konfiguration
	 */
	protected void loadConfiguration() {
		name.setText( Settings.getRenameName() );	
		numberFrom.setText( (Settings.getRenameNumberFrom() >= 0) ? Settings.getRenameNumberFrom().toString() : "" );
		if ( Settings.isRename() )
			rename.setSelected(true);
		else
			rename.setSelected(false);
		this.enableRename();
	}
	
	/**
	 * Diese Methode validiert und speichert den eingegeben Namen
	 * @return Gibt <code>true</code> zurück falls der Name valid ist, anstonsten <code>false</code>
	 */
	protected boolean saveEnterdName() {
		boolean ret = true;	
		if (!name.getText().isEmpty()) {
				Settings.setRenameName(name.getText());
		}
		else {
			ret = false;
		}
		if (!numberFrom.getText().isEmpty()) {
			Settings.setRenameNumberFrom(new Integer(numberFrom.getText()));
		}
		else {
			ret = false;
		}		
		return ret;
	}
	
}
