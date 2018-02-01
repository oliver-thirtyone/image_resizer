package classes.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Diese Klasse zeigt einen Nachrichten-Dialog an
 * @author Oliver Streuli
 * @see Dialog
 */
public class MessageDialog extends Dialog {

	private static final long serialVersionUID = 1L;
	
	public static final int ERROR 		= JOptionPane.ERROR_MESSAGE;
	public static final int INFORMATION = JOptionPane.INFORMATION_MESSAGE;
	public static final int QUESTION 	= JOptionPane.QUESTION_MESSAGE;
	
	private Component component;
	private int type;
	
	/**
	 * @param title Der Titel des Dialoges
	 * @param message Die Nachricht, die angezeigt wird
	 * @param type Der Type des Dialoges (<code>ERROR</code>, <code>INFORMATION</code>, <code>QUESTION</code>)
	 * @param component Das JFrame, das den Dialog aufruft
	 */
	public MessageDialog(String title, String message, int type, Component component) {
		super(title, message);
		this.type 	 = type;
		this.setComponent(component);
	}
	
	public Component getComponent() {
		return component;
	}
	
	public int getType() {
		return type;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	/**
	 * Diese Methode zeigt den Dialog an und gibt <code>true</code> zurück falls er bestätigt wurde (OK-Button).
	 * Ansonsten wird <code>false</code> zurückgegeben.
	 */
	public boolean showDialog() {
		boolean ret = false;
		
		if (type == ERROR || type == INFORMATION) {
			JOptionPane.showMessageDialog(component, getMessage(), getTitle(), type);
			ret = true;
		}
		else if (type == QUESTION) {
			if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(component, getMessage(), getTitle(), JOptionPane.YES_NO_OPTION))
				ret = true;
			else
				ret = false;
		}
		
		return ret;
	}
}
